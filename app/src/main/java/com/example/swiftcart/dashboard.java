package com.example.swiftcart;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;
import androidx.appcompat.app.AppCompatActivity;
import androidx.activity.result.ActivityResultLauncher;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

    static String cartid,email;
    GridView coursesGV;
    ArrayList<DataModal> dataModalArrayList;
    FirebaseFirestore db;
    TextView greetings;
    FirebaseUser user;
    boolean status = true;
    FirebaseDatabase database;
    DatabaseReference myRef;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);



        //gets instance of firebase
        database = FirebaseDatabase.getInstance();
        



        TextView cart = findViewById(R.id.cart);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        Button logout = findViewById(R.id.logout);
        user = mAuth.getCurrentUser();
        Button start = findViewById(R.id.start);
        //Button extracart = findViewById(R.id.extracart);
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
            email = user.getEmail();
            db.collection("users")
                    .whereEqualTo("email", email)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String,Object> map= document.getData();
                                StringBuilder name = new StringBuilder("Hi ," + map.get("name"));
                                for(int i=name.length();i<25;i++)
                                    name.append(" ");
                                greetings.setText(name.toString());
                            }
                        }
                    });
        }



        logout.setOnClickListener(view -> {
            FirebaseAuth.getInstance().signOut();
            Intent i = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(i);
        });



        cart.setOnClickListener(view -> {
            Intent i = new Intent(getApplicationContext(),cart.class);
            startActivity(i);
        });

       start.setOnClickListener(view -> scanCode());

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
            myRef = database.getReference("customers");

            myRef.child(cartid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        status = false;
                    } else {
                        myRef = database.getReference("customers/"+cartid);
                        myRef.setValue(email);
                        Intent i = new Intent(getApplicationContext(), cart.class);
                        startActivity(i);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    status = false;
                }
            });
            
            if(!status) {
                Toast.makeText(this, "Something went wrong or please find another cart", Toast.LENGTH_SHORT).show();
            }
        }
    });

    public static String getCartId(){
        return cartid;
    }


    private void loadDatainGridView() {

        db.collection("Data").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
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
                }).addOnFailureListener(e -> {

                    Toast.makeText(dashboard.this, "Fail to load data..", Toast.LENGTH_SHORT).show();
                });
    }

}

