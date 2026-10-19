package com.utn.gymmate.ui.auth;

import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.utn.gymmate.R;

public class ForgotPasswordActivity extends AppCompatActivity {

    private TextInputLayout tilEmail;
    private TextInputEditText etEmail;
    private Button btnSendLink;
    private TextView tvLinkLogin;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        auth = FirebaseAuth.getInstance();

        tilEmail = findViewById(R.id.tilEmail);
        etEmail = findViewById(R.id.etEmail);
        btnSendLink = findViewById(R.id.btnSendLink);
        tvLinkLogin = findViewById(R.id.tvLinkLogin);

        btnSendLink.setOnClickListener(v -> enviarEnlace());
        tvLinkLogin.setOnClickListener(v -> finish());
    }

    private void enviarEnlace() {
        tilEmail.setError(null);

        String email = etEmail.getText() == null ? "" : etEmail.getText().toString().trim();

        if (email.isEmpty()) {
            tilEmail.setError(getString(R.string.error_campos_obligatorios));
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            tilEmail.setError(getString(R.string.error_email_invalido));
            return;
        }

        btnSendLink.setEnabled(false);

        auth.sendPasswordResetEmail(email)
                .addOnSuccessListener(x -> {
                    Toast.makeText(this, R.string.recuperacion_enviada, Toast.LENGTH_LONG).show();
                    finish();
                })
                .addOnFailureListener(error -> {
                    btnSendLink.setEnabled(true);
                    if (error instanceof FirebaseAuthInvalidUserException) {
                        tilEmail.setError(getString(R.string.error_email_no_registrado));
                    } else {
                        Toast.makeText(this, R.string.error_envio_correo, Toast.LENGTH_LONG).show();
                    }
                });
    }
}
