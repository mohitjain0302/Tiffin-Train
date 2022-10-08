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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DisplayTheCentreActivity extends AppCompatActivity {

    private TiffinCentre currentCentre;
    private int sum1 = 0;
    private LinearLayout centreMenusListLayout;
    private String tiffinCentreUId;
    private ArrayList<String> menuUIds;
    private TiffinCentre currentTiffinCentre;
    private ImageView add_button;
    private ImageView subtract_button;
    final int UPI_PAYMENT = 0;
    //private TextView number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_the_centre);

        currentCentre = (TiffinCentre) getIntent().getSerializableExtra("centre");
        TextView contact = findViewById(R.id.contact_no_id);
        TextView address = findViewById(R.id.address_id);
        TextView centre = findViewById(R.id.tiffin_centre_name);
        ImageView tiffin_centre_image = findViewById(R.id.tiffin_centre_image);

        centreMenusListLayout = findViewById(R.id.centre_menus_list_layout);

        contact.setText("" + currentCentre.getContactNo());
        address.setText(currentCentre.getAddress());
        centre.setText(currentCentre.getName());
        tiffinCentreUId = currentCentre.getEmail();

        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("Tiffin Centres").document(tiffinCentreUId);
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
                                    View listItemView = vi.inflate(R.layout.menu_list_item_for_customer, null);

                                    if (currentMenu != null) {
                                        TextView menuName = (TextView) listItemView.findViewById(R.id.menu_name_text_view_user);
                                        menuName.setText(currentMenu.getMenuName());

                                        TextView menuRate = (TextView) listItemView.findViewById(R.id.menu_rate_for_customer);
                                        menuRate.setText("Rs " + Integer.toString(currentMenu.getMenuRate()) + "/-");

                                        ArrayList<String> menuItems = currentMenu.getMenuItems();
                                        String str = "";
                                        for (int i = 0; i < menuItems.size(); i++) {
                                            str += menuItems.get(i);
                                            str += " ,";
                                        }

                                        subtract_button = listItemView.findViewById(R.id.subtraction_button);
                                        add_button = listItemView.findViewById(R.id.addition_button);
                                        TextView number = listItemView.findViewById(R.id.number_tobeadded);

                                        add_button.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                String presentcount = number.getText().toString();
                                                int x = Integer.parseInt(presentcount);
                                                x += 1;
                                                sum1 += currentMenu.getMenuRate();
                                                int tempsum = sum1;
                                                String amount = Integer.toString(tempsum);
                                                TextView numb = findViewById(R.id.amount_id);
                                                numb.setText( amount);
                                                number.setText(Integer.toString(x));
                                            }
                                        });

                                             subtract_button.setOnClickListener(new View.OnClickListener() {
                                                 @Override
                                                 public void onClick(View view) {
                                                     String presentcount = number.getText().toString();
                                                     int x = Integer.parseInt(presentcount);
                                                     if(x>0)
                                                     {
                                                         x -= 1;
                                                         sum1 -= currentMenu.getMenuRate();
                                                     }

                                                     int tempsum = sum1;
                                                     String amount = Integer.toString(tempsum);
                                                     TextView numb = findViewById(R.id.amount_id);
                                                     numb.setText(amount);
                                                     number.setText(Integer.toString(x));
                                                 }
                                             });


                                        ImageView img = listItemView.findViewById(R.id.veg_nonveg_type);
                                        String str1 = currentMenu.getType();
                                        String str2 = "NonVeg";

                                        if(str1!= null && str1.equals(str2)){
                                            Log.d(str1,"going for non veg");
                                            img.setImageResource(R.drawable.nonveg);
                                        }
                                        else{
                                            img.setImageResource(R.drawable.veg_symbol);
                                        }

                                        TextView displayItemsTextView = listItemView.findViewById(R.id.display_items_text_view_customer);
                                        displayItemsTextView.setText(str.substring(0, str.length() - 1));

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

        Button payment = findViewById(R.id.pay_button);

        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView numb = findViewById(R.id.amount_id);
                String finalamount = numb.getText().toString();

                Toast.makeText(DisplayTheCentreActivity.this,"paytm username is  : " + currentCentre.getPaytm_username(),Toast.LENGTH_SHORT).show();

                Intent i = new Intent(DisplayTheCentreActivity.this,PaymentActivity.class);
                i.putExtra("Amount1",finalamount);
                i.putExtra("Current_centre",currentCentre);
                startActivity(i);

//                String upiId = "9521766675@paytm";
//                String note = "hi";
//                String name = currentCentre.getName();
//
//                Log.d(finalamount,"The amount is");
//
//                payUsingUpi(finalamount, upiId, name, note);
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

   /* void payUsingUpi(String amount, String upiId, String name, String note) {

        Uri uri = Uri.parse("upi://pay").buildUpon()
                .appendQueryParameter("pa", upiId)
                .appendQueryParameter("pn", name)
                .appendQueryParameter("tn", note)
                .appendQueryParameter("am", amount)
                .appendQueryParameter("cu", "INR")
                .build();


        Intent upiPayIntent = new Intent(Intent.ACTION_VIEW);
        upiPayIntent.setData(uri);

        // will always show a dialog to user to choose an app
        Intent chooser = Intent.createChooser(upiPayIntent, "Pay with");

        // check if intent resolves
        if(null != chooser.resolveActivity(getPackageManager())) {
     //       startActivityForResult(chooser, UPI_PAYMENT);
            activityResultLaunch.launch(chooser);
        } else {
            Toast.makeText(DisplayTheCentreActivity.this,"No UPI app found, please install one to continue",Toast.LENGTH_SHORT).show();
        }

    }
*/
  /*  ActivityResultLauncher<Intent> activityResultLaunch = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {

            switch(UPI_PAYMENT) {
                case UPI_PAYMENT:
                    if ((RESULT_OK == result.getResultCode()) || (result.getResultCode()== 11)) {
                        if (result.getData() != null) {
                            String trxt = result.getData().getStringExtra("response");
                            Log.d("UPI", "onActivityResult: " + trxt);
                            ArrayList<String> dataList = new ArrayList<>();
                            dataList.add(trxt);
                            upiPaymentDataOperation(dataList);
                        } else {
                            Log.d("UPI", "onActivityResult: " + "Return data is null");
                            ArrayList<String> dataList = new ArrayList<>();
                            dataList.add("nothing");
                            upiPaymentDataOperation(dataList);
                        }
                    } else {
                        Log.d("UPI", "onActivityResult: " + "Return data is null"); //when user simply back without payment
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add("nothing");
                        upiPaymentDataOperation(dataList);
                    }
                    break;
            }


        }

});*/


 /*   @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case UPI_PAYMENT:
                if ((RESULT_OK == resultCode) || (resultCode == 11)) {
                    if (data != null) {
                        String trxt = data.getStringExtra("response");
                        Log.d("UPI", "onActivityResult: " + trxt);
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add(trxt);
                        upiPaymentDataOperation(dataList);
                    } else {
                        Log.d("UPI", "onActivityResult: " + "Return data is null");
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add("nothing");
                        upiPaymentDataOperation(dataList);
                    }
                } else {
                    Log.d("UPI", "onActivityResult: " + "Return data is null"); //when user simply back without payment
                    ArrayList<String> dataList = new ArrayList<>();
                    dataList.add("nothing");
                    upiPaymentDataOperation(dataList);
                }
                break;
        }
    }*/

  /*  private void upiPaymentDataOperation(ArrayList<String> data) {
        if (isConnectionAvailable(DisplayTheCentreActivity.this)) {
            String str = data.get(0);
            Log.d("UPIPAY", "upiPaymentDataOperation: "+str);
            String paymentCancel = "";
            if(str == null) str = "discard";
            String status = "";
            String approvalRefNo = "";
            String response[] = str.split("&");
            for (int i = 0; i < response.length; i++) {
                String equalStr[] = response[i].split("=");
                if(equalStr.length >= 2) {
                    if (equalStr[0].toLowerCase().equals("Status".toLowerCase())) {
                        status = equalStr[1].toLowerCase();
                    }
                    else if (equalStr[0].toLowerCase().equals("ApprovalRefNo".toLowerCase()) || equalStr[0].toLowerCase().equals("txnRef".toLowerCase())) {
                        approvalRefNo = equalStr[1];
                    }
                }
                else {
                    paymentCancel = "Payment cancelled by user.";
                }
            }

            if (status.equals("success")) {
                //Code to handle successful transaction here.
                Toast.makeText(DisplayTheCentreActivity.this, "Transaction successful.", Toast.LENGTH_SHORT).show();
                Log.d("UPI", "responseStr: "+approvalRefNo);
            }
            else if("Payment cancelled by user.".equals(paymentCancel)) {
                Toast.makeText(DisplayTheCentreActivity.this, "Payment cancelled by user.", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(DisplayTheCentreActivity.this, "Transaction failed.Please try again", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(DisplayTheCentreActivity.this, "Internet connection is not available. Please check and try again", Toast.LENGTH_SHORT).show();
        }
    }

    public static boolean isConnectionAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()
                    && netInfo.isConnectedOrConnecting()
                    && netInfo.isAvailable()) {
                return true;
            }
        }
        return false;
    }
*/

}