package com.example.swiftcart;

import android.content.Intent;
import android.os.Bundle;

import android.os.Handler;
import android.view.View;
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



public class cart extends AppCompatActivity  {


    ListView cartlistitems;
    FirebaseFirestore db;
    ArrayList<DataModal> dataModalArrayList;
    int size = 0;
    int temp = 0;
    cartitems adapter;
    int totalPrice;
    static Button total;
    String cartid;
    HashMap<String,String> prices;
    Button qr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        cartid = dashboard.getCartId();

        if(getSupportActionBar() != null)
            getSupportActionBar().setTitle(cartid);


        total = findViewById(R.id.total);
        cartlistitems = findViewById(R.id.cartlistitems);
        db = FirebaseFirestore.getInstance();
        dataModalArrayList = new ArrayList<>();
        qr = findViewById(R.id.qr);



        qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),checkout.class);
                startActivity(i);
            }
        });

        context();




    }
    public void context(){
        loadcartitems();
        refresh(1000);
    }

    private void refresh(int ms){
        final Handler handler = new Handler();
//        final Runnable runnable = new Runnable() {
//            @Override
//            public void run() {
//                context();
//            }
//        };
        final Runnable runnable = () -> context();
//        final Runnable runnable = this::context;
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
                            if (size == 0) {
                                cartlistitems.setAdapter(adapter);
                                String temp1 = Integer.toString(calculateTotal(dataModalArrayList));
                                total.setText(temp1);
                            }
                            if (temp != size) {
                                cartlistitems.setAdapter(null);
                                cartlistitems.setAdapter(adapter);
                                String temp1 = Integer.toString(calculateTotal(dataModalArrayList));
                                total.setText(temp1);
                                temp = size;
                            }

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(cart.this, "Fail to load data..", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private int calculateTotal(ArrayList<DataModal> dataModalArrayList){
        totalPrice = 0;
        prices = cartitems.prices;
        for(DataModal dataModal:dataModalArrayList)
        {
            if(prices.containsKey(dataModal.getName()) && prices.get(dataModal.getName()) != null)
                totalPrice += Integer.parseInt(prices.get(dataModal.getName()));
        }
        return totalPrice;
    }


    public static String getTotal() {
        return total.getText().toString();
    }

}

