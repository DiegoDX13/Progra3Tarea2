package sifac.padron.entidades;

public class Distrito {
    private String codigo, provincia, canton, distrito;

    public Distrito(String codigo, String provincia, String canton, String distrito) {
        this.codigo = codigo; this.provincia = provincia;
        this.canton = canton; this.distrito = distrito;
    }
    public String getCodigo()  { return codigo; }
    public String getUbicacion() { return distrito + ", " + canton + ", " + provincia; }
}
