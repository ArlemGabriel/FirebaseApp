package com.example.firebaseapp;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.firebaseapp.Objetos.ReferenciasFirebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private AdaptadorImagen adaptador;
    private DatabaseReference refDatabase;
    private List<Cargar> cargas;
    private List<String> keys = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        cargas = new ArrayList<>();
        refDatabase = FirebaseDatabase.getInstance().getReference(ReferenciasFirebase.REFERENCIA_BASEDATOSFIREBASE);
        refDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                cargas.clear();
                for(DataSnapshot valorSnapshot : dataSnapshot.getChildren()){
                    Cargar cargar = valorSnapshot.getValue(Cargar.class);
                    cargas.add(cargar);
                    keys.add(valorSnapshot.getKey());
                }
                adaptador = new AdaptadorImagen(MainActivity.this,cargas,keys);
                recyclerView.setAdapter(adaptador);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(MainActivity.this,databaseError.getMessage(),Toast.LENGTH_LONG).show();;
            }
        });
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int posicion = viewHolder.getAdapterPosition();
                //Toast.makeText(MainActivity.this,"Posicion:"+posicion,Toast.LENGTH_LONG).show();
                adaptador.deleteItem(posicion);
                keys.remove(posicion);
                cargas.remove(posicion);
                //Log.i("KEYS",keys.toString());
                //Log.i("CARGAS",cargas.toString());
                adaptador.notifyItemRemoved(posicion);
                adaptador.notifyItemRangeChanged(posicion, adaptador.getItemCount());
                //Toast.makeText(MainActivity.this,"Largo de Adaptador:"+adaptador.getItemCount(),Toast.LENGTH_LONG).show();

            }
        }).attachToRecyclerView(recyclerView);

        FloatingActionButton fab = findViewById(R.id.fab);
        int greenColorValue = Color.parseColor("#ffbf00");
        fab.setBackgroundTintList(ColorStateList.valueOf(greenColorValue));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,AgregarProducto.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
