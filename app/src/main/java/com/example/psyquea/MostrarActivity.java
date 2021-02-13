package com.example.psyquea;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MostrarActivity extends AppCompatActivity {

    /*
    private Button agendar;
    private ListView list;
    private EditText edit;
    private List<String> mLista = new ArrayList<>();
    private ArrayAdapter<String> mAdapter;
*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar);

     /*   agendar=findViewById(R.id.agendar);
        agendar.setOnClickListener(this);
        list=findViewById(R.id.listView);
        list.setOnClickListener(this);
        edit=findViewById(R.id.efecha);
        edit.setOnClickListener(this);*/
    }
    /*
        @Override
        public void onClick(View view){
            switch (view.getId()){
                case R.id.agendar: String texto = edit.getText().toString().trim();

            }
        }
    */
    //Metodo para el boton de regresar
    public void MostrarRegresar(View view){
        Intent mostrarReg = new Intent(this, MainActivity.class);
        startActivity(mostrarReg);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(MostrarActivity.this, MainActivity.class));
        finish();
    }
}