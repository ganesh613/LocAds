package com.example.locationbasedads;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.os.Bundle;
import android.util.Log;
import android.webkit.HttpAuthHandler;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;


public class MenuList extends AppCompatActivity {




   // HashMap.Entry<String,String> entry=hashMap.entrySet().iterator().next();
    private List<MovieModel> movieList= new ArrayList<>();
    private MoviesAdapter mAdapter;

    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mGetReference = mDatabase.getReference();
   

    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_menu_list);
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);

      //  prepareMovieData();


        mAdapter = new MoviesAdapter(movieList);
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

        DatabaseReference ref = database.getReference("AddMenu");

// Attach a listener to read the data at our posts reference
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                movieList.clear();
                //https://stackoverflow.com/questions/52737082/java-util-hashmap-cannot-be-cast-to-com-google-android-gms-maps-model-latlng
              // for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    String imgURL = snapshot.child("itemName").getValue(String.class);
                    String itemNum = snapshot.child("itemNum").getValue(String.class);
                    String userName = snapshot.child("itemPrice").getValue(String.class);

                   // Log.i("onDataChange", snapshot.getKey()+": "+imgURL+", "+userName);
                 //   Toast.makeText(MenuList.this, ""+snapshot.getKey()+" : "+imgURL+", "+userName+" "+itemNum, Toast.LENGTH_SHORT).show();
                     MovieModel movie = new MovieModel(imgURL, itemNum, userName);
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