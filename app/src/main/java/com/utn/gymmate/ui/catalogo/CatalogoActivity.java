package com.utn.gymmate.ui.catalogo;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.utn.gymmate.R;
import com.utn.gymmate.adapters.EjercicioAdapter;
import com.utn.gymmate.data.EjercicioRepository;

public class CatalogoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_catalogo);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Marca "Ejercicios" en la barra inferior
        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);
        bottomNav.setSelectedItemId(R.id.nav_ejercicios);

        // La lista que leemos del JSON se la damos al adapter
        RecyclerView lista = findViewById(R.id.listaEjercicios);
        lista.setAdapter(new EjercicioAdapter(EjercicioRepository.obtenerTodos(this), ejercicio -> {
            Intent intent = new Intent(this, DetalleEjercicioActivity.class);
            intent.putExtra(DetalleEjercicioActivity.EXTRA_EJERCICIO_ID, ejercicio.getId());
            startActivity(intent);
        }));
    }
}