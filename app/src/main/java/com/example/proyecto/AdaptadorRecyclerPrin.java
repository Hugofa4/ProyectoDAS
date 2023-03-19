package com.example.proyecto;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdaptadorRecyclerPrin extends RecyclerView.Adapter<ListaPrin>{

    private int[] imagenes;
    private String[] usuarios;
    private String[] textos;

    public AdaptadorRecyclerPrin(int[] i, String[] u, String[] t){
        imagenes = i;
        usuarios = u;
        textos = t;
    }

    @NonNull
    @Override
    public ListaPrin onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View elLayoutDeCadaItem= LayoutInflater.from(parent.getContext()).inflate(R.layout.cardviewprin,null);
        ListaPrin lp = new ListaPrin(elLayoutDeCadaItem);
        return lp;
    }

    @Override
    public void onBindViewHolder(@NonNull ListaPrin holder, int position) {
        holder.imagen.setImageResource(imagenes[position]);
        holder.usuario.setText(usuarios[position]);
        holder.texto.setText(textos[position]);
    }

    @Override
    public int getItemCount() {
        return usuarios.length;
    }
}
