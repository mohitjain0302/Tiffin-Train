package com.example.tiffintrain;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
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
    private LinearLayout centreManagementHomeButton ;
    private LinearLayout centreManagementMenuButton ;
    private LinearLayout centreManagementOrdersButton ;
    private LinearLayout centreManagementTransactionsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_centre_management_transactions);

        centreManagementTransactionsButton =  findViewById(R.id.centre_management_transactions_button);
        centreManagementTransactionsButton.setBackgroundColor(Color.parseColor("#F57C00"));

        centreManagementOrdersButton =  findViewById(R.id.centre_management_orders_button);

        centreManagementMenuButton = findViewById(R.id.centre_management_menu_button);


        centreManagementHomeButton = findViewById(R.id.centre_management_home_button);

        centreManagementHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CentreManagementTransactionsActivity.this,CentreManagementActivity.class);
                intent.putExtra("key_current_user_email" , currentUserEmail) ;
                startActivity(intent);
            }
        });

        Intent intent = getIntent();
        currentUserEmail = intent.getStringExtra("key_current_user_email");

        centreManagementOrdersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CentreManagementTransactionsActivity.this,CentreManagementOrdersActivity.class);
                startActivity(intent);
            }
        });


        centreManagementMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CentreManagementTransactionsActivity.this , ViewAndEditMenuActivity.class);
                intent.putExtra("key_current_user_email" , currentUserEmail) ;
                startActivity(intent);
            }
        });
        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.refreshLayout);


        ArrayList<Transactions> orders = new ArrayList<>();

        TransactionAdapter adapter = new TransactionAdapter(this, orders);
        ListView listView = findViewById(R.id.transaction_list);
//        listView.setAdapter(adapter);

        orders.clear();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference collectionReference = db.collection("Transactions");
        collectionReference.whereEqualTo("tiffin_center_email" , currentUserEmail).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                orders.clear();
                for (QueryDocumentSnapshot snapshot1 : value) {

                    Log.e("hi", "HI HERE : ");
                    Transactions order = snapshot1.toObject(Transactions.class);

                    orders.add(order);
                }
            }
        });

        listView.setAdapter(adapter);

    }
}