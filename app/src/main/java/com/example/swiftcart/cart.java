package com.example.swiftcart;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class cart extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);


        if(getSupportActionBar() != null)
            getSupportActionBar().hide();

        TextView cartid = findViewById(R.id.cartid);
        cartid.setText(dashboard.getCartId());
    }
}