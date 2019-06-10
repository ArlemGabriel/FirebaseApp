package com.example.firebaseapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.firebaseapp.Objetos.ReferenciasFirebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AdaptadorImagen extends RecyclerView.Adapter<AdaptadorImagen.ContenedorImagenes>{

    private Context contexto;
    private List<Cargar> cargas;
    private List<String> keys;

    public AdaptadorImagen(Context contexto, List<Cargar> cargas,List<String> keys){
        this.contexto = contexto;
        this.cargas = cargas;
        this.keys = keys;
    }
    @Override
    public ContenedorImagenes onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(contexto).inflate(R.layout.itemproducto,parent,false);
        return new ContenedorImagenes(v);
    }

    @Override
    public void onBindViewHolder(ContenedorImagenes holder, int position) {
        Cargar cargaActual = cargas.get(position);
        holder.textViewNombre.setText(cargaActual.getNombre());
        holder.textViewPrecio.setText(cargaActual.getPrecio());
        holder.textViewDescripcion.setText(cargaActual.getDescripcion());
        Picasso.with(contexto)
                .load(cargaActual.getUrl())
                .fit()
                .centerCrop()
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return cargas.size();
    }

    public Context getContexto() {
        return contexto;
    }
    public void deleteItem(int position){
        String key = keys.get(position);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(ReferenciasFirebase.REFERENCIA_BASEDATOSFIREBASE);
        ref.child(key).removeValue();
    }
    public class ContenedorImagenes extends RecyclerView.ViewHolder{
        public TextView textViewNombre;
        public TextView textViewPrecio;
        public TextView textViewDescripcion;
        public ImageView imageView;
        public ContenedorImagenes(View itemView){
            super(itemView);
            textViewNombre = itemView.findViewById(R.id.nombrePrd);
            textViewPrecio = itemView.findViewById(R.id.precioPrd);
            textViewDescripcion = itemView.findViewById(R.id.descripcionPrd);
            imageView = itemView.findViewById(R.id.imageViewPrd);
        }
    }
}
