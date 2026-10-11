package com.utn.gymmate.data;

import android.content.Context;
import android.util.Log;

import com.utn.gymmate.models.Ejercicio;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Lee el catalogo desde assets/ejercicios.json.
 * Es la unica clase que sabe de donde vienen los ejercicios.
 */
public class EjercicioRepository {

    private static final String TAG = "EjercicioRepository";
    private static final String ARCHIVO_JSON = "ejercicios.json";

    public static List<Ejercicio> obtenerTodos(Context context) {
        List<Ejercicio> lista = new ArrayList<>();

        try (InputStream is = context.getAssets().open(ARCHIVO_JSON);
             BufferedReader br = new BufferedReader(
                     new InputStreamReader(is, StandardCharsets.UTF_8))) {

            // PASO A: el archivo pasa a ser un String
            StringBuilder texto = new StringBuilder();
            String linea;
            while ((linea = br.readLine()) != null) {
                texto.append(linea);
            }

            // PASO B: el String pasa a ser objetos Ejercicio
            JSONArray array = new JSONArray(texto.toString());
            for (int i = 0; i < array.length(); i++) {
                JSONObject o = array.getJSONObject(i);

                if (!o.optBoolean("activo", true)) continue;  // saltea los retirados

                lista.add(new Ejercicio(
                        o.getString("id"),
                        o.getString("nombre"),
                        o.getString("grupoMuscular"),
                        o.getString("nivel"),
                        leerLista(o.getJSONArray("musculosSecundarios")),
                        o.getString("descripcion"),
                        leerLista(o.getJSONArray("instrucciones")),
                        o.getString("archivoGif"),
                        o.getString("consejo")));
            }
        } catch (IOException | JSONException e) {
            Log.e(TAG, "No se pudo leer " + ARCHIVO_JSON, e);
        }

        return lista;
    }

    /** Busca un ejercicio por id; devuelve null si no existe. */
    public static Ejercicio buscarPorId(Context context, String id) {
        for (Ejercicio e : obtenerTodos(context)) {
            if (e.getId().equals(id)) return e;
        }
        return null;
    }

    /** Ruta que Glide entiende para cargar un GIF desde assets/gifs/. */
    public static String uriGif(Ejercicio e) {
        return "file:///android_asset/gifs/" + e.getArchivoGif();
    }

    /** Convierte un JSONArray de textos en una List<String>. */
    private static List<String> leerLista(JSONArray array) throws JSONException {
        List<String> lista = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            lista.add(array.getString(i));
        }
        return lista;
    }
}