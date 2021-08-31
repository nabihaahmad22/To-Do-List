package com.example.todolist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {
    Toolbar signuptoolbar;
    EditText signupemail,signuppwd;
    Button signupbtn;
    TextView haveanaccount;
    ProgressBar signupprogbar;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

       signuptoolbar=findViewById(R.id.toolbar_signup);
        setSupportActionBar(signuptoolbar);
        getSupportActionBar().setTitle("Sign Up");




        signupemail=findViewById(R.id.email_address_signup);
       signuppwd=findViewById(R.id.password_signup);
        signupbtn=findViewById(R.id.signup_button);
      haveanaccount=findViewById(R.id.gotologin);
    signupprogbar=findViewById(R.id.progressBar_signup);

        mAuth=FirebaseAuth.getInstance();

        haveanaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SignUpActivity.this,LogInActivity.class);
                startActivity(intent);
            }
        });

        signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String signemail= signupemail.getText().toString().trim();
                String signpwdd=signuppwd.getText().toString().trim();

                if(TextUtils.isEmpty(signemail)){
                    signupemail.setError("Email is Required");
                    return;
                }
                if(TextUtils.isEmpty(signpwdd)){
                   signuppwd.setError("Password is Required");
                    return;
                }
                if(signpwdd.length()<8){
                    signuppwd.setError("Password should be atleast 8 characters");
                    return;
                }
                else{
                    signupprogbar.setVisibility(View.VISIBLE);
                    mAuth.createUserWithEmailAndPassword(signemail,signpwdd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(SignUpActivity.this,"Logged In",Toast.LENGTH_LONG).show();
                                startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                                signupprogbar.setVisibility(View.GONE);
                            }
                            else {
                                Toast.makeText(SignUpActivity.this,"Error! "+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                                signupprogbar.setVisibility(View.GONE);
                            }
                        }
                    });
                }
            }
        });


    }
}