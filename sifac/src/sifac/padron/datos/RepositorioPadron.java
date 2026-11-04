package sifac.padron.datos;

import java.io.*;
import java.nio.charset.Charset;
import java.util.*;
import sifac.padron.entidades.*;

public class RepositorioPadron {

    private static final Charset TSE = Charset.forName("ISO-8859-15");

    private List<Elector> electores = new ArrayList<>();
    private List<Distrito> distritos = new ArrayList<>();

    public void cargar(String rutaPadron, String rutaDistelec) throws IOException {
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(rutaDistelec), TSE))) {
            String l;
            while ((l = br.readLine()) != null) {
                String[] c = l.split(",");
                if (c.length < 4) continue;
                distritos.add(new Distrito(c[0].trim(), c[1].trim(), c[2].trim(), c[3].trim()));
            }
        }
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(rutaPadron), TSE))) {
            String l;
            while ((l = br.readLine()) != null) {
                String[] c = l.split(",");
                if (c.length < 8) continue;
                electores.add(new Elector(c[0].trim(), c[1].trim(),
                        Integer.parseInt(c[2].trim()), c[3].trim(), c[4].trim(),
                        c[5].trim(), c[6].trim(), c[7].trim()));
            }
        }
        // PRECONDICION de la Semana 6: se ordena UNA vez, al cargar.
        Collections.sort(electores);
    }

    public int cantidad() { return electores.size(); }

    public Elector buscarBinaria(String cedula) {
        int i = Collections.binarySearch(electores,
                new Elector(cedula, "", 0, "", "", "", "", ""));
        return i >= 0 ? electores.get(i) : null;
    }

    public Elector buscarLineal(String cedula) {
        for (Elector e : electores) {
            if (e.getCedula().equals(cedula)) return e;
        }
        return null;
    }

    public String ubicacionDe(Elector e) {
        for (Distrito d : distritos) {
            if (d.getCodigo().equals(e.getCodelec())) return d.getUbicacion();
        }
        return "DESCONOCIDA";
    }
}
