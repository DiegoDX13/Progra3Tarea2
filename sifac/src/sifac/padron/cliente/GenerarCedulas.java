package sifac.padron.cliente;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class GenerarCedulas {
    private static final Charset CODIFICACION_PADRON =
            Charset.forName("ISO-8859-15");
    private static final int VALIDAS_REQUERIDAS = 150;
    private static final int INVENTADAS_REQUERIDAS = 50;

    private GenerarCedulas() {
    }

    public static void main(String[] args) throws IOException {
        if (args.length > 2) {
            throw new IllegalArgumentException(
                    "Uso: java GenerarCedulas [archivo-padron] [archivo-salida]");
        }

        Path rutaPadron = Paths.get(args.length > 0 ? args[0] : "PADRON_SINTETICO.txt");
        Path rutaSalida = Paths.get(args.length > 1 ? args[1] : "cedulas.txt");
        List<String> validas = new ArrayList<>(VALIDAS_REQUERIDAS);
        Set<String> validasUnicas = new HashSet<>();
        int longitudMaxima = 0;

        try (BufferedReader lector = Files.newBufferedReader(rutaPadron, CODIFICACION_PADRON)) {
            String linea;
            while ((linea = lector.readLine()) != null) {
                int campos = cantidadCamposComoSplit(linea);
                if (campos < 7) {
                    continue;
                }

                int separador = linea.indexOf(',');
                if (separador < 0) {
                    continue;
                }
                String cedula = linea.substring(0, separador).trim();
                if (!esNumerica(cedula)) {
                    continue;
                }

                longitudMaxima = Math.max(longitudMaxima, cedula.length());
                if (validas.size() < VALIDAS_REQUERIDAS && validasUnicas.add(cedula)) {
                    validas.add(cedula);
                }
            }
        }

        if (validas.size() != VALIDAS_REQUERIDAS) {
            throw new IOException(
                    "Se encontraron solo " + validas.size()
                            + " cédulas numéricas únicas con al menos 8 campos.");
        }
        if (longitudMaxima == 0) {
            throw new IOException("No se pudo determinar la longitud de las cédulas del padrón.");
        }

        List<String> todas = new ArrayList<>(VALIDAS_REQUERIDAS + INVENTADAS_REQUERIDAS);
        todas.addAll(validas);
        int longitudInventadas = longitudMaxima + 1;
        for (int i = 1; i <= INVENTADAS_REQUERIDAS; i++) {
            todas.add(izquierdaConCeros(i, longitudInventadas));
        }
        Collections.shuffle(todas);
        Files.write(rutaSalida, todas, StandardCharsets.UTF_8);

        System.out.println(
                "Generadas 200 cédulas en " + rutaSalida
                        + " (150 del padrón y 50 inventadas).");
    }

    private static int cantidadCamposComoSplit(String linea) {
        int fin = linea.length();
        while (fin > 0 && linea.charAt(fin - 1) == ',') {
            fin--;
        }
        if (fin == 0) {
            return 0;
        }

        int campos = 1;
        for (int i = 0; i < fin; i++) {
            if (linea.charAt(i) == ',') {
                campos++;
            }
        }
        return campos;
    }

    private static boolean esNumerica(String valor) {
        if (valor.isEmpty()) {
            return false;
        }
        for (int i = 0; i < valor.length(); i++) {
            if (valor.charAt(i) < '0' || valor.charAt(i) > '9') {
                return false;
            }
        }
        return true;
    }

    private static String izquierdaConCeros(int valor, int longitud) {
        String digitos = Integer.toString(valor);
        StringBuilder resultado = new StringBuilder(longitud);
        for (int i = digitos.length(); i < longitud; i++) {
            resultado.append('0');
        }
        return resultado.append(digitos).toString();
    }
}
