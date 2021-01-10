package com.example.psyquea;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class AgendarActivity extends AppCompatActivity implements View.OnClickListener{

    Button bfecha, bhora;
    EditText efecha, ehora;
    private int dia, mes, ano, hora, minutos;

    private EditText etf, eth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agendar);

        //fecha y horarios
        bfecha=(Button)findViewById(R.id.bfecha);
        bhora=(Button)findViewById(R.id.bhora);
        efecha=(EditText)findViewById(R.id.efecha);
        ehora=(EditText)findViewById(R.id.ehora);
        bfecha.setOnClickListener(this);
        bhora.setOnClickListener(this);

        //validar fecha y hora
        etf=(EditText)findViewById(R.id.efecha);
        eth=(EditText)findViewById(R.id.ehora);

    }

    //Metodo para el boton de regresar
    public void AgendarRegresar(View view){
        Intent agendarReg = new Intent(this, InicioActivity.class);
        startActivity(agendarReg);
    }

    //Metodo para los botones de fecha y hora
    @Override
    public void onClick(View v) {
        if(v==bfecha) {
            final Calendar c= Calendar.getInstance();
            dia=c.get(Calendar.DAY_OF_MONTH);
            mes=c.get(Calendar.MONTH);
            ano=c.get(Calendar.YEAR);

            DatePickerDialog datePickerDialog =  new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    efecha.setText(dayOfMonth+"/"+(monthOfYear+1)+"/"+year);
                }
            }
                    ,dia, mes, ano);
            datePickerDialog.show();
        }

        if(v==bhora){
            final Calendar c=Calendar.getInstance();
            hora=c.get(Calendar.HOUR_OF_DAY);
            minutos=c.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    ehora.setText(hourOfDay+":"+minute);
                }
            }, hora, minutos, false);
            timePickerDialog.show();
        }
    }

    //Metodo para verificar que los campos estan llenos
    public void AgendarV(View view){
        String fecha= etf.getText().toString();
        String hora=eth.getText().toString();

        if(fecha.length() == 0)
        {
            Toast.makeText(this, "Debes elegir una fecha", Toast.LENGTH_LONG).show();
        }
        if(hora.length() == 0)
        {
            Toast.makeText(this, "Debes elegir una hora", Toast.LENGTH_LONG).show();
        }
        if(fecha.length() != 0)
        {
            Toast.makeText(this, "Verificando disponibilidad de la cita...", Toast.LENGTH_LONG).show();
        }
    }
}