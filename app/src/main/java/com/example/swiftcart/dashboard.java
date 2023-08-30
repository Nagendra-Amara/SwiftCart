package com.example.swiftcart;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;
import androidx.appcompat.app.AppCompatActivity;
import androidx.activity.result.ActivityResultLauncher;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class dashboard extends AppCompatActivity {

    static String cartid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);



        TextView cart = findViewById(R.id.cart);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        Button logout = findViewById(R.id.logout);
        FirebaseUser user = mAuth.getCurrentUser();
        Button start = findViewById(R.id.start);
        Button extracart = findViewById(R.id.extracart);

        if(getSupportActionBar()!=null)
            getSupportActionBar().hide();

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

       start.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               scanCode();
           }
       });
    }

    private void scanCode() {
        ScanOptions  options = new ScanOptions();
        options.setPrompt("Volume up to flash on");
        options.setBeepEnabled(false);
        options.setOrientationLocked(true);
        options.setCaptureActivity(com.journeyapps.barcodescanner.CaptureActivity.class);
        barLauncher.launch(options);
    }

    ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(),result->{
        if(result.getContents() != null)
        {
            cartid = result.getContents();
            Intent i = new Intent(getApplicationContext(),cart.class);
            startActivity(i);
            Toast.makeText(this, "Cart Registered Successfully", Toast.LENGTH_SHORT).show();
        }
    });

    public static String getCartId(){
        return cartid;
    }





}