package com.smallacademy.smart_bag;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class AddFatherKid extends Activity {
    EditText id;
    List<String> Ids;
    Button Addkidbuuton;
   boolean valid;
    FirebaseAuth fAuth=FirebaseAuth.getInstance();
    DocumentReference noteRef;
    FirebaseFirestore db=FirebaseFirestore.getInstance();
    final String KEY_ID = "SonID";
    String Id_to_update;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addkidfather);
        Ids = new ArrayList<>();
        String email=fAuth.getCurrentUser().getEmail();
         noteRef=db.collection("Users").document(email);
        id = findViewById(R.id.sonidtext);
        Addkidbuuton = findViewById(R.id.addkidfatherbutton);
       loadNote();

    }

    public void on_click(View v){
        if(checkField(id) && checkid(id)) {
            String new_id = id.getText().toString();
            Map<String, Object> newidmap = new HashMap<>();
            String first = Id_to_update.substring(0, Id_to_update.length() - 1);
            String to_insert = first + "," + new_id + "]";
            newidmap.put(KEY_ID, to_insert);
            noteRef.set(newidmap)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(AddFatherKid.this, "Kid was Added Successfully ", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AddFatherKid.this, "Error!", Toast.LENGTH_SHORT).show();

                        }
                    });
        }
    }


    public void loadNote(){
        noteRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>(){
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot){
                        if(documentSnapshot.exists()){
                            Id_to_update= documentSnapshot.getString(KEY_ID);

                        }else{
                            Toast.makeText(AddFatherKid.this,"Document does not exist",Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener(){
                    @Override
                    public void onFailure(@NonNull Exception e){
                        Toast.makeText(AddFatherKid.this,"Error!",Toast.LENGTH_SHORT).show();

                    }
                });
    }


    public boolean checkField(EditText textField) {
        if (textField.getText().toString().isEmpty()) {
            textField.setError("Error");
            valid = false;
        } else {
            valid = true;
        }

        return valid;
    }

    public boolean checkid(EditText text) {

        String[] Ids = text.getText().toString().split("\\s*,\\s*");
        int i = 0;
        String to_check;
        for (String id : Ids) {
            if (i == 0) {
                to_check=id;
                if(!check_id_number(to_check)) {
                    text.setError("Error");
                    valid=false;
                    return false;
                }
            }
            else if (i == Ids.length - 1) {

                if(!check_id_number(id)){
                    text.setError("Error");
                    valid=false;
                    return false;
                }
            }
            else {
                if(!check_id_number(id)) {
                    text.setError("Error");
                    valid = false;
                    return false;
                }
            }

            i++;
        }
        //reference = FirebaseDatabase.getInstance("https://projectbag2-default-rtdb.firebaseio.com/").getReference("kids");

        String first = Id_to_update;
        String[] ids =first.split("\\s*,\\s*");
        for(String id : ids) {
            for (String id1 : Ids) {
                if (id.equals(id1)) {
                    text.setError("Son ID Exist");
                    valid = false;
                    return false;
                }
            }
                /*reference.child(id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(!snapshot.exists()){
                            Toast.makeText(AddFatherKid.this,"One Or more Kids  Doesn't Exist",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
            */


            }

            valid = true;
            return true;
        }
    public boolean check_id_number(String id){
        if(id.length()!=9){
            return false;
        }
        for(int i=0;i<9;i++){
            if(!Character.isDigit(id.charAt(i))){
                return false;
            }
        }
        return true;
    }


}



