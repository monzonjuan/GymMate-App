package com.utn.gymmate.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.utn.gymmate.R;
import com.utn.gymmate.data.EjercicioRepository;
import com.utn.gymmate.models.Ejercicio;

import java.util.List;

public class EjercicioAdapter extends RecyclerView.Adapter<EjercicioAdapter.EjercicioViewHolder> {

    // El adapter avisa "tocaron este ejercicio"; quien lo usa decide que hacer.
    public interface OnEjercicioClickListener {
        void onEjercicioClick(Ejercicio ejercicio);
    }

    private final List<Ejercicio> ejercicios;
    private final OnEjercicioClickListener listener;

    public EjercicioAdapter(List<Ejercicio> ejercicios, OnEjercicioClickListener listener) {
        this.ejercicios = ejercicios;
        this.listener = listener;
    }

    @NonNull
    @Override
    public EjercicioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View fila = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_ejercicio, parent, false);
        return new EjercicioViewHolder(fila);
    }

    @Override
    public void onBindViewHolder(@NonNull EjercicioViewHolder holder, int position) {
        Ejercicio ejercicio = ejercicios.get(position);
        holder.nombre.setText(ejercicio.getNombre());
        holder.detalle.setText(ejercicio.getSubtitulo());

        // asBitmap() = solo el primer cuadro del GIF (en la lista no queremos
        // 15 animaciones a la vez). Si falta el archivo, muestra el placeholder.
        Glide.with(holder.itemView)
                .asBitmap()
                .load(EjercicioRepository.uriGif(ejercicio))
                .placeholder(R.drawable.ic_gym_placeholder)
                .error(R.drawable.ic_gym_placeholder)
                .into(holder.miniatura);

        holder.itemView.setOnClickListener(v -> listener.onEjercicioClick(ejercicio));
    }

    @Override
    public int getItemCount() {
        return ejercicios.size();
    }

    static class EjercicioViewHolder extends RecyclerView.ViewHolder {
        final ImageView miniatura;
        final TextView nombre;
        final TextView detalle;

        EjercicioViewHolder(@NonNull View itemView) {
            super(itemView);
            miniatura = itemView.findViewById(R.id.miniaturaEjercicio);
            nombre = itemView.findViewById(R.id.nombreEjercicioItem);
            detalle = itemView.findViewById(R.id.detalleEjercicioItem);
        }
    }
}