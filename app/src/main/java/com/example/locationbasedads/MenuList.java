package com.example.locationbasedads;

import android.graphics.Picture;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MenuList extends AppCompatActivity {


   // HashMap.Entry<String,String> entry=hashMap.entrySet().iterator().next();
    private List<MenuModel> movieList= new ArrayList<>();
    private MenuAdapter mAdapter;

    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mGetReference = mDatabase.getReference();
   

    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_menu_list);
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);

      //  prepareMovieData();


        mAdapter = new MenuAdapter(movieList);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        //prepareMovieData();
        prepareMovieData();

    }



    private void prepareMovieData() {
        //Toast.makeText(this, ""+value, Toast.LENGTH_SHORT).show();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
       // Intent intent = getIntent();
        //String str = intent.getStringExtra("fc");
        DatabaseReference ref = database.getReference("AddMenu");
        ImageView rImage = findViewById(R.id.rImage);

// Attach a listener to read the data at our posts reference
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                movieList.clear();
                //https://stackoverflow.com/questions/52737082/java-util-hashmap-cannot-be-cast-to-com-google-android-gms-maps-model-latlng
              // for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    String title = snapshot.child("itemName").getValue(String.class);
                    String imgURL = snapshot.child("itemImage").getValue(String.class);
                   // Picasso.get().load(imgURL).into(rImage);
                    //Picasso.with(this).load(imgURL).into(rImage);

                    String price = snapshot.child("itemPrice").getValue(String.class);

                   // Log.i("onDataChange", snapshot.getKey()+": "+imgURL+", "+userName);
                 //   Toast.makeText(MenuList.this, ""+snapshot.getKey()+" : "+imgURL+", "+userName+" "+itemNum, Toast.LENGTH_SHORT).show();
                     MenuModel movie = new MenuModel(imgURL, title, price);
                    movieList.add(movie);
                    mAdapter.notifyDataSetChanged();

                }
                   // Toast.makeText(MenuList.this, ""+snapshot.getValue()+""+snapshot.getValue()+""+snapshot.getValue(), Toast.LENGTH_SHORT).show();
              // String  itemname=dataSnapshot.child("itemName").getValue(String.class);
              //  Toast.makeText(MenuList.this, ""+itemname, Toast.LENGTH_SHORT).show();

                    //value are store in three different arraylist
               // }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
                Toast.makeText(MenuList.this, "database read failed", Toast.LENGTH_SHORT).show();
            }
        });
       // MovieModel movie = new MovieModel("Fc menu", "Action & Adventure", "2015");
        //movieList.add(movie);

//        mAdapter.notifyDataSetChanged();
    }

}