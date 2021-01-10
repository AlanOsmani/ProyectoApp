package com.example.psyquea;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.psyquea.adapters.PaginasAdapter;
import com.example.psyquea.adapters.PaginasPsicologoAdapter;
import com.example.psyquea.informacion.Estado;
import com.example.psyquea.informacion.Users;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class InicioActivity extends AppCompatActivity {

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    FirebaseDatabase database = FirebaseDatabase.getInstance();

    String tipo = "";

    private Toolbar toolbar;
    private LinearLayout linearLayout;
    private ProgressBar progressBar;

    DatabaseReference ref_user = database.getReference("Users").child(user.getUid());
    DatabaseReference ref_user_tipo = database.getReference("Users").child(user.getUid()).child("tipo");
    DatabaseReference ref_solicitud = database.getReference("Contador").child(user.getUid());
    DatabaseReference ref_estado = database.getReference("Estado").child(user.getUid());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        //Inicializacion de elementos de la actividad
        TabLayout tabLayout = findViewById(R.id.tabLayout);

        toolbar = findViewById(R.id.toolbar);
        linearLayout = findViewById(R.id.linearlayoutPB);
        progressBar = findViewById(R.id.progresBar);

        ViewPager2 viewPager2 = findViewById(R.id.viewPager);

        //Menu de toolbar
        toolbar.inflateMenu(R.menu.menu_item);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.item_cerrar:
                        FirebaseAuth.getInstance().signOut();
                        volverLogin();
                        Toast.makeText(InicioActivity.this, "Cerrando sesion...", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });

        //Hace visible la progressBar en lo que se cargan los fragmentos y el tablayout
        linearLayout.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        //Crea el tablayout segun el tipo de usuario
        ref_user.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tipo = snapshot.child("tipo").getValue(String.class);
                if(tipo.equals("psicologo")){
                    viewPager2.setAdapter(new PaginasAdapter(InicioActivity.this));
                }else{
                    viewPager2.setAdapter(new PaginasPsicologoAdapter(InicioActivity.this));
                }

                crearTab(tipo, viewPager2, tabLayout);
                //Toast.makeText(InicioActivity.this, tipo, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //String aaa = ref_user_tipo.;

        //Toast.makeText(InicioActivity.this, aaa, Toast.LENGTH_SHORT).show();

        //GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(this);
        //uniqueUser();

        //estadoUser();

    }

    private void crearTab(String tipo, ViewPager2 viewPager2, TabLayout tabLayout) {
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                //Barra de opciones y sus iconos (Usuarios, chat, etc.)
                linearLayout.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);

                switch (position){
                    case 0:{
                        //tab.setText("Cuenta");
                        tab.setIcon(R.drawable.ic_cuenta);
                        break;
                    }
                    case 1:{
                        //tab.setText("Pacientes");
                        tab.setIcon(R.drawable.ic_usuarios);
                        final BadgeDrawable badgeDrawable = tab.getOrCreateBadge();
                        badgeDrawable.setBackgroundColor(
                                ContextCompat.getColor(getApplicationContext(), R.color.colorAccent)
                        );
                        //badgeDrawable.setVisible(true);
                        //badgeDrawable.setNumber(1);

                        ref_solicitud.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                if(snapshot.exists()){
                                    Integer val = snapshot.getValue(Integer.class);
                                    badgeDrawable.setVisible(true);
                                    if(val.equals("0")){
                                        badgeDrawable.setVisible(false);

                                    }else {
                                        badgeDrawable.setNumber(val);
                                    }
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        break;
                    }
                    case 2:{
                        //tab.setText("Chat");
                        tab.setIcon(R.drawable.ic_chats);
                        BadgeDrawable badgeDrawable = tab.getOrCreateBadge();
                        badgeDrawable.setBackgroundColor(
                                ContextCompat.getColor(getApplicationContext(), R.color.colorAccent)
                        );
                        badgeDrawable.setVisible(true);
                        badgeDrawable.setNumber(1);
                        break;
                    }
                    case 3:{
                        // tab.setText("Citas");
                        tab.setIcon(R.drawable.ic_citas);
                        break;
                    }
                    case 4:{
                        tab.setIcon(R.drawable.ic_expediente);
                        break;
                    }
                    /*case 5:{
                        //tab.setText("Solicitudes");
                        tab.setIcon(R.drawable.ic_solicitudes);

                        break;

                    }*/
                }
            }
        });

        tabLayoutMediator.attach();

        //Borra el icono de notificaciones de la seccion de la pagina
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                BadgeDrawable badgeDrawable = tabLayout.getTabAt(position).getOrCreateBadge();
                badgeDrawable.setVisible(false);

                if (position == 2) {
                    countacero();
                }
            }
        });
    }

    private void estadoUser(final String estado) {
        ref_estado.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Estado est = new Estado(estado, "", "", "");
                ref_estado.setValue(est);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        estadoUser("conectado");
    }

    @Override
    protected void onPause() {
        super.onPause();
        estadoUser("desconectado");
        ultimaFecha();
    }

    private void ultimaFecha() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        ref_estado.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ref_estado.child("fecha").setValue(dateFormat.format(c.getTime()));
                ref_estado.child("hora").setValue(timeFormat.format(c.getTime()));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void countacero() {
        ref_solicitud.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    ref_solicitud.setValue(0);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    //Crea un usuario unico en la base de datos de Firebase
   /* private void uniqueUser() {
        ref_user.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists()){
                    Users usuario = new Users(
                            user.getUid(),
                            user.getDisplayName(),
                            user.getEmail(),
                            user.getPhotoUrl().toString(),
                            "desconectado",
                            "28/12/2020",
                            "12:38",
                            0,
                            0
                    );

                    ref_user.setValue(usuario);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }*/

    //Funcion para volver al login de la App
    private void volverLogin() {
        Intent login = new Intent(this, MainActivity.class);
        login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(login);
    }
}