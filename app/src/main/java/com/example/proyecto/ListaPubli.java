package com.example.proyecto;

import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ListaPubli extends RecyclerView.ViewHolder {

    public ImageView imagen;

    public ListaPubli(@NonNull View itemView){
        super(itemView);
        //El contenido del recyclerView será una imagen
        imagen = itemView.findViewById(R.id.imagenPubli);
    }
}
