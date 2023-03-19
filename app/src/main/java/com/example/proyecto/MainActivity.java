package com.example.proyecto;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private SQLiteDatabase db;
    private String estado;
    private CharSequence usuario;
    private CharSequence correo;
    private CharSequence contra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button bSesion = (Button) findViewById(R.id.bIniciarSesion);
        estado = "prin";
        bSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openInicioSesion();
            }
        });
        Button bRegistro = (Button) findViewById(R.id.bRegistrar);
        bRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRegistro();
            }
        });
    }

    private void openInicioSesion(){
        estado = "inicioSesion";
        setContentView(R.layout.iniciosesion);
        //Se carga la base de datos
        BaseDeDatos baseDatos = new BaseDeDatos(this);
        db = baseDatos.getWritableDatabase();
        EditText usuarioI = (EditText) findViewById(R.id.eTusuarioI);
        EditText contraI = (EditText) findViewById(R.id.eTcontraI);
        Button bInicioS = (Button) findViewById(R.id.bInicioSI);
        //Se pone lo que estaba escrito en caso de que hubiese algo
        usuarioI.setText(usuario);
        contraI.setText(contra);
        bInicioS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comprobarUsuarioI(usuarioI.getText().toString(), contraI.getText().toString());
            }
        });
    }

    private void openRegistro(){
        estado = "registro";
        setContentView(R.layout.registro);
        BaseDeDatos baseDatos = new BaseDeDatos(this);
        db = baseDatos.getWritableDatabase();
        EditText usuarioR = (EditText) findViewById(R.id.eTusuarioR);
        EditText emailR = (EditText) findViewById(R.id.eTemailR);
        EditText contraR = (EditText) findViewById(R.id.eTcontraR);
        usuarioR.setText(usuario);
        emailR.setText(correo);
        contraR.setText(contra);
        Button bResgistro = (Button) findViewById(R.id.bRegistrarR);
        bResgistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comprobarUsuarioR(usuarioR.getText().toString(), emailR.getText().toString(), contraR.getText().toString());
            }
        });
    }

    private void comprobarUsuarioI(String usuario, String contra){
        boolean exist = false;
        String[] argumentos = new String[] {usuario, contra};
        String[] campos = new String[] {"Usuario"};
        //Se comprueba que en la base de datos exista un nombre de usuario con la contraseña introducida
        Cursor cu = db.query("datosUsuarios",campos,"usuario = ? and contraseña = ?",argumentos,null,null,null);
        while (cu.moveToNext()){
            //si entra en el bucle es que el usuario y la contraseña son correctos
            exist = true;
        }
        cu.close();
        if(exist){
            db.close();
            iniciarSesion(usuario);
        }else{
            //Dialogo explicando que el usuario o la contraseña son incorrectos
            DialogoMalInicio d = new DialogoMalInicio();
            d.show(getSupportFragmentManager(), "1");
        }
    }

    private void comprobarUsuarioR(String usuario, String email, String contra){
        boolean exist = false;
        String[] argumentos = new String[] {usuario};
        String[] campos = new String[] {"Usuario"};
        //Se comprueba que no haya ningun usuario existente con ese nombre
        Cursor cu = db.query("datosUsuarios",campos,"usuario = ?",argumentos,null,null,null);
        while (cu.moveToNext()){
            //Si entra en el bucle ya existe un usuario
            exist = true;
        }
        cu.close();
        if(exist){
            //Dialogo explicando que el nombre de usuario ya existe y se debe cambiar
            DialogoUsuarioOcupado d = new DialogoUsuarioOcupado();
            d.show(getSupportFragmentManager(), "1");
        }else{
            registrar(usuario, email, contra);
        }
    }

    private void registrar(String usuario, String email, String contra){
        String [] argumentos = new String[] {usuario, email, contra};
        //se inserta en la tabla el nuevo usuario
        db.execSQL("INSERT INTO datosUsuarios VALUES (?,?,?)", argumentos);
        Intent i = new Intent (this, PagPrincipal.class);
        i.putExtra("usuario",usuario);
        startActivity(i);
    }

    private void iniciarSesion(String usuario){
        //Se accede a la pagina principal con el usuario introducido
        Intent i = new Intent (this, PagPrincipal.class);
        i.putExtra("usuario",usuario);
        startActivity(i);
    }

    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        //Se guardan todas las variables que se han escrito y el estado en el que se encuentra la actividad
        savedInstanceState.putString("estado", estado);
        if (estado.equals("inicioSesion")){
            EditText usuarioI = (EditText) findViewById(R.id.eTusuarioI);
            EditText contraI = (EditText) findViewById(R.id.eTcontraI);
            savedInstanceState.putString("usuario", usuarioI.getText().toString());
            savedInstanceState.putString("contra", contraI.getText().toString());
        }else if(estado.equals("registro")){
            EditText usuarioR = (EditText) findViewById(R.id.eTusuarioR);
            EditText emailR = (EditText) findViewById(R.id.eTemailR);
            EditText contraR = (EditText) findViewById(R.id.eTcontraR);
            savedInstanceState.putString("usuario", usuarioR.getText().toString());
            savedInstanceState.putString("correo", emailR.getText().toString());
            savedInstanceState.putString("contra", contraR.getText().toString());

        }
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        //Se recuperan todos los datos guardados
        estado = savedInstanceState.getString("estado");
        usuario = savedInstanceState.getString("usuario");
        contra = savedInstanceState.getString("contra");
        if (estado.equals("registro")){
            correo = savedInstanceState.getString("correo");
        }
    }

    protected void onStart() {
        super.onStart();
        if (estado.equals("inicioSesion")){
            openInicioSesion();
        }else if(estado.equals("registro")){
            openRegistro();
        }
    }

    protected void onResume(){
        super.onResume();
        //Dependiendo del estado en el que estaba se va a un método o a otro
        if (estado.equals("inicioSesion")){
            openInicioSesion();
        }else if(estado.equals("registro")){
            openRegistro();
        }
    }
}