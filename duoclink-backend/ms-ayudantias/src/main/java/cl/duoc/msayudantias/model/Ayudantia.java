package cl.duoc.msayudantias.model;

public class Ayudantia {
    private String id;
    private String materia;
    private int cupo;
    private int inscritos;
    private String horario;
    private String dia;
    private String lugar;
    private Autor autor;
    private String createdAt;

    public Ayudantia() {
    }

    public Ayudantia(String id, String materia, int cupo, int inscritos, String horario, String dia, String lugar, Autor autor, String createdAt) {
        this.id = id;
        this.materia = materia;
        this.cupo = cupo;
        this.inscritos = inscritos;
        this.horario = horario;
        this.dia = dia;
        this.lugar = lugar;
        this.autor = autor;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMateria() {
        return materia;
    }

    public void setMateria(String materia) {
        this.materia = materia;
    }

    public int getCupo() {
        return cupo;
    }

    public void setCupo(int cupo) {
        this.cupo = cupo;
    }

    public int getInscritos() {
        return inscritos;
    }

    public void setInscritos(int inscritos) {
        this.inscritos = inscritos;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
