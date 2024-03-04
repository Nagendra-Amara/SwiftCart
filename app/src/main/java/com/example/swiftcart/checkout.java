package com.example.swiftcart;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class checkout extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        ImageView qr = findViewById(R.id.qr);


        if(getSupportActionBar() != null)getSupportActionBar().hide();


        //getting text from input text field.
        String myText = cart.getTotal();
        String cartId = dashboard.getCartId();

        DatabaseReference dbref= FirebaseDatabase.getInstance().getReference().child("customers");
        // we are use add listerner
        // for event listener method
        // which is called with query.
        String time;
        Query query=dbref.child(cartId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // remove the value at reference
                dataSnapshot.getRef().removeValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //initializing MultiFormatWriter for QR code
        MultiFormatWriter mWriter = new MultiFormatWriter();
        try {
            //BitMatrix class to encode entered text and set Width & Height
            BitMatrix mMatrix = mWriter.encode(myText, BarcodeFormat.QR_CODE, 400,400);
            BarcodeEncoder mEncoder = new BarcodeEncoder();
            Bitmap mBitmap = mEncoder.createBitmap(mMatrix);//creating bitmap of code
            qr.setImageBitmap(mBitmap);//Setting generated QR code to imageView
            // to hide the keyboard
            //InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        } catch (WriterException e) {
            e.printStackTrace();
        }

    }
}