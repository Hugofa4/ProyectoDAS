package com.example.proyecto;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class PagPrincipal extends AppCompatActivity {

    private SQLiteDatabase db;
    private ActivityResultLauncher<Intent> startActivityIntent;
    private String usuario;
    private int[] imagenes;
    private String[] usuarios;
    private String[] textos;

    protected void onCreate(Bundle SavedInstanceState) {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            //Se recoge el usuario con el que se ha iniciado sesion
            usuario = extras.getString("usuario");
        }
        super.onCreate(SavedInstanceState);
        setContentView(R.layout.pagprincipal);
        RecyclerView lista= findViewById(R.id.recyclerprin);
        BaseDeDatos baseDatos = new BaseDeDatos(this);
        //Se recogen todos los campos existentes en la tabla publicaciones de la base de datos
        db = baseDatos.getWritableDatabase();
        String[] campos = new String[] {"Imagen", "Usuario", "Texto"};
        Cursor cu = db.query("publicaciones",campos,null,null,null,null,null);
        imagenes = new int[cu.getCount()];
        usuarios = new String[cu.getCount()];
        textos = new String[cu.getCount()];
        int cont = 0;
        while (cu.moveToNext()) {
            //Se guardan en un array manteniendo el orden las imagenes, usuarios y los textos publicados
            int img = cu.getInt(0);
            String usu = cu.getString(1);
            String text = cu.getString(2);
            imagenes[cont] = img;
            usuarios[cont] = usu;
            textos[cont] = text;
            cont = cont + 1;
        }
        //Se indica que los arrays que se acaban de crear son el contenido del recyclerView
        AdaptadorRecyclerPrin adaptador = new AdaptadorRecyclerPrin(imagenes,usuarios,textos);
        lista.setAdapter(adaptador);
        LinearLayoutManager layoutLineal= new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        lista.setLayoutManager(layoutLineal);

        Button nuevaPubli = (Button) findViewById(R.id.bNuevaPubli);
        nuevaPubli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pagNuevaPubli();
            }
        });
        startActivityIntent = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == RESULT_OK) {
                }
            }
        });
    }

    private void pagNuevaPubli(){
        //Se llama a la actividad de subir una nueva publicación pasandole el usuario con el que se ha iniciado sesión
        Intent intent = new Intent(PagPrincipal.this, NuevaPubli.class);
        intent.putExtra("usuario", usuario);
        startActivityIntent.launch(intent);
    }
}
