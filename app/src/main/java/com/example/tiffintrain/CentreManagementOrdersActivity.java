package com.example.tiffintrain;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.model.Values;
import com.google.firestore.v1.Value;

import java.util.Objects;

public class CentreManagementOrdersActivity extends AppCompatActivity {

    private FirebaseAuth mAuth ;
    private String tiffin_centre_email ;
    private LinearLayout orders_list_layout ;
    private LinearLayout centreManagementHomeButton ;
    private LinearLayout centreManagementMenuButton ;
    private LinearLayout centreManagementOrdersButton ;
    private LinearLayout centreManagementTransactionsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_centre_management_orders);

        mAuth = FirebaseAuth.getInstance();
        tiffin_centre_email = Objects.requireNonNull(mAuth.getCurrentUser()).getEmail();

        orders_list_layout = findViewById(R.id.orders_list_layout) ;

        centreManagementHomeButton =  findViewById(R.id.centre_management_home_button);
        centreManagementOrdersButton =  findViewById(R.id.centre_management_orders_button);
        centreManagementOrdersButton.setBackgroundColor(Color.parseColor("#F57C00"));
        centreManagementMenuButton = findViewById(R.id.centre_management_menu_button);
        centreManagementTransactionsButton = findViewById(R.id.centre_management_transactions_button);

        centreManagementTransactionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CentreManagementOrdersActivity.this,CentreManagementTransactionsActivity.class);
                intent.putExtra("key_current_user_email" , tiffin_centre_email) ;
                startActivity(intent);
                finish();
            }
        });

        centreManagementHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CentreManagementOrdersActivity.this,CentreManagementActivity.class);
                startActivity(intent);
                finish();
            }
        });


        centreManagementMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CentreManagementOrdersActivity.this , ViewAndEditMenuActivity.class);
                intent.putExtra("key_current_user_email" , tiffin_centre_email) ;
                startActivity(intent);
                finish();
            }
        });

        FirebaseFirestore.getInstance().collection("Orders").whereEqualTo("tiffinCentreEmail" , tiffin_centre_email).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for (QueryDocumentSnapshot snapshot1 : value) {
                    OnOrderDetails order = snapshot1.toObject(OnOrderDetails.class);
                    LayoutInflater vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View listItemView = vi.inflate(R.layout.orders_list_item, null);

                    if(order != null)
                    {

                        TextView orderNameTextView = listItemView.findViewById(R.id.order_name) ;
                        ImageView imgView = listItemView.findViewById(R.id.order_type_img) ;
                        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("Menus").document(order.getMenuId()) ;
                        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                            @Override
                            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                                Menu menu = value.toObject(Menu.class) ;

                                if(menu != null)
                                orderNameTextView.setText("Menu : " + menu.getMenuName());
                            }
                        });
                        if(order.getSubscriptionPlan()!=0)
                            imgView.setImageResource(R.drawable.ic_baseline_subscriptions_24);
                        else
                            imgView.setImageResource(R.drawable.ic_baseline_call_received_24);
                        listItemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                AlertDialog.Builder alert = new AlertDialog.Builder(CentreManagementOrdersActivity.this) ;
                                View orderDetailsDialog = getLayoutInflater().inflate(R.layout.order_details_dialogue,null);
                                TextView dialogueMenuName = orderDetailsDialog.findViewById(R.id.dialogue_menu_name) ;
                                Button orderCompletedButton = orderDetailsDialog.findViewById(R.id.order_completed_button);
                                orderCompletedButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        FirebaseFirestore.getInstance().collection("Orders").document(order.getOrderUId()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(CentreManagementOrdersActivity.this , "Order Completed" , Toast.LENGTH_SHORT).show();
                                                view.setVisibility(View.GONE);
                                            }
                                        });
                                    }
                                });
                                DocumentReference documentReference = FirebaseFirestore.getInstance().collection("Menus").document(order.getMenuId()) ;
                                documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                    @Override
                                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                                        Menu menu = value.toObject(Menu.class) ;
                                        dialogueMenuName.setText("Menu : " + menu.getMenuName());
                                    }
                                });

                                TextView deliveryAddress = orderDetailsDialog.findViewById(R.id.dialogue_delivery_address);
                                deliveryAddress.setText("Delivery Address : " + order.getDeliveryAddress());
                                TextView subscriptionPlan = orderDetailsDialog.findViewById(R.id.dialogue_subscription_plan);
                                String subscription = "Subscription Plan : " ;
                                if(order.getSubscriptionPlan() == 0)
                                    subscription += "None" ;
                                else if(order.getSubscriptionPlan() == 7)
                                    subscription += "7 days";
                                else if(order.getSubscriptionPlan() == 15)
                                    subscription += "15 days";
                                else if(order.getSubscriptionPlan() == 30)
                                    subscription += "30 days";
                                subscriptionPlan.setText(subscription);
                                TextView tiffinQuantity = orderDetailsDialog.findViewById(R.id.dialogue_tiffin_quantity) ;
                                if(order.getSubscriptionPlan() == 0)
                                    tiffinQuantity.setText("Tiffin Quantity : " + order.getNoOfTiffins());
                                else
                                    tiffinQuantity.setText("Tiffin Quantity : " + order.getNoOfTiffins());
                                TextView addOnsTextView = orderDetailsDialog.findViewById(R.id.dialogue_add_ons);
                                if(order.getSubscriptionPlan() == 0)
                                addOnsTextView.setText("Add Ons : Chapatis - " + order.getNoOfChapatis() + " , Curd - " + order.getNoOfCurds() + " , Sweet Dish - " + order.getNoOfSweetDishes());
                                else
                                    addOnsTextView.setText("Add Ons : None");
                                alert.setView(orderDetailsDialog);
                                final AlertDialog alertDialog = alert.create();
                                alertDialog.setCanceledOnTouchOutside(true);
                                alertDialog.show();

                            }
                        });

                        orders_list_layout.addView(listItemView);
                    }
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(CentreManagementOrdersActivity.this , CentreManagementActivity.class));
        finish();
    }
}