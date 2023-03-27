package com.example.locationbasedads;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.Nullable;

import java.util.ArrayList;


public class HomeActivity extends AppCompatActivity {


    // creating variables for our list view.
    private ListView coursesLV2;

    private ArrayAdapter<String> adapter2;

    // creating a new array list.
    ArrayList<String> coursesArrayList2;
    // creating a variable for database reference.
    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mGetReference = mDatabase.getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        // initializing variables for listviews.
        coursesLV2 = findViewById(R.id.show_data);


        // initializing our array list
        coursesArrayList2 = new ArrayList<String>();

        // ArrayAdapter adapter=new ArrayAdapter(coursesArrayList, );
        adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,coursesArrayList2);

        coursesLV2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//                String item = adapter2.getItem(position);
//                adapter2.remove(item2);
//                mDatabase.getReference("AddMenu/"+number.getNum()).removeValue();

                String referenceName = coursesLV2.getItemAtPosition(position).toString();
                Intent intent = new Intent(getApplicationContext(), CheckList.class);
                intent.putExtra("reference_name", referenceName.trim());
                startActivity(intent);

                //child(itemId).removeValue()
                adapter2.notifyDataSetChanged();
                //Delete item form Firebase database
            }
        });

        // calling a method to get data from
        // Firebase and set data to list view
        initializeListView();
    }

    private void initializeListView() {
        // creating a new array adapter for our list view.
        //final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, coursesArrayList);

        // below line is used for getting reference
        // of our Firebase Database.

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("AddData");
//        Toast.makeText(HomeActivity.this, "Please add some data."+ref, Toast.LENGTH_SHORT).show();

        // in below line we are calling method for add child event
        // listener to get the child of our database.
        ProgressBar progressBar = findViewById(R.id.load_data);



        ref.addChildEventListener(new ChildEventListener() {
            @Override


            public void onChildAdded(@NonNull DataSnapshot datasnapshot2, @Nullable String previousChildName2) {
                // this method is called when new child is added to
                // our data base and after adding new child
                // we are adding that item inside our array list and
                // notifying our adapter that the data in adapter is changed.

                //  for (DataSnapshot snapshot: datasnapshot.getChildren()) {
//                String data = datasnapshot2.child("itemNum").getValue(String.class);
                String data="    "+datasnapshot2.child("itemName").getValue(String.class);
//                data+="   -   "+datasnapshot2.child("itemPrice").getValue(String.class);
//                Toast.makeText(HomeActivity.this, "Please add some data."+data, Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                coursesArrayList2.add(data);
                //  }
//                    coursesArrayList.add(snapshot.getValue(String.class));
                adapter2.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // this method is called when the new child is added.
                // when the new child is added to our list we will be
                // notifying our adapter that data has changed.
                adapter2.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                // below method is called when we remove a child from our database.
                // inside this method we are removing the child from our array list
                // by comparing with it's value.
                // after removing the data we are notifying our adapter that the
                // data has been changed.
////                coursesArrayList2.remove(snapshot.getValue(String.class));
//                adapter2.notifyDataSetChanged();
                String removedValue = snapshot.getValue(String.class);

                // Remove the corresponding item from the array list
                for (int i = 0; i < coursesArrayList2.size(); i++) {
                    if (coursesArrayList2.get(i).equals(removedValue)) {
                        coursesArrayList2.remove(i);
                        break;
                    }
                }

                // Notify the adapter of the data set change
                adapter2.notifyDataSetChanged();
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

        coursesLV2.setAdapter(adapter2);

    }
}