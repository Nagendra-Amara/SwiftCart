package com.example.swiftcart;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class cartitems extends ArrayAdapter<DataModal>
{

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


        itemname.setText(dataModal.getName());


        Picasso.get().load(dataModal.getImgUrl()).into(img);



        listitemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getContext(), "Item clicked is : " + dataModal.getName(), Toast.LENGTH_SHORT).show();

            }
        });
        return listitemView;
    }
}
