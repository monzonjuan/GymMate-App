package com.utn.gymmate.models;

import java.util.List;

/**
 * Un ejercicio del catalogo.
 *
 * Esto es un POJO (Plain Old Java Object): una clase que SOLO guarda datos.
 * No dibuja nada, no sabe que existe Android, no toca la base de datos.
 * Si maniana cambiamos de API o pasamos a Room, esta clase no se entera.
 *
 * Los campos son private y se leen con getters. Es la convencion de Java y
 * ademas es lo que esperan las librerias que vamos a usar despues (Gson para
 * leer el JSON de la API, Room para las tablas).
 */
public class Ejercicio {

    private final String id;
    private final String nombre;
    private final String grupoMuscular;        // Pecho, Espalda, Piernas...
    private final String nivel;                // Principiante, Intermedio, Avanzado
    private final String gifUrl;               // la animacion que baja de la API
    private final String musculosSecundarios;  // "Triceps y Hombros"
    private final List<String> instrucciones;  // cantidad variable de pasos
    private final String consejo;

    public Ejercicio(String id,
                     String nombre,
                     String grupoMuscular,
                     String nivel,
                     String gifUrl,
                     String musculosSecundarios,
                     List<String> instrucciones,
                     String consejo) {
        this.id = id;
        this.nombre = nombre;
        this.grupoMuscular = grupoMuscular;
        this.nivel = nivel;
        this.gifUrl = gifUrl;
        this.musculosSecundarios = musculosSecundarios;
        this.instrucciones = instrucciones;
        this.consejo = consejo;
    }

    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public String getGrupoMuscular() { return grupoMuscular; }
    public String getNivel() { return nivel; }
    public String getGifUrl() { return gifUrl; }
    public String getMusculosSecundarios() { return musculosSecundarios; }
    public List<String> getInstrucciones() { return instrucciones; }
    public String getConsejo() { return consejo; }

    /**
     * El subtitulo que se muestra en cada fila del catalogo: "Pecho - Intermedio".
     *
     * Lo arma el modelo y no el Adapter a proposito: es una regla sobre los
     * datos, no sobre como se dibujan. Si maniana el formato cambia, se toca
     * en un solo lugar.
     */
    public String getSubtitulo() {
        return grupoMuscular + " - " + nivel;
    }
}
