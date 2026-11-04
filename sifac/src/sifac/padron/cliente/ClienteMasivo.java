package sifac.padron.cliente;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public final class ClienteMasivo {
    private static final String ARCHIVO_CEDULAS = "cedulas.txt";
    private static final int CANTIDAD_CEDULAS = 200;

    private ClienteMasivo() {
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            throw new IllegalArgumentException(
                    "Uso: java ClienteMasivo <puerto> <cantidad-hilos>");
        }

        int puerto = Integer.parseInt(args[0]);
        int cantidadHilos = Integer.parseInt(args[1]);
        if (puerto < 1 || puerto > 65535) {
            throw new IllegalArgumentException("El puerto debe estar entre 1 y 65535.");
        }
        if (cantidadHilos < 1) {
            throw new IllegalArgumentException("La cantidad de hilos debe ser positiva.");
        }

        List<String> cedulas = Files.readAllLines(
                Paths.get(ARCHIVO_CEDULAS), StandardCharsets.UTF_8);
        if (cedulas.size() != CANTIDAD_CEDULAS) {
            throw new IllegalArgumentException(
                    ARCHIVO_CEDULAS + " debe contener exactamente "
                            + CANTIDAD_CEDULAS + " líneas; contiene " + cedulas.size() + ".");
        }
        for (String cedula : cedulas) {
            if (cedula.trim().isEmpty()) {
                throw new IllegalArgumentException(
                        ARCHIVO_CEDULAS + " no puede contener líneas vacías.");
            }
        }

        List<Consulta> consultas = new ArrayList<>();
        List<Thread> hilos = new ArrayList<>();
        int base = cedulas.size() / cantidadHilos;
        int sobrantes = cedulas.size() % cantidadHilos;
        int inicio = 0;

        for (int i = 0; i < cantidadHilos; i++) {
            int cantidad = base + (i < sobrantes ? 1 : 0);
            int fin = inicio + cantidad;
            Consulta consulta = new Consulta(
                    "hilo-" + (i + 1), puerto, cedulas.subList(inicio, fin));
            consultas.add(consulta);
            hilos.add(new Thread(consulta, consulta.nombre));
            inicio = fin;
        }

        long inicioTotal = System.nanoTime();
        for (Thread hilo : hilos) {
            hilo.start();
        }
        for (Thread hilo : hilos) {
            hilo.join();
        }
        long tiempoTotalMs = (System.nanoTime() - inicioTotal) / 1_000_000;

        for (Consulta consulta : consultas) {
            if (consulta.error != null) {
                throw new IOException("Falló " + consulta.nombre + ".", consulta.error);
            }
        }

        int encontradas = 0;
        int noEncontradas = 0;
        System.out.printf(
                Locale.ROOT, "ClienteMasivo · %d cedulas · %d hilos%n",
                cedulas.size(), cantidadHilos);
        for (Consulta consulta : consultas) {
            encontradas += consulta.encontradas;
            noEncontradas += consulta.noEncontradas;
            System.out.printf(
                    Locale.ROOT,
                    "%s   %d cedulas   encontradas %d   no encontradas %d   t= %d ms%n",
                    consulta.nombre,
                    consulta.cedulas.size(),
                    consulta.encontradas,
                    consulta.noEncontradas,
                    consulta.tiempoMs);
        }
        System.out.printf(Locale.ROOT, "%n");
        System.out.printf(Locale.ROOT, "total encontradas     %d%n", encontradas);
        System.out.printf(Locale.ROOT, "total no encontradas   %d%n", noEncontradas);
        System.out.printf(Locale.ROOT, "tiempo total          %d ms%n", tiempoTotalMs);

        if (encontradas != 150 || noEncontradas != 50) {
            throw new IllegalStateException(
                    "Conteo inesperado: se esperaban 150 encontradas y 50 no encontradas.");
        }
    }

    private static final class Consulta implements Runnable {
        private final String nombre;
        private final int puerto;
        private final List<String> cedulas;
        private int encontradas;
        private int noEncontradas;
        private long tiempoMs;
        private Exception error;

        private Consulta(String nombre, int puerto, List<String> cedulas) {
            this.nombre = nombre;
            this.puerto = puerto;
            this.cedulas = cedulas;
        }

        @Override
        public void run() {
            long inicio = System.nanoTime();
            try (Socket socket = new Socket("localhost", puerto);
                 BufferedReader entrada = new BufferedReader(
                         new InputStreamReader(
                                 socket.getInputStream(), StandardCharsets.UTF_8));
                 BufferedWriter salida = new BufferedWriter(
                         new OutputStreamWriter(
                                 socket.getOutputStream(), StandardCharsets.UTF_8))) {
                for (String cedula : cedulas) {
                    salida.write(cedula);
                    salida.newLine();
                    salida.flush();

                    String respuesta = entrada.readLine();
                    if (respuesta == null) {
                        throw new IOException("El servidor cerró la conexión antes de responder.");
                    }
                    if (respuesta.startsWith("OK|")) {
                        encontradas++;
                    } else if ("NOTFOUND".equals(respuesta)) {
                        noEncontradas++;
                    } else {
                        throw new IOException("Respuesta no reconocida del servidor.");
                    }
                }

                salida.write("SALIR");
                salida.newLine();
                salida.flush();
            } catch (Exception ex) {
                error = ex;
            } finally {
                tiempoMs = (System.nanoTime() - inicio) / 1_000_000;
            }
        }
    }
}
