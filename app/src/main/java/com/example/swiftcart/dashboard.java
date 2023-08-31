package com.example.swiftcart;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.activity.result.ActivityResultLauncher;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class dashboard extends AppCompatActivity {

    static String cartid;
    GridView coursesGV;
    ArrayList<DataModal> dataModalArrayList;
    FirebaseFirestore db;

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
        coursesGV = findViewById(R.id.idGVCourses);
        dataModalArrayList = new ArrayList<>();
        db = FirebaseFirestore.getInstance();


        if(getSupportActionBar()!=null)
            getSupportActionBar().hide();

        if(user == null){
            Intent i = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(i);
        }


        loadDatainGridView();


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

    private void loadDatainGridView() {
        // below line is use to get data from Firebase
        // firestore using collection in android.
        db.collection("Data").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        // after getting the data we are calling on success method
                        // and inside this method we are checking if the received
                        // query snapshot is empty or not.
                        if (!queryDocumentSnapshots.isEmpty()) {
                            // if the snapshot is not empty we are hiding our
                            // progress bar and adding our data in a list.
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot d : list) {

                                // after getting this list we are passing
                                // that list to our object class.
                                DataModal dataModal = d.toObject(DataModal.class);

                                // after getting data from Firebase
                                // we are storing that data in our array list
                                dataModalArrayList.add(dataModal);
                            }
                            // after that we are passing our array list to our adapter class.
                            CoursesGVAdapter adapter = new CoursesGVAdapter(dashboard.this, dataModalArrayList);

                            // after passing this array list
                            // to our adapter class we are setting
                            // our adapter to our list view.
                            coursesGV.setAdapter(adapter);
                        } else {
                            // if the snapshot is empty we are displaying a toast message.
                            Toast.makeText(dashboard.this, "No data found in Database", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // we are displaying a toast message
                        // when we get any error from Firebase.
                        Toast.makeText(dashboard.this, "Fail to load data..", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}

