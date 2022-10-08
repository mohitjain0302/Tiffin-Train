package com.example.tiffintrain;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class CentreManagementTransactionsActivity extends AppCompatActivity {

    private String currentUserEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_centre_management_transactions);

        Intent intent = getIntent();
        currentUserEmail = intent.getStringExtra("key_current_user_email");

        ArrayList<Orders> orders = new ArrayList<>();

        TransactionAdapter adapter = new TransactionAdapter(this, orders);
        ListView listView = findViewById(R.id.transaction_list);
        listView.setAdapter(adapter);

        orders.clear();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference collectionReference = db.collection("Orders");
        collectionReference.whereEqualTo("tiffin_center_email" , currentUserEmail).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                orders.clear();
                for (QueryDocumentSnapshot snapshot1 : value) {

                    Log.e("hi", "HI HERE : ");
                    Orders order = snapshot1.toObject(Orders.class);

                    orders.add(order);
                }
            }
        });



    }
}