package com.utn.gymmate.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.slider.Slider;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.utn.gymmate.R;

import java.util.HashMap;
import java.util.Map;


public class PerfilFisicoActivity extends AppCompatActivity {

    private TextInputEditText etPeso;
    private TextInputEditText etEdad;
    private TextInputEditText etAltura;
    private ChipGroup grupoNivel;
    private Slider sliderDias;
    private ChipGroup grupoObjetivos;
    private Button btnGuardarPerfil;
    private TextInputLayout tilEdad, tilPeso, tilAltura;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_fisico);

        etEdad = findViewById(R.id.etEdad);
        etPeso = findViewById(R.id.etPeso);
        etAltura = findViewById(R.id.etAltura);
        grupoNivel = findViewById(R.id.grupoNivel);
        sliderDias = findViewById(R.id.sliderDias);
        grupoObjetivos = findViewById(R.id.grupoObjetivo);
        btnGuardarPerfil = findViewById(R.id.btnGuardarPerfil);
        tilEdad   = findViewById(R.id.tilEdad);
        tilPeso   = findViewById(R.id.tilPeso);
        tilAltura = findViewById(R.id.tilAltura);

        cargarPerfil();

        btnGuardarPerfil.setOnClickListener(v -> leerFormulario());
    }

    private void leerFormulario(){
        if(!formularioValido()) return;

        int edad = (int) aNumero(etEdad.getText().toString().trim());
        double peso = aNumero(etPeso.getText().toString().trim());
        double altura = aNumero(etAltura.getText().toString().trim());
        int dias = (int) sliderDias.getValue();

        String nivel = textoDelChip(grupoNivel);
        String objetivo = textoDelChip(grupoObjetivos);

        guardarPerfil(edad, peso, altura, nivel, dias, objetivo);
    }

    private String textoDelChip(ChipGroup grupo){
        int id = grupo.getCheckedChipId();
        if (id == View.NO_ID) return "";
        Chip chip = grupo.findViewById(id);
        return chip.getText().toString().trim();
    }

    private boolean formularioValido(){
        tilEdad.setError(null);
        tilPeso.setError(null);
        tilAltura.setError(null);

        boolean valido = true;

        String edadTxt = etEdad.getText().toString().trim();
        if (edadTxt.isEmpty()) {
            tilEdad.setError(getString(R.string.error_campos_obligatorios));
            valido = false;
        } else {
            double edad = aNumero(edadTxt);
            if (edad <= 0) {
                tilEdad.setError(getString(R.string.error_numeros_positivos));
                valido = false;
            } else if (edad < 10 || edad > 100) {
                tilEdad.setError(getString(R.string.error_fuera_de_rango));
                valido = false;
            }
        }

        String pesoTxt = etPeso.getText().toString().trim();
        if (pesoTxt.isEmpty()) {
            tilPeso.setError(getString(R.string.error_campos_obligatorios));
            valido = false;
        } else {
            double peso = aNumero(pesoTxt);
            if (peso <= 0) {
                tilPeso.setError(getString(R.string.error_numeros_positivos));
                valido = false;
            } else if (peso < 20 || peso > 400) {
                tilPeso.setError(getString(R.string.error_fuera_de_rango));
                valido = false;
            }
        }

        String alturaTxt = etAltura.getText().toString().trim();
        if (alturaTxt.isEmpty()) {
            tilAltura.setError(getString(R.string.error_campos_obligatorios));
            valido = false;
        } else {
            double altura = aNumero(alturaTxt);
            if (altura <= 0) {
                tilAltura.setError(getString(R.string.error_numeros_positivos));
                valido = false;
            } else if (altura < 100 || altura > 300) {
                tilAltura.setError(getString(R.string.error_fuera_de_rango));
                valido = false;
            }
        }

        return valido;
    }

    private void guardarPerfil(int edad, double peso, double altura,
                               String nivel, int dias, String objetivo) {

        FirebaseUser usuario = FirebaseAuth.getInstance().getCurrentUser();
        if (usuario == null) {
            Toast.makeText(this, "Primero tenes que iniciar sesion", Toast.LENGTH_LONG).show();
            return;
        }

        Map<String, Object> datos = new HashMap<>();
        datos.put("edad", edad);
        datos.put("peso", peso);
        datos.put("altura", altura);
        datos.put("nivel", nivel);
        datos.put("diasDisponibles", dias);
        datos.put("objetivo", objetivo);

        btnGuardarPerfil.setEnabled(false);

        FirebaseFirestore.getInstance()
                .collection("usuarios")
                .document(usuario.getUid())
                .set(datos, SetOptions.merge())
                .addOnSuccessListener(x -> {
                    Toast.makeText(this, R.string.perfil_guardado_ok, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, HomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> {
                    btnGuardarPerfil.setEnabled(true);
                    Toast.makeText(this, R.string.error_guardar_perfil, Toast.LENGTH_LONG).show();
                });
    }

    private double aNumero(String texto) {
        try {
            return Double.parseDouble(texto.replace(',', '.'));
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private void cargarPerfil() {
        FirebaseUser usuario = FirebaseAuth.getInstance().getCurrentUser();
        if (usuario == null) return;

        FirebaseFirestore.getInstance()
                .collection("usuarios")
                .document(usuario.getUid())
                .get()
                .addOnSuccessListener(doc -> {

                    if (!doc.exists()) return;

                    Long edad = doc.getLong("edad");
                    if (edad != null) etEdad.setText(String.valueOf(edad));

                    Double peso = doc.getDouble("peso");
                    if (peso != null) etPeso.setText(String.valueOf(peso));

                    Double altura = doc.getDouble("altura");
                    if (altura != null) etAltura.setText(String.valueOf(altura));

                    Long dias = doc.getLong("diasDisponibles");
                    if(dias != null &&  1 <= dias && dias <= 7) sliderDias.setValue(dias);

                    marcarChipPorTexto(grupoNivel, doc.getString("nivel"));
                    marcarChipPorTexto(grupoObjetivos, doc.getString("objetivo"));

                    btnGuardarPerfil.setText("GUARDAR CAMBIOS");
                });
    }

    private void marcarChipPorTexto(ChipGroup grupo, String texto) {
        if (texto == null) return;
        for(int i = 0; i < grupo.getChildCount();i++){
            Chip chip = (Chip) grupo.getChildAt(i);
            if (chip.getText().toString().equalsIgnoreCase(texto)) {
                chip.setChecked(true);
                return;
            }
        }
    }
}

