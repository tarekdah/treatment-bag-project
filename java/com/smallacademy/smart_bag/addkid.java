package com.smallacademy.smart_bag;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.HashMap;

public class addkid extends AppCompatActivity {

    EditText name, id, dbth, score, initdig;
    Button Add;
    FirebaseDatabase database;
    DatabaseReference ref;
    Player player;
    boolean valid = true, valid2 = true;
    private FirebaseDatabase database1;
   private  DatePickerDialog picker;
    private void getvalues() {

        player.setName(name.getText().toString());

        player.setDate(dbth.getText().toString());

        player.setId(id.getText().toString());


        player.setScore(score.getText().toString());
        player.setInitialdiagnosis(initdig.getText().toString());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addkid);
        name = findViewById(R.id.kidnameP);
        id = findViewById(R.id.kididfather);
        dbth = findViewById(R.id.kidbirth);
        score = findViewById(R.id.kidscoreP);
        initdig = findViewById(R.id.kiddig);
        Add = findViewById(R.id.kidaddbtn);
        player = new Player();
        dbth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar=Calendar.getInstance();
                int day=calendar.get(Calendar.DAY_OF_MONTH);
                int month=calendar.get(Calendar.MONTH);
                int year=calendar.get(Calendar.YEAR);
                picker=new DatePickerDialog(addkid.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        dbth.setText(dayOfMonth+"/"+(month+1)+"/"+year);

                    }
                },year,month,day);
                picker.show();
            }
        });
        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean vl = checkField(name) &&
                        checkField(id) &&
                        checkexists(id.getText().toString()) &&
                        checkField(dbth) &&
                        checkField(score) &&checkid(id)&&
                        checkField(initdig);
                if (vl) {

                    getvalues();


                    final HashMap<String, Object> map = new HashMap<>();
                    map.put("Name", player.getName().toString());
                    map.put("Id", player.getId().toString());
                    map.put("Date", player.getDate().toString());
                    map.put("Score", player.getScore().toString());
                    map.put("Initialdiagnosis", player.getInitialdiagnosis().toString());
                    database = FirebaseDatabase.getInstance();
                    ref = database.getReference("kids");
                    ref.child(player.getId().toString()).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Log.i("aaa", "On complete");
                        }

                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.i("ss", "On failure" + e.toString());
                        }

                    }).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(addkid.this, "Inserted", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(getApplicationContext(), Admin.class));
                        }
                    });


                }
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

    public boolean checkexists(final String s) {
        database = FirebaseDatabase.getInstance();
        ref = database.getReference("kids");
        ref.child(s).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    valid2=false;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                valid2=false;
            }
        });
        return valid2;


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
        valid=true;
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


