package com.smallacademy.smart_bag;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.smallacademy.smart_bag.databinding.ActivitySearchSonBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;


public class SearchSon extends AppCompatActivity {

    ActivitySearchSonBinding binding;
    DatabaseReference reference;
    FirebaseUser user;
    String uid;
    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        binding = ActivitySearchSonBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.readdataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = binding.etusername.getText().toString();
                if (!username.isEmpty()){
                    readData(username);
                }
                else{

                    Toast.makeText(SearchSon.this,"PLease Enter Username",Toast.LENGTH_SHORT).show();
                }

            }
        });

       Button back_button = findViewById(R.id.back_id);

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                finish();
            }
        });


    }

    private void readData(final String username) {

        reference = FirebaseDatabase.getInstance("https://projectbag2-default-rtdb.firebaseio.com/").getReference("kids");
        reference.child(username).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    final String firstName = String.valueOf(dataSnapshot.child("Name").getValue());
                    final String diag = String.valueOf(dataSnapshot.child("Initialdiagnosis").getValue());
                    final String score = String.valueOf(dataSnapshot.child("Score").getValue());
                    // checkSon(username,firstName,diag,score);
                    fStore= FirebaseFirestore.getInstance();
                    user = FirebaseAuth.getInstance().getCurrentUser();
                    uid=user.getEmail();

                    final DocumentReference[] df = {fStore.collection("Users").document(uid)};
                    df[0].get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists())
                                {
                                    String to_check;
                                    boolean flag=false;
                                    String[] Ids = document.getData().get("SonID").toString().split("\\s*,\\s*");
                                    int i=0;
                                    for(String id:Ids){
                                        if(i==0){
                                            if(Ids.length==1) {
                                                 to_check = id.substring(1).substring(0, id.length() - 2);
                                            } else
                                                to_check=id.substring(1);
                                                if (to_check.equals(username)) {
                                                    flag = true;
                                                    break;
                                                }



                                        }
                                        else
                                        if(i==Ids.length-1){
                                            if(id.substring(0,id.length()-1).equals(username)){
                                                flag=true;
                                                break;
                                            }
                                        }
                                        else if(id.equals(username)) {
                                            flag = true;
                                            break;
                                        }

                                        i++;
                                    }
                                 if(flag) {
                                            //  DataSnapshot dataSnapshot = task.getResult();
                                            binding.FirstUpdate.setText("First Name: " + firstName);
                                            binding.DiagnosisUpdate.setText("Achievments: " + diag);
                                            binding.scoreUpdate.setText("Score: " + score);
                                            df[0] = null;
                                            fStore = null;
                                          return;
                                        }
                                    }

                                        Toast.makeText(SearchSon.this,"The Kid Id  Doesn't Exist",Toast.LENGTH_SHORT).show();
                                        df[0]=null;
                                        fStore=null;


                                    }

                                }



                    });
                    dataSnapshot=null;

                }
                else {
                    Toast.makeText(SearchSon.this,"User Doesn't Exist",Toast.LENGTH_SHORT).show();


                }
            }

            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SearchSon.this,"Failed to read",Toast.LENGTH_SHORT).show();

            }
        });
        /*reference.child(username).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {

                if (task.isSuccessful()){
                    if (task.getResult().exists()){
                        DataSnapshot dataSnapshot = task.getResult();
                        final String firstName = String.valueOf(dataSnapshot.child("Name").getValue());
                        final String diag = String.valueOf(dataSnapshot.child("Initialdiagnosis").getValue());
                        final String score = String.valueOf(dataSnapshot.child("Score").getValue());
                       // checkSon(username,firstName,diag,score);
                        fStore= FirebaseFirestore.getInstance();
                        user = FirebaseAuth.getInstance().getCurrentUser();
                        uid=user.getEmail();

                        final DocumentReference[] df = {fStore.collection("Users").document(uid)};
                        df[0].get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                System.out.print("fuck1");
                                if (task.isSuccessful()) {

                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists())
                                    {
                                        System.out.print("fuck");
                                        String ahha=(document.getData().get("SonID")).toString();
                                        if(ahha.equals(username)){
                                            //  DataSnapshot dataSnapshot = task.getResult();
                                            binding.FirstUpdate.setText("First Name: "+firstName);
                                            binding.DiagnosisUpdate.setText("Diagnosis: "+diag);
                                            binding.scoreUpdate.setText("Score: "+score);
                                            df[0] =null;
                                            fStore=null;
                                        }
                                        else{
                                            Toast.makeText(SearchSon.this,"User Doesn't Exist",Toast.LENGTH_SHORT).show();
                                            df[0]=null;
                                            fStore=null;


                                        }

                                    }
                                }

                            }
                        });
                        dataSnapshot=null;

                    }
                    else {

                        Toast.makeText(SearchSon.this,"User Doesn't Exist",Toast.LENGTH_SHORT).show();


                    }


                }
                else {

                    Toast.makeText(SearchSon.this,"Failed to read222",Toast.LENGTH_SHORT).show();
                }

            }
        });*/

    }
    private void checkSon(final String ids,final String firstName,final String diag,final String score) {

        fStore= FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        uid=user.getEmail();

        final DocumentReference[] df = {fStore.collection("Users").document(uid)};
        df[0].get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {

                    DocumentSnapshot document = task.getResult();
                    if (document.exists())
                    {
                        String ahha=(document.getData().get("SonID")).toString();
                        if(ahha.equals(ids)){
                            //  DataSnapshot dataSnapshot = task.getResult();
                            binding.FirstUpdate.setText("First Name: "+firstName);
                            binding.DiagnosisUpdate.setText("Diagnosis: "+diag);
                            binding.scoreUpdate.setText("Score: "+score);
                            df[0] =null;
                            fStore=null;
                        }
                        else{
                            Toast.makeText(SearchSon.this,"User Doesn't Exist",Toast.LENGTH_SHORT).show();
                            df[0]=null;
                            fStore=null;


                        }

                    }
                }

            }
        });


    }




}