package com.example.tiffintrain;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Set;

public class SubscriptionsActivity extends AppCompatActivity {

    private String currentUserEmail;
    private EditText sevenDayPrice;
    private EditText fifteenDayPrice;
    private EditText oneMonthPrice;
    private Button saveSubscription;
    private CheckBox sevenDayBox;
    private CheckBox fifteenDayBox;
    private CheckBox oneMonthBox;
    private  TiffinCentre centre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscriptions);

        Intent intent = getIntent();
        currentUserEmail = intent.getStringExtra("key_current_user_email");

        sevenDayPrice = findViewById(R.id.seven_day_price);
        fifteenDayPrice = findViewById(R.id.fifteen_day_price);
        oneMonthPrice = findViewById(R.id.one_month_price);
        saveSubscription = findViewById(R.id.save_subscription_button);

        sevenDayBox = findViewById(R.id.seven_day_checkbox);
        fifteenDayBox = findViewById(R.id.fifteen_day_checkbox);
        oneMonthBox = findViewById(R.id.one_month_checkbox);

        DocumentReference docref = FirebaseFirestore.getInstance().collection("Tiffin Centres").document(currentUserEmail);

        docref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                centre = documentSnapshot.toObject(TiffinCentre.class);
                Log.e("hi1", "BHai  aa gye : " + centre.getEmail() + centre.getSevenDay());

                if(centre != null) {

                    Log.e("hi", "BHai bahar aa gye : " + centre.getSevenDay());

                    if (centre.getSevenDay() == true) {

                        Log.e("hi", "BHai andar aa gye : ");
                        sevenDayBox.setChecked(true);
                    //    sevenDayPrice.setText("" + centre.getSevenday_price());
                    }

                        sevenDayPrice.setText("" + centre.getSevenday_price());


                    if (centre.getFifteenDay() == true) {
                        fifteenDayBox.setChecked(true);
                      //  fifteenDayPrice.setText("" + centre.getFifteenday_price());
                    }

                        fifteenDayPrice.setText("" + centre.getFifteenday_price());


                    if (centre.getOneMonth() == true) {
                        oneMonthBox.setChecked(true);}
                        oneMonthPrice.setText("" + centre.getOnemonth_price());


                }

            }
        });

        saveSubscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DocumentReference docref = FirebaseFirestore.getInstance().collection("Tiffin Centres").document(currentUserEmail);

                HashMap<String,Object> m = new HashMap<>();

                    m.put("sevenDay",sevenDayBox.isChecked());
                    m.put("sevenday_price",Integer.parseInt(sevenDayPrice.getText().toString()));

                    m.put("fifteenDay",fifteenDayBox.isChecked());
                    m.put("fifteenday_price",Integer.parseInt(fifteenDayPrice.getText().toString()));

                    m.put("oneMonth",oneMonthBox.isChecked());
                    m.put("onemonth_price",Integer.parseInt(oneMonthPrice.getText().toString()));

                    docref.set(m, SetOptions.merge());
            }
        });

    }
}