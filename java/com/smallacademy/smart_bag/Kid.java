package com.smallacademy.smart_bag;

import android.annotation.SuppressLint;


import android.app.DatePickerDialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
public class Kid extends FirebaseRecyclerAdapter<Player, Kid.PastViewHolder> {
        private Context context;
        boolean valid=true;
    private DatePickerDialog picker;
        public Kid(@NonNull FirebaseRecyclerOptions<Player> options,Context context) {
            super(options);
            this.context = context;
        }



        @Override
        protected void onBindViewHolder(@NonNull final PastViewHolder holder, @SuppressLint("RecyclerView") final int i, @NonNull final Player player) {
            holder.name.setText(player.getName());
            holder.id.setText(player.getId());
           holder.dbth.setText(player.getDate());
            holder.score.setText(player.getScore());
            holder.initidg.setText(player.getInitialdiagnosis());

            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DatabaseReference db=FirebaseDatabase.getInstance().getReference("kids").child(player.getId());
                    db.removeValue();
                    //Toast.makeText(MainActivity.this,"User Doesn't Exist",Toast.LENGTH_SHORT).show();

                }
            });
            holder.edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final DialogPlus dialog = DialogPlus.newDialog(context)
                            .setGravity(Gravity.CENTER)
                            .setMargin(50,0,50,0)
                            .setContentHolder(new ViewHolder(R.layout.content))
                            .setExpanded(false)  // This will enable the expand feature, (similar to android L share dialog)
                            .create();

                    final View holderView = (LinearLayout)dialog.getHolderView();

                    final EditText name = holderView.findViewById(R.id.ckidname);
                    final EditText dateofbirth = holderView.findViewById(R.id.ckiddbth);
                    final EditText initdig = holderView.findViewById(R.id.ckidinidig);
                    final EditText id = holderView.findViewById(R.id.ckidid);
                    final EditText score = holderView.findViewById(R.id.ckidscore);
                    dateofbirth.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final Calendar calendar=Calendar.getInstance();
                            int day=calendar.get(Calendar.DAY_OF_MONTH);
                            int month=calendar.get(Calendar.MONTH);
                            int year=calendar.get(Calendar.YEAR);
                            picker=new DatePickerDialog(holderView.getContext(), new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                    dateofbirth.setText(dayOfMonth+"/"+(month+1)+"/"+year);

                                }
                            },year,month,day);
                            picker.show();
                        }
                    });
                    name.setText(player.getName());
                    dateofbirth.setText(player.getDate());
                    initdig.setText(player.getInitialdiagnosis());
                    id.setText(player.getId());
                    score.setText(player.getScore());

                    Button update = holderView.findViewById(R.id.ckidupdate);
                    update.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            boolean vn=checkField(name)&&checkField(dateofbirth)&&checkField(initdig)&&checkField(id)&&checkField(score);
                          if(vn) {
                              Map<String, Object> map = new HashMap<>();
                              map.put("Name", name.getText().toString());
                              map.put("Id", id.getText().toString());
                              map.put("Date", dateofbirth.getText().toString());
                              map.put("Score", score.getText().toString());
                              map.put("Initialdiagnosis", initdig.getText().toString());
                              FirebaseDatabase.getInstance().getReference()
                                      .child("kids")
                                      .child(getRef(i).getKey())
                                      .updateChildren(map)
                                      .addOnCompleteListener(new OnCompleteListener<Void>() {
                                          @Override
                                          public void onComplete(@NonNull Task<Void> task) {
                                              dialog.dismiss();
                                          }
                                      });

                          }
                        }
                    });


                    dialog.show();
                }

            });


        }

        @NonNull
        @Override
        public PastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.activity_kid, parent, false);
            return new PastViewHolder(view);
        }

class PastViewHolder extends RecyclerView.ViewHolder{

    TextView name,id,score,initidg,dbth;
    ImageView edit;
    ImageView delete;


    public PastViewHolder(@NonNull View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.kidnameP);
        id = itemView.findViewById(R.id.kididfather);
        dbth = itemView.findViewById(R.id.kiddbthP);
        score = itemView.findViewById(R.id.kidscoreP);
        initidg = itemView.findViewById(R.id.kidinidigP);
        edit = itemView.findViewById(R.id.edit);
        delete=itemView.findViewById(R.id.deleteim);
    }
}


    public boolean checkField(EditText textField){
        if(textField.getText().toString().isEmpty()){
            textField.setError("Error");
            valid = false;
        }
        else {
            valid = true;
        }

        return valid;
    }


}