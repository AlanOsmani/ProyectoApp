package com.example.psyquea;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class RegistroActivity<DatabaseReference> extends AppCompatActivity {

    ImageView ImgUserPhoto;
    private ToggleButton tgbtn_tipo;
    private TextView tv_tipo_user;
    private ProgressBar progressBar;


    static int PReqCode = 1;
    static int REQUESTCODE = 1;
    Uri pickedImageUri;

    //Originales
    private EditText mEditTextName;
    private EditText mEditTextLastName;
    private EditText mEditTextEmail;
    private EditText mEditTextTel;
    private EditText mEditTextCedula;
    private EditText mEditTextPass;
    private EditText mEditTextPass2;
    private Button mButtonRegister;
    //Variables de los datos que se van a registrar
    private String name = "";
    private String lastName = "";
    private String Email = "";
    private String Tel = "";
    private String pass = "";
    private String pass2 = "";
    private String tipo = "";
    private String foto = "";
    private String cedula = "";
    private String estado = "";
    private String fecha = "";
    private String hora = "";
    private int solicitudes = 0;
    private int nuevoMensaje = 0;

    FirebaseAuth mAuth;
    com.google.firebase.database.DatabaseReference mDataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        mAuth = FirebaseAuth.getInstance();
        mDataBase = FirebaseDatabase.getInstance().getReference();

        ImgUserPhoto = findViewById(R.id.img_user);
        tgbtn_tipo = findViewById(R.id.tgbtn_tipo);
        tv_tipo_user = findViewById(R.id.tv_tipo_user);

        mEditTextName = (EditText) findViewById(R.id.nombreRegis);
        mEditTextLastName = (EditText) findViewById(R.id.apellidoRegis);
        mEditTextEmail = (EditText) findViewById(R.id.emailRegis);
        mEditTextTel = (EditText) findViewById(R.id.telRegis);
        mEditTextCedula = (EditText) findViewById(R.id.cedulaRegis);
        mEditTextPass = (EditText) findViewById(R.id.passRegis);
        mEditTextPass2 = (EditText) findViewById(R.id.passRegis2);
        mButtonRegister = (Button) findViewById(R.id.btnConfirmRegis);

        progressBar = (ProgressBar) findViewById(R.id.progresBar);

        Calendar c = Calendar.getInstance();
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");



        tipo = "paciente";
        estado = "desconectado";
        fecha = dateFormat.format(c.getTime());
        hora = timeFormat.format(c.getTime());
        solicitudes = 0;
        nuevoMensaje = 0;

        ImgUserPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT >= 22){
                    checkAndRequestForPermission();
                }else{
                    openGallery();
                }
            }
        });

        tgbtn_tipo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    tv_tipo_user.setText("Psicologo");
                    tipo = "psicologo";
                    mEditTextCedula.setVisibility(View.VISIBLE);
                }else{
                    tv_tipo_user.setText("Cliente");
                    tipo = "paciente";
                    mEditTextCedula.setVisibility(View.GONE);
                }
            }
        });


        mButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = mEditTextName.getText().toString();
                lastName = mEditTextLastName.getText().toString();
                Email = mEditTextEmail.getText().toString();
                Tel = mEditTextTel.getText().toString();
                cedula = mEditTextCedula.getText().toString();
                pass = mEditTextPass.getText().toString();
                pass2 = mEditTextPass2.getText().toString();

                if(!name.isEmpty() && !lastName.isEmpty() && !Email.isEmpty() && !Tel.isEmpty() && !pass.isEmpty() && !pass2.isEmpty() && !foto.isEmpty()) {

                    if (pass.length() >= 6 ) {
                        if(pass.equals(pass2)){
                            if(tipo.equals("psicologo") && cedula.isEmpty()){
                                Toast.makeText(RegistroActivity.this, "Debe introducir su cedula profesional", Toast.LENGTH_SHORT).show();
                            }else{
                                progressBar.setVisibility(View.VISIBLE);
                                mButtonRegister.setVisibility(View.GONE);
                                registerUser();
                            }
                        }else{
                            Toast.makeText(RegistroActivity.this, "No coincide la confirmación de la contraseña", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(RegistroActivity.this, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                        Toast.makeText(RegistroActivity.this, "Debe llenar todos los campos incluyendo foto de perfil", Toast.LENGTH_SHORT).show();
                }

            }

        });
    }

    private void openGallery() {

        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, REQUESTCODE);
    }

    private void checkAndRequestForPermission() {
        if(ContextCompat.checkSelfPermission(RegistroActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){

            if(ActivityCompat.shouldShowRequestPermissionRationale(RegistroActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)){

                Toast.makeText(RegistroActivity.this, "Por favor acepta los permisos necesarios", Toast.LENGTH_SHORT).show();

            }else{
                ActivityCompat.requestPermissions(RegistroActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PReqCode);
            }

        }else{
            openGallery();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && requestCode == REQUESTCODE && data != null ){
            pickedImageUri = data.getData();
            ImgUserPhoto.setImageURI(pickedImageUri);
            foto = "seleccionada";
        }
    }

    private void registerUser(){
        mAuth.createUserWithEmailAndPassword(Email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){

                    StorageReference mStorage = FirebaseStorage.getInstance().getReference().child("users_photos");
                    StorageReference imageFilePath = mStorage.child(pickedImageUri.getLastPathSegment());
                    imageFilePath.putFile(pickedImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        //La imagen se guarda correctamente en el storage de Firebase
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                                            .setDisplayName(name + " " + lastName)
                                            .setPhotoUri(uri).build();
                                    mAuth.getCurrentUser().updateProfile(profileUpdate)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> taskA) {

                                                    if(taskA.isSuccessful()){

                                                        Toast.makeText(RegistroActivity.this, "Imagen guardada con exito", Toast.LENGTH_SHORT).show();

                                                        String id = mAuth.getCurrentUser().getUid();

                                                        if(tipo.equals("psicologo")){
                                                            registrarCedula(id, cedula);
                                                        }

                                                        Map<String, Object> map = new HashMap<>();
                                                        map.put( "id", id);
                                                        map.put( "nombre", (name + " " + lastName));
                                                        map.put( "mail",Email);
                                                        map.put( "tel",Tel);
                                                        map.put( "tipo",tipo);
                                                        //map.put( "cedula",cedula);
                                                        map.put( "pass",pass);
                                                        map.put( "foto",uri.toString());
                                                        map.put( "estado", estado);
                                                        map.put( "fecha", fecha);
                                                        map.put( "hora", hora);
                                                        map.put( "solicitudes", solicitudes);
                                                        map.put( "nuevoMensaje", nuevoMensaje);

                                                        mDataBase.child("Users").child(id).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task2) {
                                                                if (task2.isSuccessful()) {
                                                                    startActivity(new Intent(RegistroActivity.this, InicioActivity.class));
                                                                    finish();
                                                                } else {
                                                                    Toast.makeText(RegistroActivity.this, "No se pude crear el perfil", Toast.LENGTH_SHORT).show();
                                                                    progressBar.setVisibility(View.GONE);
                                                                    mButtonRegister.setVisibility(View.VISIBLE);
                                                                }
                                                            }
                                                        });
                                                    }
                                                }
                                            });

                                }
                            });
                        }
                    });



                }
                else{
                    Toast.makeText(RegistroActivity.this, "No se pudo registrar este usuario", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    mButtonRegister.setVisibility(View.VISIBLE);
                }
            }


        });

    }

    private void registrarCedula(String id, String cedula) {
        Map<String, Object> map = new HashMap<>();
        map.put( "cedula", cedula);

        mDataBase.child("Cedula").child(id).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task2) {
                if (task2.isSuccessful()) {
                    Toast.makeText(RegistroActivity.this, "La cedula se guardo correctamente", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RegistroActivity.this, "No se pudo guradar la cedula correctamente", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    mButtonRegister.setVisibility(View.VISIBLE);
                }
            }
        });
    }

}