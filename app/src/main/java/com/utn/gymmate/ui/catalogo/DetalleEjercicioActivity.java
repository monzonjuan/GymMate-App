package com.utn.gymmate.ui.catalogo;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.chip.Chip;
import com.utn.gymmate.R;
import com.utn.gymmate.data.EjercicioRepository;
import com.utn.gymmate.models.Ejercicio;

public class DetalleEjercicioActivity extends AppCompatActivity {

    // Clave con la que viaja el id en el Intent.
    public static final String EXTRA_EJERCICIO_ID = "ejercicio_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detalle_ejercicio);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 1) Recibimos el id y buscamos el ejercicio completo.
        String id = getIntent().getStringExtra(EXTRA_EJERCICIO_ID);
        Ejercicio e = (id == null) ? null : EjercicioRepository.buscarPorId(this, id);
        if (e == null) {
            finish();   // no deberia pasar, pero no dejamos la pantalla vacia
            return;
        }

        // 2) Volver y barra inferior.
        findViewById(R.id.btnVolver).setOnClickListener(v -> finish());
        ((BottomNavigationView) findViewById(R.id.bottomNav))
                .setSelectedItemId(R.id.nav_ejercicios);

        // 3) Textos.
        ((TextView) findViewById(R.id.nombreEjercicio)).setText(e.getNombre());
        ((TextView) findViewById(R.id.descripcionEjercicio)).setText(e.getDescripcion());
        ((TextView) findViewById(R.id.instruccionesPasos)).setText(armarPasos(e));
        ((TextView) findViewById(R.id.consejoTecnico)).setText(e.getConsejo());

        ((Chip) findViewById(R.id.chipMusculoPrincipal))
                .setText(e.getGrupoMuscular() + " (Principal)");
        Chip chipSecundarios = findViewById(R.id.chipMusculosSecundarios);
        if (e.getMusculosSecundarios().isEmpty()) {
            chipSecundarios.setVisibility(View.GONE);
        } else {
            chipSecundarios.setText(TextUtils.join(", ", e.getMusculosSecundarios()));
        }

        // 4) El GIF: Glide detecta que es un GIF y lo anima solo.
        ImageView gif = findViewById(R.id.gifEjercicio);
        Glide.with(this)
                .load(EjercicioRepository.uriGif(e))
                .placeholder(R.drawable.ic_gym_placeholder)
                .error(R.drawable.ic_gym_placeholder)
                .into(gif);
    }

    /** Une la lista de pasos en un solo texto: "1. ...\n2. ...\n3. ..." */
    private String armarPasos(Ejercicio e) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < e.getInstrucciones().size(); i++) {
            if (i > 0) sb.append("\n");
            sb.append(i + 1).append(". ").append(e.getInstrucciones().get(i));
        }
        return sb.toString();
    }
}