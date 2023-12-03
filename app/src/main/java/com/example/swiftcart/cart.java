package com.example.swiftcart;

import android.os.Bundle;

import android.os.Handler;
import android.widget.Button;
import android.widget.ListView;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.*;



public class cart extends AppCompatActivity {


    ListView cartlistitems;
    FirebaseFirestore db;
    ArrayList<DataModal> dataModalArrayList;
    static int size = 0;
    static int temp = 0;
    cartitems adapter;
    static long totalPrice = 0;
    Button total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);


        if(getSupportActionBar() != null)
            getSupportActionBar().setTitle(dashboard.getCartId());


        total = findViewById(R.id.total);
        cartlistitems = findViewById(R.id.cartlistitems);
        db = FirebaseFirestore.getInstance();
        dataModalArrayList = new ArrayList<>();


        context();




    }
    public void context(){
        loadcartitems();
        refresh(10000);
    }

    private void refresh(int ms){
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                context();
            }
        };
        handler.postDelayed(runnable,ms);
    }

    private void loadcartitems()
    {
        dataModalArrayList = new ArrayList<>();

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

                            adapter = new cartitems(cart.this, dataModalArrayList);
                            size = dataModalArrayList.size();
                            if(size == 0)
                            {
                                cartlistitems.setAdapter(adapter);
                                String temp1 = Long.toString(calculateTotal(dataModalArrayList));
                                total.setText("Checkout and Total  " + temp1);
                            }
                            if(temp != size)
                            {
                                cartlistitems.setAdapter(null);
                                cartlistitems.setAdapter(adapter);
                                String temp1 = Long.toString(calculateTotal(dataModalArrayList));
                                total.setText("Checkout and Total  " + temp1);
                                temp = size;
                            }


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
    private long calculateTotal(ArrayList<DataModal> dataModalArrayList){
        totalPrice = 0;
        for(DataModal dataModal:dataModalArrayList){
            db.collection("prices")
                    .whereEqualTo("name", dataModal.getName())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Map<String,Object> map= document.getData();
                                    if(map.get("price") != null) totalPrice = Integer.parseInt((String) map.get("price"));
                                }
                            }
                        }
                    });
        }
        return totalPrice;
    }

}

