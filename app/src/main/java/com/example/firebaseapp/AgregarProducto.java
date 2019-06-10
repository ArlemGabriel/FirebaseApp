package com.example.firebaseapp;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.firebaseapp.Objetos.ReferenciasFirebase;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class AgregarProducto extends AppCompatActivity {
    Button btnAgregar;
    TextView txtNombre, txtPrecio,txtDescripcion,txtUrl;

    //Atributos para subir imagen/////////////////////////////////
    private static final int PICK_IMAGE_REQUEST = 1;
    private Button btnEscogerImg,btnCargarImg;
    //private TextView txtViewMostrarImagenes; //es el btnAgregar
    private ImageView imvImagen;
    private EditText txtNombreImagen;
    private ProgressBar prbSubidaImg;
    private Uri uriImagen;
    private StorageReference refStorage;
    private DatabaseReference refDatabase;

    //////////////////////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_producto);
        //Inicializacion de variables para subir imagen
        btnEscogerImg = findViewById(R.id.btnChoose);
        btnCargarImg = findViewById(R.id.btnSubir);
        imvImagen = findViewById(R.id.imageView);
        prbSubidaImg = findViewById(R.id.progress_bar);

        refStorage = FirebaseStorage.getInstance().getReference(ReferenciasFirebase.REFERENCIA_STORAGEFIREBASE);
        refDatabase = FirebaseDatabase.getInstance().getReference(ReferenciasFirebase.REFERENCIA_BASEDATOSFIREBASE);
        btnEscogerImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirBusquedaArchivo();
            }
        });
        btnCargarImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cargarImagen();
            }
        });


        txtNombre = findViewById(R.id.txtNombre);
        txtPrecio = findViewById(R.id.txtPrecio);
        txtDescripcion = findViewById(R.id.txtDescripcion);

    }
    private void abrirBusquedaArchivo(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data !=null && data.getData() != null){
            uriImagen = data.getData();
            Picasso.with(this).load(uriImagen).into(imvImagen);

        }
    }
    private String obtenerExtensionArchivo(Uri uri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }
    private void cargarImagen(){
        final String datosNombre = txtNombre.getText().toString();
        final String datosPrecio = txtPrecio.getText().toString();
        final String datosDescripcion = txtDescripcion.getText().toString();
        if(datosNombre.isEmpty() || datosPrecio.isEmpty() || datosDescripcion.isEmpty()) {
            Toast.makeText(AgregarProducto.this, "Ingrese todos los datos", Toast.LENGTH_LONG).show();
        }else{
            if (uriImagen != null){
                final StorageReference refArchivo = refStorage.child(System.currentTimeMillis()+
                        "."+obtenerExtensionArchivo(uriImagen));
                refArchivo.putFile(uriImagen)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        prbSubidaImg.setProgress(0);
                                    }
                                },500);
                                Toast.makeText(AgregarProducto.this,"Carga Exitosa",Toast.LENGTH_LONG).show();
                                refArchivo.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        Uri url= uri;
                                        String datosUrl = url.toString();

                                        Productos prd = new Productos(datosNombre,datosPrecio,datosDescripcion,datosUrl);
                                        refDatabase.push().setValue(prd);

                                        Intent intent = new Intent(AgregarProducto.this,MainActivity.class);
                                        startActivity(intent);

                                    }
                                });
                            /*Cargar cargar = new Cargar(txtNombreImagen.getText().toString().trim(),
                                    refArchivo.getDownloadUrl().toString());
                            //Nombre del url
                            String cargarID = refDatabase.push().getKey();
                            refDatabase.child(cargarID).setValue(cargar);*/
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(AgregarProducto.this,e.getMessage(),Toast.LENGTH_LONG).show();
                            }
                        })
                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                double progreso = (100.00*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                                prbSubidaImg.setProgress((int) progreso);
                            }
                        });
            }else{
                Toast.makeText(this,"Seleccione una imagen",Toast.LENGTH_LONG).show();
            }
        }
    }
}
