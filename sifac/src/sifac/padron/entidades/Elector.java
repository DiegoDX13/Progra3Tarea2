package sifac.padron.entidades;

public class Elector implements Comparable<Elector> {

    private String cedula;
    private String codelec;
    private int    sexo;
    private String caducidad;
    private String junta;
    private String nombre;
    private String apellido1;
    private String apellido2;

    public Elector(String cedula, String codelec, int sexo, String caducidad,
                   String junta, String nombre, String apellido1, String apellido2) {
        this.cedula = cedula;       this.codelec = codelec;
        this.sexo = sexo;           this.caducidad = caducidad;
        this.junta = junta;         this.nombre = nombre;
        this.apellido1 = apellido1; this.apellido2 = apellido2;
    }

    public String getCedula()  { return cedula; }
    public String getCodelec() { return codelec; }
    public int    getSexo()    { return sexo; }
    public String getJunta()   { return junta; }

    public String getNombreCompleto() {
        return nombre + " " + apellido1 + " " + apellido2;
    }

    @Override
    public int compareTo(Elector otro) {
        return this.cedula.compareTo(otro.cedula);
    }

    @Override
    public String toString() {
        return cedula + " " + getNombreCompleto();
    }
}
