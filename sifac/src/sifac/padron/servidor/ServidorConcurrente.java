package sifac.padron.servidor;

import java.net.*;
import sifac.padron.datos.RepositorioPadron;

/**
 * FASE 2 · un hilo por cliente.
 */
public class ServidorConcurrente {

    public static final int PUERTO = 5051;

    public static void main(String[] args) throws Exception {
        String rutaPadron = args.length > 0 ? args[0] : "PADRON_SINTETICO.txt";
        String rutaDistelec = args.length > 1 ? args[1] : "Distelec.txt";

        RepositorioPadron repo = new RepositorioPadron();
        repo.cargar(rutaPadron, rutaDistelec);
        System.out.println("[servidor] padron cargado: " + repo.cantidad() + " electores");

        ServerSocket servidor = new ServerSocket(PUERTO);
        System.out.println("[servidor] escuchando en el puerto " + PUERTO);

        int n = 0;
        int limite = args.length > 2 ? Integer.parseInt(args[2]) : Integer.MAX_VALUE;

        while (n < limite) {
            Socket s = servidor.accept();
            n++;
            Thread hilo = new Thread(new AtencionCliente(s, repo), "cliente-" + n);
            System.out.println("[servidor] hilo creado " + hilo.getName()
                    + " estado " + hilo.getState());
            hilo.start();
            System.out.println("[servidor] hilo arrancado " + hilo.getName()
                    + " estado " + hilo.getState());
        }
        servidor.close();
        System.out.println("[servidor] no acepta mas conexiones");
    }
}
