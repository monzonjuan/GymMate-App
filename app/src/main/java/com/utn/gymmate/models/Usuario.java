package com.utn.gymmate.models;

public class Usuario {

    private String uid;
    private String email;
    private String nombre;
    private Long edad;
    private Double peso;
    private Double altura;
    private String nivel;
    private Long diasDisponibles;
    private String objetivo;

    public Usuario() {
    }

    public Usuario(String uid, String email) {
        this.uid = uid;
        this.email = email;
    }

    public String getUid() { return uid; }
    public void setUid(String uid) { this.uid = uid; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public Long getEdad() { return edad; }
    public void setEdad(Long edad) { this.edad = edad; }

    public Double getPeso() { return peso; }
    public void setPeso(Double peso) { this.peso = peso; }

    public Double getAltura() { return altura; }
    public void setAltura(Double altura) { this.altura = altura; }

    public String getNivel() { return nivel; }
    public void setNivel(String nivel) { this.nivel = nivel; }

    public Long getDiasDisponibles() { return diasDisponibles; }
    public void setDiasDisponibles(Long diasDisponibles) { this.diasDisponibles = diasDisponibles; }

    public String getObjetivo() { return objetivo; }
    public void setObjetivo(String objetivo) { this.objetivo = objetivo; }

    public boolean tienePerfilFisico() {
        return peso != null && altura != null && edad != null
                && nivel != null && diasDisponibles != null && objetivo != null;
    }
}
