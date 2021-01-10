package com.example.psyquea.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.psyquea.AgendarActivity;
import com.example.psyquea.CancelarActivity;
import com.example.psyquea.ModificarActivity;
import com.example.psyquea.MostrarActivity;
import com.example.psyquea.R;
import com.google.firebase.auth.FirebaseAuth;

public class citasFragment extends Fragment {

    private Button btn_agendar, btn_mostrar, btn_modificar, btn_cancelar;


    public citasFragment() {
        // Required empty public constructor
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_citas, container, false);


        btn_agendar = view.findViewById(R.id.agenda);
        btn_mostrar = view.findViewById(R.id.mostrar);
        btn_modificar = view.findViewById(R.id.modificar);
        btn_cancelar = view.findViewById(R.id.cancelar);

        btn_agendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent agendar = new Intent(getContext(), AgendarActivity.class);
                agendar.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(agendar);
            }
        });

        btn_mostrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mostrar = new Intent(getContext(), MostrarActivity.class);
                mostrar.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(mostrar);
            }
        });

        btn_modificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent modificar = new Intent(getContext(), ModificarActivity.class);
                modificar.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(modificar);
            }
        });

        btn_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cancelar = new Intent(getContext(), CancelarActivity.class);
                cancelar.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(cancelar);
            }
        });

        return view;
    }

}