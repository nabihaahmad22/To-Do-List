package com.example.todolist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LogInActivity extends AppCompatActivity {
     Toolbar logintoolbar;
     EditText loginemail,loginpwd;
     Button loginbtn;
     TextView donthaveaccount;
     ProgressBar loginprogbar;
     FirebaseAuth mAuth;

     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        logintoolbar=findViewById(R.id.toolbar_login);
         setSupportActionBar(logintoolbar);
         getSupportActionBar().setTitle("Log In");




         loginemail=findViewById(R.id.email_address_login);
        loginpwd=findViewById(R.id.password_login);
        loginbtn=findViewById(R.id.login_button);
        donthaveaccount=findViewById(R.id.gotosignup);
        loginprogbar=findViewById(R.id.progressBar_login);

        mAuth=FirebaseAuth.getInstance();
donthaveaccount.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Intent intent=new Intent(LogInActivity.this,SignUpActivity.class);
        startActivity(intent);
    }
});
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailin= loginemail.getText().toString().trim();
                String passwordin= loginpwd.getText().toString().trim();

             if(TextUtils.isEmpty(emailin)){
                 loginemail.setError("Email is Required");
                 return;
             }
                if(TextUtils.isEmpty(passwordin)){
                    loginpwd.setError("Password is Required");
                    return;
                }
                if(passwordin.length()<8){
                    loginpwd.setError("Password should be atleast 8 characters");
                    return;
                }
           else{
               loginprogbar.setVisibility(View.VISIBLE);
               mAuth.signInWithEmailAndPassword(emailin,passwordin).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                   @Override
                   public void onComplete(@NonNull Task<AuthResult> task) {
                       if(task.isSuccessful()){
                           Toast.makeText(LogInActivity.this,"Logged In",Toast.LENGTH_LONG).show();
                           startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                           loginprogbar.setVisibility(View.GONE);
                       }
                       else {
                           Toast.makeText(LogInActivity.this,"Error! "+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                           loginprogbar.setVisibility(View.GONE);
                       }
                   }
               });


                }
            }
        });


    }


}