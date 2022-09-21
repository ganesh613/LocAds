package com.example.locationbasedads;

import android.os.Bundle;
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


    private List<MenuModel> menuList = new ArrayList<>();
    private MenuAdapter mAdapter;

    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mGetReference = mDatabase.getReference();
   

    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_menu_list);
        super.onCreate(savedInstanceState);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        mAdapter = new MenuAdapter(menuList);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        prepareMenuData();

    }


    private void prepareMenuData() {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference ref = database.getReference("AddMenu");

// Attach a listener to read the data at our posts reference
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                menuList.clear();
                //https://stackoverflow.com/questions/52737082/java-util-hashmap-cannot-be-cast-to-com-google-android-gms-maps-model-latlng
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    String itemName = snapshot.child("itemName").getValue(String.class);
                    String itemPrice = snapshot.child("itemPrice").getValue(String.class);

                  MenuModel movie = new MenuModel(itemName, itemPrice);
                    menuList.add(movie);
                    mAdapter.notifyDataSetChanged();

                }
                    //value are store in three different arraylist
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
                Toast.makeText(MenuList.this, "database read failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

}