package com.example.psyquea.fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.psyquea.InicioActivity;
import com.example.psyquea.MainActivity;
import com.example.psyquea.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class cuentaFragment extends Fragment {

    ImageView img_foto;
    TextView tv_usuario;
    Button btn_cerrar_sesion;

    //public AlertDialog.Builder dialogBuilder;



    public cuentaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_cuenta, container, false);

        img_foto = view.findViewById(R.id.img_user);
        tv_usuario = view.findViewById(R.id.tv_nombre);
        btn_cerrar_sesion = view.findViewById(R.id.btn_cerrar_sesion);

        btn_cerrar_sesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                volverLogin();
                Toast.makeText(getContext(), "Cerrando sesion...", Toast.LENGTH_SHORT).show();
            }
        });

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        Glide.with(this).load(user.getPhotoUrl()).into(img_foto);
        tv_usuario.setText(user.getDisplayName());
        // Inflate the layout for this fragment
        return view;
    }

    private void volverLogin() {
        Intent login = new Intent(getContext(), MainActivity.class);
        login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(login);
    }
}