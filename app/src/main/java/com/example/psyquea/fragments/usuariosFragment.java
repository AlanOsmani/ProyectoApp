package com.example.psyquea.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.psyquea.R;
import com.example.psyquea.adapters.UsuariosAdapter;
import com.example.psyquea.informacion.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class usuariosFragment extends Fragment {

    private ProgressBar progressBar;
    private RecyclerView rv;
    private ArrayList<Users> usersArrayList;
    private UsuariosAdapter adapter;
    private LinearLayoutManager mLayaoutManager;

    public usuariosFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Creacion del usuario al que pertenece la cuenta
        View view = inflater.inflate(R.layout.fragment_usuarios, container, false);

        //TextView tv_user = view.findViewById(R.id.tv_user);
        //mageView img_user = view.findViewById(R.id.img_user);

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        progressBar = view.findViewById(R.id.progresBar);
        //tv_user.setText(user.getDisplayName());
        //Glide.with(this).load(user.getPhotoUrl()).into(img_user);

        //Creacion de los demas usuarios con el RecyclerView

        mLayaoutManager = new LinearLayoutManager(getContext());
        mLayaoutManager.setReverseLayout(true);
        mLayaoutManager.setStackFromEnd(true);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myref = database.getReference("Users");
        DatabaseReference ref_user_tipo = database.getReference("Users").child(user.getUid()).child("tipo");

        rv = view.findViewById(R.id.rv);

        rv.setLayoutManager(mLayaoutManager);

        ref_user_tipo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String tipoUsuario = snapshot.getValue(String.class);

                if(snapshot.exists()){
                    usersArrayList = new ArrayList<Users>();
                    adapter = new UsuariosAdapter(usersArrayList, getContext(), tipoUsuario);
                    rv.setAdapter(adapter);

                    actualizarLista(usersArrayList, adapter, myref, tipoUsuario);


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }

    private void actualizarLista(ArrayList<Users> usersArrayList, UsuariosAdapter adapter, DatabaseReference myref, String tipoUsuario) {
        myref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){
                    rv.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    usersArrayList.removeAll(usersArrayList);
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        Users user = dataSnapshot.getValue(Users.class);
                        if(!user.getTipo().equals(tipoUsuario)){
                            usersArrayList.add(user);
                        }
                    }
                    adapter.notifyDataSetChanged();
                }else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "No existen usuarios", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}