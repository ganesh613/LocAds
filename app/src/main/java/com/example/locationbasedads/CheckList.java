package com.example.locationbasedads;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.Nullable;

import java.util.ArrayList;


public class CheckList extends AppCompatActivity{

    // creating variables for our list view.
    private ListView coursesLV;

    private ArrayAdapter<String> adapter;
    private static final String TAG = "CheckList";


    // creating a new array list.
    ArrayList<String> coursesArrayList;
    // creating a variable for database reference.
    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mGetReference = mDatabase.getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_list);


        // initializing variables for listviews.
        coursesLV = findViewById(R.id.idLVCourses);


        // initializing our array list
        coursesArrayList = new ArrayList<String>();

       // ArrayAdapter adapter=new ArrayAdapter(coursesArrayList, );
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,coursesArrayList);
        Intent intent = getIntent();

        // Get the reference name from the intent extras
        String referenceName = intent.getStringExtra("reference_name");
     //   Toast.makeText(CheckList.this, "ref "+referenceName, Toast.LENGTH_SHORT).show();

        coursesLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//                String item = adapter.getItem(position);
//                adapter.remove(item);
////                mDatabase.getReference("AddMenu/"+number.getNum()).removeValue();
//                mDatabase.getReference(referenceName+"/"+number.getNum()).removeValue();
//                        //child(itemId).removeValue()


//                adapter.notifyDataSetChanged();
                //Delete item form Firebase database
            }
        });

        // calling a method to get data from
        // Firebase and set data to list view
        initializeListView(referenceName.trim());
    }

    private void initializeListView(String referenceName) {
        // creating a new array adapter for our list view.
        //final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, coursesArrayList);

        // below line is used for getting reference
        // of our Firebase Database.

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference ref = database.getReference("AddMenu");
        if(referenceName.equals("Eta"))
            referenceName="AddBooks";
        if(referenceName.equals("FoodCourt"))
            referenceName="AddMenu";
        if(referenceName.equals("Academic Block"))
            referenceName="ABblock";

        DatabaseReference ref = database.getReference(referenceName+"");
        // in below line we are calling method for add child event
        // listener to get the child of our database.
        ref.addChildEventListener(new ChildEventListener() {
            @Override

            public void onChildAdded(@NonNull DataSnapshot datasnapshot, @Nullable String previousChildName) {
                // this method is called when new child is added to
                // our data base and after adding new child
                // we are adding that item inside our array list and
                // notifying our adapter that the data in adapter is changed.

              //  for (DataSnapshot snapshot: datasnapshot.getChildren()) {
                String data = datasnapshot.child("itemNum").getValue(String.class);
                data=data+"    "+datasnapshot.child("itemName").getValue(String.class);
                data+="   -   "+datasnapshot.child("itemPrice").getValue(String.class);

                coursesArrayList.add(data);
              //  }
//                    coursesArrayList.add(snapshot.getValue(String.class));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // this method is called when the new child is added.
                // when the new child is added to our list we will be
                // notifying our adapter that data has changed.
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                // below method is called when we remove a child from our database.
                // inside this method we are removing the child from our array list
                // by comparing with it's value.
                // after removing the data we are notifying our adapter that the
                // data has been changed.
                coursesArrayList.remove(snapshot.getValue(String.class));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // this method is called when we move our
                // child in our database.
                // in our code we are note moving any child.
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // this method is called when we get any
                // error from Firebase with error.
            }
        });
        // below line is used for setting
        // an adapter to our list view.
        coursesLV.setAdapter(adapter);
    }
}
