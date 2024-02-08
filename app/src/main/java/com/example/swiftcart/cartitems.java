package com.example.swiftcart;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class cartitems extends ArrayAdapter<DataModal>
{

    FirebaseFirestore db;
    public static HashMap<String,String> prices = new HashMap<>();
    HashMap<String,Integer> itemqnt = new HashMap<>();


    public cartitems(@NonNull Context context, ArrayList<DataModal> dataModalArrayList) {
        super(context, 0, dataModalArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


        View listitemView = convertView;
        if (listitemView == null) {
            listitemView = LayoutInflater.from(getContext()).inflate(R.layout.dummy, parent, false);
        }


        DataModal dataModal = getItem(position);


        TextView itemname = listitemView.findViewById(R.id.itemname);
        ImageView img = listitemView.findViewById(R.id.img);
        TextView price = listitemView.findViewById(R.id.price);
        TextView qnt = listitemView.findViewById(R.id.qnt);



        itemname.setText(dataModal.getName());

        db = FirebaseFirestore.getInstance();
        db.collection("prices")
                .whereEqualTo("name", dataModal.getName())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String,Object> map= document.getData();
                                price.setText((String)map.get("price"));
                                itemqnt.put(dataModal.getName(),itemqnt.getOrDefault(dataModal.getName(),0)+1);
                                prices.put(dataModal.getName(),(String)price.getText());
                            }
                        }
                    }
                });
        //qnt.setText("Qnt :"+Integer.toString(itemqnt.get(dataModal.getName())));
        Picasso.get().load(dataModal.getImgUrl()).into(img);

        


        return listitemView;
    }
}
