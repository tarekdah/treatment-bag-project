package com.smallacademy.smart_bag;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.smallacademy.smart_bag.databinding.ActivityMainBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class
MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    DatabaseReference reference;
    FirebaseUser user;
    String uid;
    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        Button search = findViewById(R.id.search_son);


        search.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View view) {

                                          startActivity(new Intent(getApplicationContext(), SearchSon.class));

                                      }
                                  });
        Button addkidf=findViewById(R.id.addkidfather);
        addkidf.setOnClickListener(new View.OnClickListener(){
            @Override
                    public void onClick(View view ){
                    startActivity(new Intent(getApplicationContext(),AddFatherKid.class));

            }

        });

        Button logout = findViewById(R.id.logoutBtn);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(),Login.class));
                finish();
            }


        });

    }

    private void readData(final String username) {

        reference = FirebaseDatabase.getInstance("https://projectbag2-default-rtdb.firebaseio.com/").getReference("kids");
      /*  reference.child(username).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()){
                    if (task.getResult().exists() ){
                        DataSnapshot dataSnapshot = task.getResult();
                      //  boolean v=checkSon(username,dataSnapshot);

                    }
                    else {

                        Toast.makeText(MainActivity.this,"User Doesn't Exist",Toast.LENGTH_SHORT).show();

                    }


                }
                else {

                    Toast.makeText(MainActivity.this,"Failed to read1111",Toast.LENGTH_SHORT).show();
                }

            }
        });*/
        reference.child(username).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               if(dataSnapshot.exists()){

               }
               else {

                   Toast.makeText(MainActivity.this,"User Doesn't Exist",Toast.LENGTH_SHORT).show();

               }
            }

            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this,"Failed to read1111",Toast.LENGTH_SHORT).show();

            }
        });

    }




}