package com.example.tiffintrain;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class ViewAndEditMenuActivity extends AppCompatActivity {

    private LinearLayout centreManagementHomeButton ;
    private LinearLayout centreManagementMenuButton ;
    private LinearLayout centreManagementOrdersButton ;
    private LinearLayout centreManagementTransactionsButton;
    private LinearLayout menuListLayout ;
    private String currentUserEmail ;
    private ArrayList<String> menuUIds ;
    private TiffinCentre currentTiffinCentre ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_and_edit_menu);

        Intent intent = getIntent();
        currentUserEmail = intent.getStringExtra("key_current_user_email");

        menuListLayout = findViewById(R.id.menu_list_layout) ;

        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("Tiffin Centres").document(currentUserEmail);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                currentTiffinCentre = documentSnapshot.toObject(TiffinCentre.class);
                if(currentTiffinCentre.getMenuUIds() != null) {
                    if (currentTiffinCentre.getMenuUIds().isEmpty()) {
                        Log.d("Uid", "Going In If");
                    } else {
                        Log.d("Uid", "Going In Else");
                        ArrayList<com.example.tiffintrain.Menu> menus = new ArrayList<>();

                        menus.clear();

                        CollectionReference collectionReference = FirebaseFirestore.getInstance().collection("Menus");
                        Log.d("Uid", "Till here");
                        menuUIds = currentTiffinCentre.getMenuUIds();

                        for (int i = 0; i < menuUIds.size(); i++) {
                            String currentMenuUId = menuUIds.get(i);
                            collectionReference.document(currentMenuUId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    com.example.tiffintrain.Menu currentMenu = documentSnapshot.toObject(com.example.tiffintrain.Menu.class);
                                    LayoutInflater vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                    View listItemView = vi.inflate(R.layout.menu_list_item, null);


                                    if(currentMenu!=null) {
                                        TextView menuName = (TextView) listItemView.findViewById(R.id.menu_name_text_view);
                                        menuName.setText(currentMenu.getMenuName());

                                        TextView menuRate = (TextView) listItemView.findViewById(R.id.menu_rate_text_view);
                                        menuRate.setText("Rs " + Integer.toString(currentMenu.getMenuRate()) + "/-");

                                        ArrayList<String> menuItems = currentMenu.getMenuItem();
                                        String str = "";
                                        for (int i = 0; i < menuItems.size(); i++) {
                                            str += menuItems.get(i);
                                            str += ",";
                                        }

                                        ImageView img = listItemView.findViewById(R.id.veg_nonveg_id);
                                        String str1 = currentMenu.getType();
                                        String str2 = "NonVeg";

                                        if(str1!= null && str1.equals(str2)){
                                            Log.d(str1,"going for non veg");
                                            img.setImageResource(R.drawable.nonveg);
                                        }
                                        else{
                                            img.setImageResource(R.drawable.veg_symbol);
                                        }
                                        TextView displayItemsTextView = listItemView.findViewById(R.id.display_items_text_view);
                                        displayItemsTextView.setText(str.substring(0, str.length() - 1));

                                        ImageView subscriptionButton = listItemView.findViewById(R.id.subsription_button);
                                        if(subscriptionButton != null) {
                                            subscriptionButton.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    Intent intent = new Intent(ViewAndEditMenuActivity.this, SubscriptionsActivity.class);
                                                    intent.putExtra("menuUId", currentMenuUId);
                                                    startActivity(intent);
                                                }
                                            });
                                        }

                                        menuListLayout.addView(listItemView);
                                    }
                                }
                            });
                        }
                    }
                }
            }
        });

        centreManagementMenuButton =  findViewById(R.id.centre_management_menu_button);
        centreManagementMenuButton.setBackgroundColor(Color.parseColor("#F57C00"));
        centreManagementOrdersButton =  findViewById(R.id.centre_management_orders_button);
        centreManagementTransactionsButton = findViewById(R.id.centre_management_transactions_button);
        centreManagementHomeButton = findViewById(R.id.centre_management_home_button);

        centreManagementHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ViewAndEditMenuActivity.this , CentreManagementActivity.class));
                finish();
            }
        });

        centreManagementTransactionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewAndEditMenuActivity.this,CentreManagementTransactionsActivity.class);
                intent.putExtra("key_current_user_email" , currentUserEmail) ;
                startActivity(intent);
                finish();
            }
        });

        centreManagementOrdersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewAndEditMenuActivity.this,CentreManagementOrdersActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.create_menu_option , menu );
        getMenuInflater().inflate(R.menu.add_on_option , menu );
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int item_id = item.getItemId() ;

        if(item_id == R.id.create_menu_button){
            Intent intent = new Intent(ViewAndEditMenuActivity.this , CreateMenuActivity.class);
            intent.putExtra("key_current_user_email" , currentUserEmail) ;
            startActivity(intent);
        }
        if(item_id == R.id.manage_addOns_button){
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(ViewAndEditMenuActivity.this);
            View bottomSheetView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.bottom_sheet_dialogue_add_on,
                    (LinearLayout) findViewById(R.id.bottomSheetContainerAddOn));
            bottomSheetDialog.setContentView(bottomSheetView);
            bottomSheetDialog.show();

            Button saveAddOnsButton = bottomSheetView.findViewById(R.id.save_addons_button);
            saveAddOnsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CheckBox checkChapati = bottomSheetView.findViewById(R.id.check_chapati) ;
                    CheckBox checkSweetdish = bottomSheetView.findViewById(R.id.check_sweetdish) ;
                    CheckBox checkCurd = bottomSheetView.findViewById(R.id.check_curd) ;

                    int chapatiRate ;
                    int sweetdishRate ;
                    int curdRate ;
                    DocumentReference documentReference = FirebaseFirestore.getInstance().collection("Tiffin Centres").document(currentUserEmail);
                    HashMap<String , Object> m = new HashMap<>();

                         EditText chapati = bottomSheetView.findViewById(R.id.chapati_rate);
                         chapatiRate = Integer.parseInt(chapati.getText().toString());
                         m.put("isChapatiAddOn" , checkChapati.isChecked()) ;
                         m.put("chapatiRate" , chapatiRate);

                        EditText sweetdish = bottomSheetView.findViewById(R.id.sweetdish_rate);
                        sweetdishRate = Integer.parseInt(sweetdish.getText().toString());
                        m.put("isSweetdishAddOn" , checkSweetdish.isChecked()) ;
                        m.put("sweetdishRate" , sweetdishRate);

                        EditText curd = bottomSheetView.findViewById(R.id.curd_rate);
                        curdRate = Integer.parseInt(curd.getText().toString());
                        m.put("isCurdAddOn" , checkCurd.isChecked()) ;
                        m.put("curdRate" , curdRate);

                    documentReference.set(m , SetOptions.merge());
                    Toast.makeText(ViewAndEditMenuActivity.this , "Changes Saved",Toast.LENGTH_SHORT).show();
                }
            });
            DocumentReference documentReference = FirebaseFirestore.getInstance().collection("Tiffin Centres").document(currentUserEmail);
            documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    currentTiffinCentre = documentSnapshot.toObject(TiffinCentre.class) ;
                    CheckBox checkChapati = bottomSheetView.findViewById(R.id.check_chapati) ;
                    CheckBox checkSweetdish = bottomSheetView.findViewById(R.id.check_sweetdish) ;
                    CheckBox checkCurd = bottomSheetView.findViewById(R.id.check_curd) ;
                    if(currentTiffinCentre.getIsChapatiAddOn())
                        checkChapati.setChecked(true);
                    if(currentTiffinCentre.getIsSweetdishAddOn())
                        checkSweetdish.setChecked(true);
                    if(currentTiffinCentre.getIsCurdAddOn())
                        checkCurd.setChecked(true);
                    EditText chapati = bottomSheetView.findViewById(R.id.chapati_rate);
                    EditText sweetdish = bottomSheetView.findViewById(R.id.sweetdish_rate);
                    EditText curd = bottomSheetView.findViewById(R.id.curd_rate);
                    chapati.setText("" + currentTiffinCentre.getChapatiRate());
                    sweetdish.setText("" + currentTiffinCentre.getSweetdishRate());
                    curd.setText("" + currentTiffinCentre.getCurdRate());
                }
            });




        }

        return true;
    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(ViewAndEditMenuActivity.this , CentreManagementActivity.class));
        finish();
    }
}