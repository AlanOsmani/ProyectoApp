package com.example.psyquea;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity {

    private EditText emailLogin, passLogin;
    private TextView registro, login;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emailLogin = findViewById(R.id.emailLogin);
        passLogin = findViewById(R.id.passLogin);

        registro = (TextView) findViewById(R.id.btnRegistroLogin);
        login = (TextView) findViewById(R.id.btnSesionLogin);
        progressBar = (ProgressBar) findViewById(R.id.progresBar);

        mAuth = FirebaseAuth.getInstance();

        progressBar.setVisibility(View.GONE);
        login.setVisibility(View.VISIBLE);
        registro.setVisibility(View.VISIBLE);

        registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registro = new Intent(MainActivity.this, RegistroActivity.class);
                registro.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(registro);

            }


        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String mail = emailLogin.getText().toString();
                final String pass = passLogin.getText().toString();

                if(mail.isEmpty() || pass.isEmpty()){
                    progressBar.setVisibility(View.GONE);
                    login.setVisibility(View.VISIBLE);
                    registro.setVisibility(View.VISIBLE);
                    Toast.makeText(MainActivity.this, "Introduzca correo y contraseña", Toast.LENGTH_SHORT).show();
                }else{
                    progressBar.setVisibility(View.VISIBLE);
                    login.setVisibility(View.GONE);
                    registro.setVisibility(View.GONE);
                    sigIn(mail, pass);
                }
            }
        });
    }

    private void sigIn(String mail, String pass) {

        mAuth.signInWithEmailAndPassword(mail, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Intent inicio = new Intent(MainActivity.this, InicioActivity.class);
                    inicio.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(inicio);
                }else{
                    progressBar.setVisibility(View.GONE);
                    login.setVisibility(View.VISIBLE);
                    registro.setVisibility(View.VISIBLE);
                    Toast.makeText(MainActivity.this, "No se pudo registrar", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = mAuth.getCurrentUser();
        if(user != null){
            Intent inicio = new Intent(MainActivity.this, InicioActivity.class);
            inicio.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(inicio);
        }
    }
}