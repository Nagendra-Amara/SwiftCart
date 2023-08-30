package com.example.swiftcart;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
//import com.pusher.pushnotifications.PushNotifications;

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

