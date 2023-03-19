package com.example.proyecto;

import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class NuevaPubli extends AppCompatActivity {

    private SQLiteDatabase db;
    private String usuario;
    private int imagen;

    private CharSequence texto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            //Se recoge el usuario que va a realizar la nueva publicación
            usuario = extras.getString("usuario");
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nuevapubli);

        RecyclerView lista= findViewById(R.id.rEscogeI);
        //Se guardan las posibles imágenes que se pueden subir, que están en la carpeta drawable
        int[] imagenes = {R.drawable.emoji1, R.drawable.emoji2, R.drawable.emoji3, R.drawable.emoji4};
        AdaptadorRecyclerPubli adaptador = new AdaptadorRecyclerPubli(imagenes);
        lista.setAdapter(adaptador);
        LinearLayoutManager layoutLineal= new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        lista.setLayoutManager(layoutLineal);
        EditText textoPubli = (EditText) findViewById(R.id.eEscribeT);
        Button bNuevaPubli = (Button) findViewById(R.id.bPublicar);
        bNuevaPubli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagen = adaptador.getImgSeleccionada();
                texto = textoPubli.getText().toString();
                nuevaPublicacion(imagen, imagenes, textoPubli.getText().toString());
            }
        });
    }

    private void nuevaPublicacion(int i, int[] imagenes, String textoPubli) {
        BaseDeDatos baseDatos = new BaseDeDatos(this);
        db = baseDatos.getWritableDatabase();
        //Se inserta en la base de datos la nueva publicación, con la imagen escogida, el usuario que la hace y el texto escrito
        ContentValues nuevaPubli = new ContentValues();
        nuevaPubli.put("imagen", imagenes[i]);
        nuevaPubli.put("usuario", usuario);
        nuevaPubli.put("texto", textoPubli);
        db.insert("publicaciones", null, nuevaPubli);

        //Se pide el permiso necesario para realizar una notificación
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new
                    String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 11);
        }

        //Se crean los elementos necesarios para enviar la notificación
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "IdCanal");
        NotificationChannel canal = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            canal = new NotificationChannel("IdCanal", "NombreCanal",
                    NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(canal);
        }

        builder.setSmallIcon(android.R.drawable.stat_sys_warning)
                .setContentTitle("Publicación")
                .setContentText("Has realizado una nueva publicación.")
                .setSubText("Información extra")
                .setVibrate(new long[]{0, 1000, 500, 1000});
        canal.setDescription("Descripción del canal");
        canal.enableLights(true);
        canal.setLightColor(Color.RED);
        canal.enableVibration(true);
        canal.setVibrationPattern(new long[]{0, 1000, 500, 1000});
        //Cuando se realiza la nueva publicación llega una notificación
        manager.notify(1, builder.build());

        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }

    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt("imagen",imagen);
        EditText t = (EditText) findViewById(R.id.eEscribeT);
        savedInstanceState.putString("texto", t.getText().toString());
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        imagen = savedInstanceState.getInt("imagen");
        texto = savedInstanceState.getString("texto");
    }

    protected void onStart() {
        super.onStart();
        EditText textoPubli = (EditText) findViewById(R.id.eEscribeT);
        textoPubli.setText(texto);
    }

    protected void onResume(){
        super.onResume();
        EditText textoPubli = (EditText) findViewById(R.id.eEscribeT);
        textoPubli.setText(texto);
    }
}
