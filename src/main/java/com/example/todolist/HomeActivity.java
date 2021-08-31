package com.example.todolist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Date;

public class HomeActivity extends AppCompatActivity {


    Toolbar home_toolbar;
    RecyclerView recView;
    FloatingActionButton float_button;

    FirebaseAuth mAuth;
    FirebaseUser muser;
    DatabaseReference reference;
    String oneLineUserId;

    ProgressBar progbarhome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        home_toolbar=findViewById(R.id.toolbar_home);
        setSupportActionBar(home_toolbar);
        getSupportActionBar().setTitle("To Do List");

        recView=findViewById(R.id.recyclerview);
        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recView.setHasFixedSize(true);
        recView.setLayoutManager(linearLayoutManager);


        float_button=findViewById(R.id.fab);
        progbarhome=findViewById(R.id.progressbar_home);

        mAuth=FirebaseAuth.getInstance();

        muser=mAuth.getCurrentUser();
        if(muser!= null)
        {oneLineUserId=muser.getUid();}
        reference= FirebaseDatabase.getInstance().getReference().child("All Tasks").child(oneLineUserId);

        float_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addTask();
            }
        });

    }

    private void addTask() {
        AlertDialog.Builder myDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);

        View myView = inflater.inflate(R.layout.input_file, null);
        myDialog.setView(myView);

        final AlertDialog dialog = myDialog.create();
        dialog.setCancelable(false);

        final EditText edit_task= myView.findViewById(R.id.edit_task);
        final EditText edit_description= myView.findViewById(R.id.edit_description);

        Button save = myView.findViewById(R.id.save_button);
        Button cancel = myView.findViewById(R.id.cancel_button);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mTask = edit_task.getText().toString().trim();
                String mDescription = edit_description.getText().toString().trim();
                String email = reference.push().getKey();
                String date = DateFormat.getDateInstance().format(new Date());


                if (TextUtils.isEmpty(mTask)) {
                    edit_task.setError("Task Required");
                    return;
                }
                if (TextUtils.isEmpty(mDescription)) {
                    edit_description.setError("Description Required");
                    return;
                } else {
                    progbarhome.setVisibility(View.VISIBLE);

                   Model model=new Model(mTask,mDescription,email,date);
                    reference.child(email).setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(HomeActivity.this, "Task has been inserted successfully", Toast.LENGTH_SHORT).show();
                                progbarhome.setVisibility(View.GONE);
                            } else {
                                String error = task.getException().toString();
                                Toast.makeText(HomeActivity.this, "Failed: " + error, Toast.LENGTH_SHORT).show();
                                progbarhome.setVisibility(View.GONE);
                            }
                        }
                    });

                }

                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Model> options = new FirebaseRecyclerOptions.Builder<Model>()
                .setQuery(reference, Model.class)
                .build();
        FirebaseRecyclerAdapter<Model, myViewHolder> adapter=new FirebaseRecyclerAdapter<Model, myViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull myViewHolder myViewHolder, int i, @NonNull Model model) {
                myViewHolder.setDate(model.getDate());
                myViewHolder.setTask(model.getTask());
                myViewHolder.setDesc(model.getDescription());


            }

            @NonNull
            @Override
            public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.retrieved_layout,parent,false);
                return  new myViewHolder(view);
            }
        };
        recView.setAdapter(adapter);
        adapter.startListening();
    }
    public static class  myViewHolder extends  RecyclerView.ViewHolder{
        View mView;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            mView=itemView;
        }
        public void setTask(String task){
            TextView task_set=mView.findViewById(R.id.taskcards);
            task_set.setText(task);
        }
        public void setDesc(String desc){
            TextView desc_set=mView.findViewById(R.id.descriptioncards);
            desc_set.setText(desc);
        }
        public void setDate(String date){
            TextView date_set=mView.findViewById(R.id.date_cards);
            date_set.setText(date);
        }
    }
}