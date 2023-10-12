package com.example.swiftcart;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.*;


public class cart extends AppCompatActivity {


    GridView cartlistitems;
    FirebaseFirestore db;
    ArrayList<DataModal> dataModalArrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);


        if(getSupportActionBar() != null)
            getSupportActionBar().setTitle(dashboard.getCartId());

        //TextView cartid = findViewById(R.id.cartid);
        //cartid.setText(dashboard.getCartId());
        cartlistitems = findViewById(R.id.cartlistitems);
        db = FirebaseFirestore.getInstance();
        dataModalArrayList = new ArrayList<>();
        loadcartitems();
    }

    private void loadcartitems()
    {
        db.collection(dashboard.getCartId()).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot d : list) {
                                DataModal dataModal = d.toObject(DataModal.class);
                                dataModalArrayList.add(dataModal);
                            }

                            cartitems adapter = new cartitems(cart.this, dataModalArrayList);


                            cartlistitems.setAdapter(adapter);
                        } else {

                            Toast.makeText(cart.this, "No data found in Database", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // we are displaying a toast message
                        // when we get any error from Firebase.
                        Toast.makeText(cart.this, "Fail to load data..", Toast.LENGTH_SHORT).show();
                    }
                });
    }


}

