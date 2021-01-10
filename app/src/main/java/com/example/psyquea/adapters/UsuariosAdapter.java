package com.example.psyquea.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.psyquea.InicioActivity;
import com.example.psyquea.MainActivity;
import com.example.psyquea.MensajesActivity;
import com.example.psyquea.R;
import com.example.psyquea.informacion.Solicitudes;
import com.example.psyquea.informacion.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

//Lista de usuarios en el fragmento Usuarios

public class UsuariosAdapter extends RecyclerView.Adapter<UsuariosAdapter.viewHolderAdapter> {

    List<Users> usersList;
    Context context;
    String tipoUsuario;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    DatabaseReference ref_user_tipo = database.getReference("Users").child(user.getUid()).child("tipo");

    SharedPreferences mPref;

    public UsuariosAdapter(List<Users> usersList, Context context){
        this.usersList = usersList;
        this.context = context;


    }

    public UsuariosAdapter(List<Users> usersList, Context context, String tipoUsuario) {
        this.usersList = usersList;
        this.context = context;
        this.tipoUsuario = tipoUsuario;
    }

    @NonNull
    @Override
    public viewHolderAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_usuarios, parent, false);
        viewHolderAdapter holder = new viewHolderAdapter(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolderAdapter holder, int position) {

        Users users = usersList.get(position);

        DatabaseReference ref_solicitudes = database.getReference("Solicitudes").child(user.getUid());

        final Vibrator vibrator = (Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);

        Glide.with(context).load(users.getFoto()).into(holder.img_user);
        holder.tv_usuario.setText(users.getNombre());

        if(users.getId().equals(user.getUid())){
            holder.cardView.setVisibility(View.GONE);
        }else{
            holder.cardView.setVisibility(View.VISIBLE);
        }


        ref_solicitudes.child(users.getId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String estado = "";

                if(snapshot.exists()){
                    estado = snapshot.child("estado").getValue(String.class);

                    if( (estado.equals("solicitud") || estado.equals("amigos")) && tipoUsuario.equals("psicologo")){
                        holder.cardView.setVisibility(View.VISIBLE);
                        configureButtons(users, estado, holder);
                    }else if(tipoUsuario.equals("paciente") && users.getTipo().equals("psicologo")){
                        holder.cardView.setVisibility(View.VISIBLE);
                        configureButtons(users, estado, holder);
                    }else{
                        holder.cardView.setVisibility(View.GONE);
                    }

                }else{
                    if(tipoUsuario.equals("paciente") && !users.getTipo().equals("paciente")){
                        holder.cardView.setVisibility(View.VISIBLE);
                        configureButtons(users, estado, holder);
                    }else{
                        holder.cardView.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatabaseReference A = database.getReference("Solicitudes").child(user.getUid());
                A.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        Solicitudes solicitudes = new Solicitudes("enviado", "");

                        A.child(users.getId()).setValue(solicitudes);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                final DatabaseReference B = database.getReference("Solicitudes").child(users.getId());
                B.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        Solicitudes solicitudes = new Solicitudes("solicitud", "");

                        B.child(user.getUid()).setValue(solicitudes);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                final DatabaseReference count = database.getReference("Contador").child(users.getId());
                count.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            Integer val = snapshot.getValue(Integer.class);
                            if(val == 0){
                                count.setValue(1);
                            }else{
                                count.setValue(val + 1);
                            }
                        }else{
                            count.setValue(1);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                vibrator.vibrate(300);
            }
        });

        holder.Tsolicitud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String idchat = ref_solicitudes.push().getKey();

                final DatabaseReference A = database.getReference("Solicitudes").child(users.getId()).child(user.getUid());
                A.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        Solicitudes solicitudes = new Solicitudes("amigos", idchat);
                        A.setValue(solicitudes);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                final DatabaseReference B = database.getReference("Solicitudes").child(user.getUid()).child(users.getId());
                B.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        Solicitudes solicitudes = new Solicitudes("amigos", idchat);
                        B.setValue(solicitudes);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                vibrator.vibrate(300);
            }
        });

        holder.amigos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mPref = v.getContext().getSharedPreferences("usuario_sp", Context.MODE_PRIVATE);
                final SharedPreferences.Editor editor = mPref.edit();

                final DatabaseReference ref = database.getReference("Solicitudes").child(user.getUid()).child(users.getId()).child("idchat");
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        String id_unico = snapshot.getValue(String.class);
                        if(snapshot.exists()){
                            Intent intent = new Intent(v.getContext(), MensajesActivity.class);
                            intent.putExtra("nombre", users.getNombre());
                            intent.putExtra("img_user", users.getFoto());
                            intent.putExtra("id_user", users.getId());
                            intent.putExtra("id_unico", id_unico);

                            editor.putString("usuario_sp", users.getId());
                            editor.apply();

                            v.getContext().startActivity(intent);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                //Toast.makeText(context, "Inserte Eliminar", Toast.LENGTH_SHORT).show();
            }
        });

        holder.eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                builder.setMessage("¿Seguro que quiere eliminar a esta persona o solicitud?")
                        .setCancelable(false)
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                //Referencias a usuario A (Usuario Actual) y usuario B
                                final DatabaseReference A = database.getReference("Solicitudes").child(users.getId()).child(user.getUid());
                                final DatabaseReference B = database.getReference("Solicitudes").child(user.getUid()).child(users.getId());

                                //Referencia al contador
                                final DatabaseReference C = database.getReference("Contador").child(users.getId());


                                //Referencia al ID de chat
                                final DatabaseReference chat = database.getReference("Solicitudes").child(user.getUid())
                                        .child(users.getId()).child("idchat");
                                //chat.removeValue();



                                chat.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                                        if(snapshot.exists()){
                                            String id_chat = snapshot.getValue(String.class);
                                            if(!id_chat.equals("")){
                                                DatabaseReference chatAB = database.getReference("Chats").child(id_chat);
                                                chatAB.removeValue();
                                            }
                                        }

                                        A.removeValue();
                                        B.removeValue();

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                                C.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                                        if(snapshot.exists()){
                                            Integer val = snapshot.getValue(Integer.class);
                                            if(val == 0){
                                                C.setValue(0);
                                            }else{
                                                C.setValue(val - 1);
                                            }
                                        }else{
                                            C.setValue(0);
                                        }

                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                                holder.cardView.setVisibility(View.GONE);
                            }

                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });


    }

    private void configureButtons(Users users, String estado, viewHolderAdapter holder) {

        switch (estado){
            case "enviado":
                holder.send.setVisibility(View.VISIBLE);
                holder.add.setVisibility(View.GONE);
                holder.amigos.setVisibility(View.GONE);
                holder.Tsolicitud.setVisibility(View.GONE);
                holder.eliminar.setVisibility(View.VISIBLE);
                holder.progressBar.setVisibility(View.GONE);
                break;
            case "solicitud":
                holder.send.setVisibility(View.GONE);
                holder.add.setVisibility(View.GONE);
                holder.amigos.setVisibility(View.GONE);
                holder.Tsolicitud.setVisibility(View.VISIBLE);
                holder.eliminar.setVisibility(View.VISIBLE);
                holder.progressBar.setVisibility(View.GONE);
                break;
            case "amigos":
                holder.send.setVisibility(View.GONE);
                holder.add.setVisibility(View.GONE);
                holder.amigos.setVisibility(View.VISIBLE);
                holder.Tsolicitud.setVisibility(View.GONE);
                holder.eliminar.setVisibility(View.VISIBLE);
                holder.progressBar.setVisibility(View.GONE);
                break;
            default:
                holder.send.setVisibility(View.GONE);
                holder.add.setVisibility(View.VISIBLE);
                holder.amigos.setVisibility(View.GONE);
                holder.Tsolicitud.setVisibility(View.GONE);
                holder.eliminar.setVisibility(View.GONE);
                holder.progressBar.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public int getItemCount() {

        if (usersList != null)
            return usersList.size();
        else
            return 0;
    }

    public class viewHolderAdapter extends RecyclerView.ViewHolder {

        TextView tv_usuario;
        ImageView img_user;
        CardView cardView;
        Button add, send, amigos, Tsolicitud;
        ImageButton eliminar;
        ProgressBar progressBar;

        public viewHolderAdapter(@NonNull View itemView) {

            super(itemView);

            tv_usuario = itemView.findViewById(R.id.tv_user);
            img_user = itemView.findViewById(R.id.img_user);
            cardView = itemView.findViewById(R.id.cardview);
            add = itemView.findViewById(R.id.btn_add);
            send = itemView.findViewById(R.id.btn_send);
            amigos = itemView.findViewById(R.id.btn_amigos);
            Tsolicitud = itemView.findViewById(R.id.btn_Tsolicitud);
            eliminar = itemView.findViewById(R.id.btn_eliminar);
            progressBar = itemView.findViewById(R.id.progresBar);
        }
    }
}









