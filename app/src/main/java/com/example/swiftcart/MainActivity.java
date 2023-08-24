package com.example.swiftcart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {


    FirebaseAuth mAuth;
    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent i = new Intent(getApplicationContext(),dashboard.class);
            startActivity(i);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if(getSupportActionBar()!=null)
            getSupportActionBar().hide();

        
        mAuth = FirebaseAuth.getInstance();
        Button login = findViewById(R.id.login);
        TextView email = (TextView) findViewById(R.id.email);
        TextView password = (TextView) findViewById(R.id.password);
        TextView logintosignup = findViewById(R.id.logintosignup);
        ProgressBar progressBar = findViewById(R.id.progressBar);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                String email1,password1;
                email1 = String.valueOf(email.getText());
                password1 = String.valueOf(password.getText());
                mAuth.signInWithEmailAndPassword(email1, password1)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    progressBar.setVisibility(View.GONE);
                                    Intent i = new Intent(getApplicationContext(),dashboard.class);
                                    startActivity(i);
                                    Toast.makeText(MainActivity.this, "Login Success",
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    Intent i = new Intent(getApplicationContext(),signup.class);
                                    startActivity(i);
                                    Toast.makeText(MainActivity.this, "signup before login",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        logintosignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),signup.class);
                startActivity(i);
            }
        });
    }
}