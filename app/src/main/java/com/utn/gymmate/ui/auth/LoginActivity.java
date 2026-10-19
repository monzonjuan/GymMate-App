package com.utn.gymmate.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.utn.gymmate.R;
import com.utn.gymmate.ui.HomeActivity;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout tilEmail, tilPassword;
    private TextInputEditText etEmail, etPassword;
    private Button btnLogin;
    private TextView tvLinkRegistro, tvLinkForgot;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();

        tilEmail = findViewById(R.id.tilEmail);
        tilPassword = findViewById(R.id.tilPassword);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvLinkRegistro = findViewById(R.id.tvLinkLogin);
        tvLinkForgot = findViewById(R.id.tvLinkForgot);

        btnLogin.setOnClickListener(v -> iniciarSesion());

        tvLinkRegistro.setOnClickListener(v -> {
            startActivity(new Intent(this, RegisterActivity.class));
            finish();
        });

        tvLinkForgot.setOnClickListener(v ->
                startActivity(new Intent(this, ForgotPasswordActivity.class)));
    }

    private void iniciarSesion() {
        tilEmail.setError(null);
        tilPassword.setError(null);

        String email = texto(etEmail);
        String password = texto(etPassword);

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, R.string.error_campos_obligatorios, Toast.LENGTH_LONG).show();
            if (email.isEmpty()) tilEmail.setError(getString(R.string.error_campos_obligatorios));
            if (password.isEmpty()) tilPassword.setError(getString(R.string.error_campos_obligatorios));
            return;
        }

        btnLogin.setEnabled(false);

        auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(resultado -> {
                    Intent intent = new Intent(this, HomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(error -> {
                    btnLogin.setEnabled(true);
                    Toast.makeText(this, R.string.error_credenciales, Toast.LENGTH_LONG).show();
                });
    }

    private String texto(TextInputEditText campo) {
        return campo.getText() == null ? "" : campo.getText().toString().trim();
    }
}
