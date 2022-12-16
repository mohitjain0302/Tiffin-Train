package com.example.tiffintrain;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Objects;

public class DisplayTheCentreActivity extends AppCompatActivity {

    private TiffinCentre currentCentre;
    FirebaseAuth mAuth ;
    private int sum1 = 0;
    private LinearLayout centreMenusListLayout;
    private String tiffinCentreUId;
    private ArrayList<String> menuUIds;
    private TiffinCentre currentTiffinCentre;
    private ImageView add_button;
    private ImageView subtract_button;
    private TextView tiffinQuantity;
    final int UPI_PAYMENT = 0;
    public int addOnChapatiViewId ;
    private int addOnCurdViewId ;
    private int addOnSweetDishViewId ;
    private String userEmail;
    //private TextView number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_the_centre);

        mAuth = FirebaseAuth.getInstance();
        userEmail = Objects.requireNonNull(mAuth.getCurrentUser()).getEmail();
        currentCentre = (TiffinCentre) getIntent().getSerializableExtra("centre");
        TextView contact = findViewById(R.id.contact_no_id);
        TextView address = findViewById(R.id.address_id);
        TextView centre = findViewById(R.id.tiffin_centre_name);
        ImageView tiffin_centre_image = findViewById(R.id.tiffin_centre_image);

        centreMenusListLayout = findViewById(R.id.centre_menus_list_layout);

        tiffinQuantity = findViewById(R.id.tiffin_quantity);
        contact.setText("" + currentCentre.getContactNo());
        address.setText(currentCentre.getAddress());
        centre.setText(currentCentre.getName());
        tiffinCentreUId = currentCentre.getEmail();

        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("Tiffin Centres").document(tiffinCentreUId);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                currentTiffinCentre = documentSnapshot.toObject(TiffinCentre.class);

                if (currentTiffinCentre.getMenuUIds() != null) {
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
                                    View listItemView = vi.inflate(R.layout.menu_list_item_for_customer, null);

                                    if (currentMenu != null) {
                                        TextView menuName = (TextView) listItemView.findViewById(R.id.menu_name_text_view_user);
                                        menuName.setText(currentMenu.getMenuName());

                                        TextView menuRate = (TextView) listItemView.findViewById(R.id.menu_rate_for_customer);
                                        menuRate.setText("Rs " + Integer.toString(currentMenu.getMenuRate()) + "/-");

                                        ArrayList<String> menuItems = currentMenu.getMenuItem();
                                        String str = "";
                                        for (int i = 0; i < menuItems.size(); i++) {
                                            str += menuItems.get(i);
                                            str += " ,";
                                        }


                                        ImageView img = listItemView.findViewById(R.id.veg_nonveg_type);
                                        String str1 = currentMenu.getType();
                                        String str2 = "NonVeg";

                                        if (str1 != null && str1.equals(str2)) {
                                            Log.d(str1, "going for non veg");
                                            img.setImageResource(R.drawable.nonveg);
                                        } else {
                                            img.setImageResource(R.drawable.veg_symbol);
                                        }

                                        TextView displayItemsTextView = listItemView.findViewById(R.id.display_items_text_view_customer);
                                        displayItemsTextView.setText(str.substring(0, str.length() - 1));

                                        listItemView.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(DisplayTheCentreActivity.this);
                                                View bottomSheetView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.bottom_sheet_dialogue_order,
                                                        (LinearLayout) findViewById(R.id.bottomSheetContainerOrder));
                                                Button incrementButton = bottomSheetView.findViewById(R.id.increment_button);
                                                Button decrementButton = bottomSheetView.findViewById(R.id.decrement_button);
                                                CheckBox noneCheckBox = bottomSheetView.findViewById(R.id.check_none);
                                                TextView sevenDayRate = bottomSheetView.findViewById(R.id.seven_day_rate);
                                                TextView fifteenDayRate = bottomSheetView.findViewById(R.id.fifteen_day_rate);
                                                TextView oneMonthRate = bottomSheetView.findViewById(R.id.one_month_rate);
                                                Button placeOrderButton = bottomSheetView.findViewById(R.id.place_order_button);
                                                if (currentMenu.getIsSevenDay())
                                                    sevenDayRate.setText("Rs " + currentMenu.getSevenDayRate());
                                                if (currentMenu.getIsOneMonth())
                                                    oneMonthRate.setText("Rs " + currentMenu.getOneMonthRate());
                                                if (currentMenu.getIsFifteenDay())
                                                    fifteenDayRate.setText("Rs " + currentMenu.getFifteenDayRate());
                                                incrementButton.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
                                                        if(noneCheckBox.isChecked())
                                                        {
                                                            TextView tiffinQuantity = bottomSheetView.findViewById(R.id.tiffin_quantity);
                                                            int curr = Integer.parseInt(tiffinQuantity.getText().toString());
                                                            tiffinQuantity.setText("" + ++curr);
                                                            TextView orderAmount = bottomSheetView.findViewById(R.id.order_amount);
                                                            orderAmount.setText("Rs " + curr * currentMenu.getMenuRate());
                                                        }
                                                        else{
                                                            Toast.makeText(DisplayTheCentreActivity.this , "Please select the None option for Ordering now", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                                decrementButton.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
                                                        if(noneCheckBox.isChecked())
                                                        {
                                                            TextView tiffinQuantity = bottomSheetView.findViewById(R.id.tiffin_quantity);
                                                            int curr = Integer.parseInt(tiffinQuantity.getText().toString());
                                                            if (curr != 0) {
                                                                tiffinQuantity.setText("" + --curr);
                                                                TextView orderAmount = bottomSheetView.findViewById(R.id.order_amount);
                                                                orderAmount.setText("Rs " + curr * currentMenu.getMenuRate());
                                                            }
                                                        }
                                                        else{
                                                            Toast.makeText(DisplayTheCentreActivity.this , "Please select the None option for Ordering now", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });

                                                placeOrderButton.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {

                                                        CheckBox checkSeven = bottomSheetView.findViewById(R.id.check_sevenDay);
                                                        CheckBox checkFifteenDay = bottomSheetView.findViewById(R.id.check_fifteenDay);
                                                        CheckBox checkOneMonth = bottomSheetView.findViewById(R.id.check_oneMonth);
                                                        CheckBox checkNone = bottomSheetView.findViewById(R.id.check_none);
                                                        int noOfTiffins = 0;

                                                        short isSeven = 0, isFifteen = 0, isOneMonth = 0, isNone = 0;
                                                        if (checkSeven.isChecked())
                                                            isSeven = 1;
                                                        if (checkFifteenDay.isChecked())
                                                            isFifteen = 1;
                                                        if (checkOneMonth.isChecked())
                                                            isOneMonth = 1;
                                                        if (checkNone.isChecked())
                                                            isNone = 1;

                                                        if (isSeven + isFifteen + isOneMonth + isNone == 0)
                                                            Toast.makeText(DisplayTheCentreActivity.this, "Please select one option", Toast.LENGTH_SHORT).show();
                                                        else if (isSeven + isFifteen + isOneMonth + isNone == 1) {

                                                            String finalAmount = "";

                                                            int subscriptionPlan = 0 ;
                                                            boolean areAddOns = false ;
                                                            int noOfChapatis = 0;
                                                            int noOfCurds = 0;
                                                            int noOfSweetDishes = 0;
                                                            if (checkSeven.isChecked()) {
                                                                TextView amount = bottomSheetView.findViewById(R.id.seven_day_rate);
                                                                finalAmount += amount.getText().toString();
                                                                subscriptionPlan = 7;
                                                            } else if (checkFifteenDay.isChecked()) {
                                                                TextView amount = bottomSheetView.findViewById(R.id.fifteen_day_rate);
                                                                finalAmount += amount.getText().toString();
                                                                subscriptionPlan = 15;
                                                            } else if (checkOneMonth.isChecked()) {
                                                                TextView amount = bottomSheetView.findViewById(R.id.one_month_rate);
                                                                finalAmount += amount.getText().toString();
                                                                subscriptionPlan = 30;
                                                            } else if (checkNone.isChecked()) {
                                                                TextView amount = bottomSheetView.findViewById(R.id.order_amount);
                                                                finalAmount += amount.getText().toString();
                                                                subscriptionPlan = 0;
                                                                TextView v = bottomSheetView.findViewById(R.id.tiffin_quantity) ;
                                                                noOfTiffins = Integer.parseInt(v.getText().toString());
                                                                if(currentCentre.getIsChapatiAddOn()){
                                                                    TextView t = findViewById(addOnChapatiViewId).findViewById(R.id.addon_quantity_textview);
                                                                    noOfChapatis = Integer.parseInt(t.getText().toString());
                                                                }
                                                                if(currentCentre.getIsSweetdishAddOn()){
                                                                    TextView t = findViewById(addOnSweetDishViewId).findViewById(R.id.addon_quantity_textview);
                                                                    noOfSweetDishes = Integer.parseInt(t.getText().toString());
                                                                }
                                                                if(currentCentre.getIsCurdAddOn()){
                                                                    TextView t = findViewById(addOnCurdViewId).findViewById(R.id.addon_quantity_textview);
                                                                    noOfCurds = Integer.parseInt(t.getText().toString());
                                                                }

                                                            }


                                                            EditText deliveryAddressField = bottomSheetView.findViewById(R.id.delivery_address_field);
                                                            String deliveryAddress = deliveryAddressField.getText().toString();
                                                            if(Integer.parseInt(finalAmount.substring(3))>0  && deliveryAddress != null) {
                                                                Intent intent = new Intent(DisplayTheCentreActivity.this, PaymentActivity.class);
                                                                intent.putExtra("Amount1", finalAmount);
                                                                intent.putExtra("Current_centre", currentCentre);
                                                                areAddOns = (noOfChapatis + noOfCurds + noOfSweetDishes)>0 ;
                                                                OnOrderDetails onOrderDetails = new OnOrderDetails(currentCentre.getEmail() , userEmail , currentMenuUId , subscriptionPlan , areAddOns , noOfTiffins , noOfChapatis , noOfCurds , noOfSweetDishes , deliveryAddress);
                                                                intent.putExtra("OnOrderDetails" , (Parcelable) onOrderDetails) ;
                                                                startActivity(intent);
                                                            }
                                                            else
                                                                Toast.makeText(DisplayTheCentreActivity.this , "Your tiffin is empty or Enter a valid address" , Toast.LENGTH_SHORT).show();
                                                        }
                                                        else
                                                            Toast.makeText(DisplayTheCentreActivity.this , "Please select only one option" , Toast.LENGTH_SHORT).show();
                                                    }

                                                });

                                                LinearLayout addOnsParentLayout = bottomSheetView.findViewById(R.id.addons_parent_layout) ;

                                                if(currentTiffinCentre.getIsChapatiAddOn()){
                                                    View child = getLayoutInflater().inflate(R.layout.addon_list_item , null);

                                                    addOnChapatiViewId = View.generateViewId();
                                                    child.setId(addOnChapatiViewId);
//                                                    addOnChapatiViewId = child.getId() ;
                                                    TextView addonName = child.findViewById(R.id.addon_name_textview) ;
                                                    addonName.setText("Chapati");
                                                    TextView addonQuantity = child.findViewById(R.id.addon_quantity_textview) ;
                                                    Button incButton = child.findViewById(R.id.increment_addon_button);
                                                    Button decButton = child.findViewById(R.id.decrement_addon_button);
                                                    incButton.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View view) {
                                                            if (noneCheckBox.isChecked()) {
                                                                addonQuantity.setText("" + (Integer.parseInt(addonQuantity.getText().toString()) + 1));
                                                                TextView orderAmount = bottomSheetView.findViewById(R.id.order_amount);
                                                                orderAmount.setText("Rs " + (Integer.parseInt(orderAmount.getText().toString().substring(3)) + currentTiffinCentre.getChapatiRate()));
                                                            }
                                                            else{
                                                                Toast.makeText(DisplayTheCentreActivity.this , "Please select the None option for Ordering now", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });
                                                    decButton.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View view) {
                                                            if(noneCheckBox.isChecked()) {
                                                                if (Integer.parseInt(addonQuantity.getText().toString()) != 0) {
                                                                    addonQuantity.setText("" + (Integer.parseInt(addonQuantity.getText().toString()) - 1));
                                                                    TextView orderAmount = bottomSheetView.findViewById(R.id.order_amount);
                                                                    orderAmount.setText("Rs " + (Integer.parseInt(orderAmount.getText().toString().substring(3)) - currentTiffinCentre.getChapatiRate()));
                                                                }
                                                                else{
                                                                    Toast.makeText(DisplayTheCentreActivity.this , "Please select the None option for Ordering now", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        }
                                                    });
                                                    addOnsParentLayout.addView(child);
                                                }

                                                if(currentTiffinCentre.getIsSweetdishAddOn()){
                                                    View child = getLayoutInflater().inflate(R.layout.addon_list_item , null);
                                                    child.setId(View.generateViewId());
                                                    addOnSweetDishViewId = child.getId() ;
                                                    TextView addonName = child.findViewById(R.id.addon_name_textview) ;
                                                    addonName.setText("Sweet Dish");
                                                    TextView addonQuantity = child.findViewById(R.id.addon_quantity_textview) ;
                                                    Button incButton = child.findViewById(R.id.increment_addon_button);
                                                    Button decButton = child.findViewById(R.id.decrement_addon_button);
                                                    incButton.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View view) {
                                                            if (noneCheckBox.isChecked()) {
                                                                addonQuantity.setText("" + (Integer.parseInt(addonQuantity.getText().toString()) + 1));
                                                                TextView orderAmount = bottomSheetView.findViewById(R.id.order_amount);
                                                                orderAmount.setText("Rs " + (Integer.parseInt(orderAmount.getText().toString().substring(3)) + currentTiffinCentre.getSweetdishRate()));
                                                            }
                                                            else{
                                                                Toast.makeText(DisplayTheCentreActivity.this , "Please select the None option for Ordering now", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });
                                                    decButton.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View view) {
                                                            if (noneCheckBox.isChecked()) {
                                                                if (Integer.parseInt(addonQuantity.getText().toString()) != 0) {
                                                                    addonQuantity.setText("" + (Integer.parseInt(addonQuantity.getText().toString()) - 1));
                                                                    TextView orderAmount = bottomSheetView.findViewById(R.id.order_amount);
                                                                    orderAmount.setText("Rs " + (Integer.parseInt(orderAmount.getText().toString().substring(3)) - currentTiffinCentre.getSweetdishRate()));
                                                                }
                                                            }
                                                            else{
                                                                Toast.makeText(DisplayTheCentreActivity.this , "Please select the None option for Ordering now", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });
                                                    addOnsParentLayout.addView(child);
                                                }
                                                if(currentTiffinCentre.getIsCurdAddOn()){
                                                    View child = getLayoutInflater().inflate(R.layout.addon_list_item , null);
                                                    child.setId(View.generateViewId());
                                                    addOnCurdViewId = child.getId() ;
                                                    TextView addonName = child.findViewById(R.id.addon_name_textview) ;
                                                    addonName.setText("Curd");
                                                    TextView addonQuantity = child.findViewById(R.id.addon_quantity_textview) ;
                                                    Button incButton = child.findViewById(R.id.increment_addon_button);
                                                    Button decButton = child.findViewById(R.id.decrement_addon_button);
                                                    incButton.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View view) {
                                                            if (noneCheckBox.isChecked()) {
                                                                addonQuantity.setText("" + (Integer.parseInt(addonQuantity.getText().toString()) + 1));
                                                                TextView orderAmount = bottomSheetView.findViewById(R.id.order_amount);
                                                                orderAmount.setText("Rs " + (Integer.parseInt(orderAmount.getText().toString().substring(3)) + currentTiffinCentre.getCurdRate()));
                                                            }
                                                            else{
                                                                Toast.makeText(DisplayTheCentreActivity.this , "Please select the None option for Ordering now", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });
                                                    decButton.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View view) {
                                                            if (noneCheckBox.isChecked()) {
                                                                if (Integer.parseInt(addonQuantity.getText().toString()) != 0) {
                                                                    addonQuantity.setText("" + (Integer.parseInt(addonQuantity.getText().toString()) - 1));
                                                                    TextView orderAmount = bottomSheetView.findViewById(R.id.order_amount);
                                                                    orderAmount.setText("Rs " + (Integer.parseInt(orderAmount.getText().toString().substring(3)) - currentTiffinCentre.getCurdRate()));
                                                                }
                                                            }
                                                            else{
                                                                Toast.makeText(DisplayTheCentreActivity.this , "Please select the None option for Ordering now", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });
                                                    addOnsParentLayout.addView(child);
                                                }

                                                bottomSheetDialog.setContentView(bottomSheetView);
                                                bottomSheetDialog.show();

                                            }
                                        });


                                        centreMenusListLayout.addView(listItemView);
                                    }
                                }
                            });

                            if (currentCentre.getMyTiffinCentreImageUrl() != null) {
                                Picasso.with(DisplayTheCentreActivity.this).load(currentCentre.getMyTiffinCentreImageUrl()).fit().centerCrop().into(tiffin_centre_image);
                            }
                        }

                    }
                }
            }
        });

        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + currentCentre.getContactNo()));
                startActivity(intent);
            }
        });




    }

    public void showMap(View view) {
        double latitude = currentCentre.getCentre_latitude();
        double longitude = currentCentre.getCentre_longitude();
        String strUri = "http://maps.google.com/maps?q=loc:" + latitude + "," + longitude + " (" + "Label which you want" + ")";
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(strUri));
        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
        startActivity(intent);
    }

}



