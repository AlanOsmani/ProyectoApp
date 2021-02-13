package com.example.psyquea;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.psyquea.adapters.ChatsAdapter;
import com.example.psyquea.informacion.Chats;
import com.example.psyquea.informacion.Estado;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class MensajesActivity extends AppCompatActivity {
    CircleImageView img_user;
    TextView username;
    ImageView ic_conectado, ic_desconectado;
    SharedPreferences mPref;

    //Datos y referencias para la base de datos con firebase
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref_estado = database.getReference("Estado").child(user.getUid());
    DatabaseReference ref_chat = database.getReference("Chats");


    EditText et_mensaje_txt;
    ImageButton btn_enviar_msj, btn_videoChat;

    //ID CHAT GLOBAL
    String id_chat_global;
    Boolean amigo_online = false;

    //RecyclerView
    RecyclerView rv_chats;
    ChatsAdapter adapter;
    ArrayList <Chats> chatlist;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Quitamos la barra de titulo por razones esteticas
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_mensajes);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mPref = getApplicationContext().getSharedPreferences("usuario_sp", MODE_PRIVATE);

        img_user = findViewById(R.id.img_user);
        username = findViewById(R.id.tv_user);
        ic_conectado = findViewById(R.id.icon_conectado);
        ic_desconectado = findViewById(R.id.icon_desconectado);

        String usuario = getIntent().getExtras().getString("nombre");
        String foto = getIntent().getExtras().getString("img_user");
        final String id_user = getIntent().getExtras().getString("id_user");
        id_chat_global = getIntent().getExtras().getString("id_unico");


        colocarEnVisto();

        et_mensaje_txt = findViewById(R.id.et_txt_mensaje);
        btn_enviar_msj = findViewById(R.id.btn_enviar_msj);
        btn_videoChat = findViewById(R.id.btn_videoChat);

        //Redirecciona al videochat
        btn_videoChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MensajesActivity.this, CallActivity.class);
                intent.putExtra("friendUser", usuario);
                intent.putExtra("username", user.getDisplayName());
                intent.putExtra("userID", user.getUid());
                intent.putExtra("friendID", id_user);
                startActivity(intent);
            }
        });


        //Funcionalidad de boton de enviar mensaje

        btn_enviar_msj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String msj = et_mensaje_txt.getText().toString();

                if(!msj.isEmpty()){
                    final Calendar c = Calendar.getInstance();
                    final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
                    final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    String idpush = ref_chat.push().getKey();

                    if(amigo_online){
                        Chats chatmsj = new Chats(idpush, user.getUid(), id_user, msj, "si", dateFormat.format(c.getTime()), timeFormat.format(c.getTime()));
                        ref_chat.child(id_chat_global).child(idpush).setValue(chatmsj);
                        Toast.makeText(MensajesActivity.this, "Mensaje Enviado", Toast.LENGTH_SHORT).show();
                        et_mensaje_txt.setText("");
                    }else{
                        Chats chatmsj = new Chats(idpush, user.getUid(), id_user, msj, "no", dateFormat.format(c.getTime()), timeFormat.format(c.getTime()));
                        ref_chat.child(id_chat_global).child(idpush).setValue(chatmsj);
                        Toast.makeText(MensajesActivity.this, "Mensaje Enviado", Toast.LENGTH_SHORT).show();
                        et_mensaje_txt.setText("");
                    }

                }else{
                    Toast.makeText(MensajesActivity.this, "No se puede enviar mensaje vacio", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Estado de conexion del otro usuario

        final String id_user_sp = mPref.getString("usuario_sp", "");

        username.setText(usuario);
        Glide.with(this).load(foto).into(img_user);

        final DatabaseReference ref = database.getReference("Estado").child(id_user_sp).child("chatU");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String chatU = snapshot.getValue(String.class);
                if(snapshot.exists()){
                    if (chatU.equals(user.getUid())) {
                        amigo_online = true;
                        ic_conectado.setVisibility(View.VISIBLE);
                        ic_desconectado.setVisibility(View.GONE);
                    } else {
                        amigo_online = false;
                        ic_conectado.setVisibility(View.GONE);
                        ic_desconectado.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        //RV

        rv_chats = findViewById(R.id.rv);
        rv_chats.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        linearLayoutManager.setStackFromEnd(true);
        rv_chats.setLayoutManager(linearLayoutManager);

        chatlist = new ArrayList<>();
        adapter = new ChatsAdapter(chatlist, this);
        rv_chats.setAdapter(adapter);

        LeerMensajes();

    }

    private void colocarEnVisto() {
        ref_chat.child(id_chat_global).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){

                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                        Chats chats = dataSnapshot.getValue(Chats.class);
                        if (chats.getReceptor().equals(user.getUid())){
                            ref_chat.child(id_chat_global).child(chats.getId()).child("visto").setValue("si");
                        }
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void LeerMensajes() {
        ref_chat.child(id_chat_global).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    chatlist.removeAll(chatlist);
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                        Chats chat = dataSnapshot.getValue(Chats.class);
                        chatlist.add(chat);

                        setScroll();
                    }

                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setScroll() {
        rv_chats.scrollToPosition(adapter.getItemCount() - 1);
    }

    private void estadoUser(final String estado) {
        ref_estado.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                final String id_user_sp = mPref.getString("usuario_sp", "");

                Estado est = new Estado(estado, "", "", id_user_sp);
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


}