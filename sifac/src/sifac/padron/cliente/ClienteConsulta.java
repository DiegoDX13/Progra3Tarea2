package sifac.padron.cliente;

import java.io.*;
import java.net.*;

public class ClienteConsulta {
    public static void main(String[] args) throws Exception {
        String etiqueta = args.length > 0 ? args[0] : "CLIENTE";
        int    puerto   = args.length > 1 ? Integer.parseInt(args[1]) : 5051;

        String[] cedulas = args.length > 2
                ? java.util.Arrays.copyOfRange(args, 2, args.length)
                : new String[] { "104560789", "112340567", "999999999" };

        long t0 = System.currentTimeMillis();

        Socket s = new Socket("localhost", puerto);
        BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
        PrintWriter out = new PrintWriter(s.getOutputStream(), true);

        for (String cedula : cedulas) {
            out.println(cedula);
            String r = in.readLine();
            long ms = System.currentTimeMillis() - t0;
            System.out.printf("%-10s t=%5d ms  %s -> %s%n", etiqueta, ms, cedula, r);
        }
        out.println("SALIR");
        s.close();
    }
}