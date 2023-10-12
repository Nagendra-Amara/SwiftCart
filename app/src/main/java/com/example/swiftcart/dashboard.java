package com.example.swiftcart;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
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
import java.util.Map;

public class dashboard extends AppCompatActivity {

    static String cartid;

    GridView coursesGV;
    ArrayList<DataModal> dataModalArrayList;
    FirebaseFirestore db;
    TextView greetings;



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
        greetings = findViewById(R.id.greetings);
        coursesGV = findViewById(R.id.idGVCourses);
        dataModalArrayList = new ArrayList<>();


        db = FirebaseFirestore.getInstance();


        loadDatainGridView();



        if(getSupportActionBar()!=null)
            getSupportActionBar().hide();

        if(user == null){
            Intent i = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(i);
        }
        else{
            String email = user.getEmail();
            db.collection("users")
                    .whereEqualTo("email", email)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Map<String,Object> map= document.getData();
                                    String name = "Hi ,"+map.get("name");
                                    for(int i=name.length();i<25;i++)
                                        name = name+" ";
                                    greetings.setText(name);
                                }
                            }
                        }
                    });
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
        options.setOrientationLocked(false);
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

        db.collection("Data").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot d : list) {
                                DataModal dataModal = d.toObject(DataModal.class);
                                dataModalArrayList.add(dataModal);
                            }

                            CoursesGVAdapter adapter = new CoursesGVAdapter(dashboard.this, dataModalArrayList);


                            coursesGV.setAdapter(adapter);
                        } else {

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

