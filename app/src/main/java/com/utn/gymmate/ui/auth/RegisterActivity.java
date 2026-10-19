package com.utn.gymmate.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.utn.gymmate.R;
import com.utn.gymmate.models.Usuario;
import com.utn.gymmate.ui.PerfilFisicoActivity;

public class RegisterActivity extends AppCompatActivity {

    private TextInputLayout tilEmail, tilPassword, tilConfirmPassword;
    private TextInputEditText etEmail, etPassword, etConfirmPassword;
    private Button btnRegistrar;
    private TextView tvLinkLogin;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        auth = FirebaseAuth.getInstance();

        tilEmail = findViewById(R.id.tilEmail);
        tilPassword = findViewById(R.id.tilPassword);
        tilConfirmPassword = findViewById(R.id.tilConfirmPassword);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnRegistrar = findViewById(R.id.btnLogin);
        tvLinkLogin = findViewById(R.id.tvLinkLogin);

        btnRegistrar.setOnClickListener(v -> registrar());
        tvLinkLogin.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }

    private void registrar() {
        String email = texto(etEmail);
        String password = texto(etPassword);
        String confirmacion = texto(etConfirmPassword);

        if (!datosValidos(email, password, confirmacion)) return;

        btnRegistrar.setEnabled(false);

        auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(resultado -> {
                    Toast.makeText(this, R.string.registro_ok, Toast.LENGTH_SHORT).show();
                    crearDocumentoUsuario(resultado.getUser(), email);
                })
                .addOnFailureListener(error -> {
                    btnRegistrar.setEnabled(true);
                    if (error instanceof FirebaseAuthUserCollisionException) {
                        tilEmail.setError(getString(R.string.error_correo_registrado));
                    } else if (error instanceof FirebaseAuthWeakPasswordException) {
                        tilPassword.setError(getString(R.string.error_password_corta));
                    } else {
                        Toast.makeText(this, R.string.error_envio_correo, Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void crearDocumentoUsuario(FirebaseUser usuario, String email) {
        if (usuario == null) {
            irAlPerfil();
            return;
        }
        FirebaseFirestore.getInstance()
                .collection("usuarios")
                .document(usuario.getUid())
                .set(new Usuario(usuario.getUid(), email), SetOptions.merge())
                .addOnCompleteListener(t -> irAlPerfil());
    }

    private void irAlPerfil() {
        Intent intent = new Intent(this, PerfilFisicoActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private boolean datosValidos(String email, String password, String confirmacion) {
        tilEmail.setError(null);
        tilPassword.setError(null);
        tilConfirmPassword.setError(null);

        if (email.isEmpty() || password.isEmpty() || confirmacion.isEmpty()) {
            Toast.makeText(this, R.string.error_completar_campos, Toast.LENGTH_LONG).show();
            if (email.isEmpty()) tilEmail.setError(getString(R.string.error_completar_campos));
            if (password.isEmpty()) tilPassword.setError(getString(R.string.error_completar_campos));
            if (confirmacion.isEmpty()) tilConfirmPassword.setError(getString(R.string.error_completar_campos));
            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            tilEmail.setError(getString(R.string.error_email_invalido));
            return false;
        }

        if (password.length() < 6) {
            tilPassword.setError(getString(R.string.error_password_corta));
            return false;
        }

        if (!password.equals(confirmacion)) {
            tilConfirmPassword.setError(getString(R.string.error_passwords_no_coinciden));
            return false;
        }

        return true;
    }

    private String texto(TextInputEditText campo) {
        return campo.getText() == null ? "" : campo.getText().toString().trim();
    }
}
