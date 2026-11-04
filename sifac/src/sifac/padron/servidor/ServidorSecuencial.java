package sifac.padron.servidor;

import java.io.*;
import java.net.*;
import sifac.padron.datos.RepositorioPadron;
import sifac.padron.entidades.Elector;

/**
 * FASE 1 · un solo cliente a la vez. Aqui esta la falla que motiva el hilo.
 */
public class ServidorSecuencial {

    public static final int PUERTO = 5050;

    public static void main(String[] args) throws Exception {
        String rutaPadron = args.length > 0 ? args[0] : "PADRON_SINTETICO.txt";
        String rutaDistelec = args.length > 1 ? args[1] : "Distelec.txt";

        RepositorioPadron repo = new RepositorioPadron();
        repo.cargar(rutaPadron, rutaDistelec);
        System.out.println("[servidor] padron cargado: " + repo.cantidad() + " electores");

        ServerSocket servidor = new ServerSocket(PUERTO);
        System.out.println("[servidor] escuchando en el puerto " + PUERTO);

        int atendidos = 0;
        int limite = args.length > 2 ? Integer.parseInt(args[2]) : Integer.MAX_VALUE;

        while (atendidos < limite) {
            Socket s = servidor.accept();
            System.out.println("[servidor] conectado " + s.getPort());
            atender(s, repo);
            atendidos++;
        }
        servidor.close();
        System.out.println("[servidor] cerrado");
    }

    static void atender(Socket s, RepositorioPadron repo) throws Exception {
        BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
        PrintWriter out = new PrintWriter(s.getOutputStream(), true);
        String cedula;
        while ((cedula = in.readLine()) != null) {
            if (cedula.equals("SALIR")) {
                break;
            }
            Thread.sleep(1500);                   // simula una consulta pesada
            Elector e = repo.buscarBinaria(cedula.trim());
            out.println(e == null ? "NOTFOUND"
                    : "OK|" + e.getCedula() + "|" + e.getNombreCompleto()
                    + "|" + repo.ubicacionDe(e) + "|junta " + e.getJunta());
        }
        s.close();
        System.out.println("[servidor] desconectado " + s.getPort());
    }
}
