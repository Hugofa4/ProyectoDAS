package com.example.proyecto;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdaptadorRecyclerPubli extends RecyclerView.Adapter<ListaPubli>{

    private int[] imagenes;
    private static boolean[] seleccionados;

    public AdaptadorRecyclerPubli(int[] i){
        imagenes = i;
        seleccionados = new boolean[i.length];

    }

    @NonNull
    @Override
    public ListaPubli onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View elLayoutDeCadaItem= LayoutInflater.from(parent.getContext()).inflate(R.layout.cardviewpubli,null);
        ListaPubli lp = new ListaPubli(elLayoutDeCadaItem);
        return lp;
    }

    @Override
    public void onBindViewHolder(@NonNull ListaPubli holder, int position) {
        holder.imagen.setImageResource(imagenes[position]);
        //Se crea un listener para saber cuando se ha seleccionado una imagen, con un array de booleanos.
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (seleccionados[holder.getAdapterPosition()]==true){
                    seleccionados[holder.getAdapterPosition()]=false;
                    //Si se quita la selección de una imagen se le quita el marco que se le había añadido
                    holder.imagen.setPadding(0,0,0,0);
                }
                else{
                    //Cuando una imagen es seleccionada se la añade un marco
                    seleccionados[holder.getAdapterPosition()]=true;
                    holder.imagen.setPadding(10,10,10,10);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return imagenes.length;
    }

    public int getImgSeleccionada(){
        //Método que se usa para sabeer la posición en el array de la imagen seleccionada
        int img = -1;
        int aux = 0;
        for (boolean s : seleccionados) {
            if (s){
                img = aux;
            }else{
                aux = aux + 1;
            }
        }
        if (img < 0){
            img = aux;
        }
        return img;
    }

}
