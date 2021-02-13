package com.example.psyquea;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ModificarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar);
    }

    //Metodo para el boton de regresar
    public void ModificarRegresar(View view){
        Intent modificarReg = new Intent(this, MainActivity.class);
        startActivity(modificarReg);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(ModificarActivity.this, MainActivity.class));
        finish();
    }
}