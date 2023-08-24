package com.example.swiftcart;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class dashboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        TextView cart = findViewById(R.id.cart);


        if(getSupportActionBar()!=null)
            getSupportActionBar().hide();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        Button logout = findViewById(R.id.logout);
        FirebaseUser user = mAuth.getCurrentUser();
        if(user == null){
            Intent i = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(i);
        }

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent i = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(i);
            }
        });

        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),cart.class);
                startActivity(i);
            }
        });
    }
}