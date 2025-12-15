package cl.duoc.msayudantias.model;

public class Autor {
    private String uid;
    private String nombre;

    public Autor() {
    }

    public Autor(String uid, String nombre) {
        this.uid = uid;
        this.nombre = nombre;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
