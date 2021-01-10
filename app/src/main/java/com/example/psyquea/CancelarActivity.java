package com.example.psyquea;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class CancelarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancelar);
    }

    //Metodo para el boton de regresar
    public void CancelarRegresar(View view){
        Intent cancelarReg = new Intent(this, MainActivity.class);
        startActivity(cancelarReg);
    }
}