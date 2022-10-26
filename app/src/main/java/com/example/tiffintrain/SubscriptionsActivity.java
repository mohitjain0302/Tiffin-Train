package com.example.tiffintrain;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Set;

public class SubscriptionsActivity extends AppCompatActivity {

    private String currentMenuUId;
    private EditText sevenDayPrice;
    private EditText fifteenDayPrice;
    private EditText oneMonthPrice;
    private Button saveSubscription;
    private CheckBox sevenDayBox;
    private CheckBox fifteenDayBox;
    private CheckBox oneMonthBox;
    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscriptions);

        Intent intent = getIntent();
        currentMenuUId = intent.getStringExtra("menuUId");

        sevenDayPrice = findViewById(R.id.seven_day_price);
        fifteenDayPrice = findViewById(R.id.fifteen_day_price);
        oneMonthPrice = findViewById(R.id.one_month_price);
        saveSubscription = findViewById(R.id.save_subscription_button);

        sevenDayBox = findViewById(R.id.seven_day_checkbox);
        fifteenDayBox = findViewById(R.id.fifteen_day_checkbox);
        oneMonthBox = findViewById(R.id.one_month_checkbox);

        DocumentReference docref = FirebaseFirestore.getInstance().collection("Menus").document(currentMenuUId);

        docref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                menu = documentSnapshot.toObject(Menu.class);
//                Log.e("hi1", "BHai  aa gye : " + centre.getEmail() + centre.getSevenDay());

                if(menu != null) {

//                    Log.e("hi", "BHai bahar aa gye : " + centre.getSevenDay());

                    if (menu.getIsSevenDay() == true) {

//                        Log.e("hi", "BHai andar aa gye : ");
                        sevenDayBox.setChecked(true);
                    //    sevenDayPrice.setText("" + centre.getSevenday_price());
                    }

                        sevenDayPrice.setText("" + menu.getSevenDayRate());


                    if (menu.getIsFifteenDay() == true) {
                        fifteenDayBox.setChecked(true);
                      //  fifteenDayPrice.setText("" + centre.getFifteenday_price());
                    }

                        fifteenDayPrice.setText("" + menu.getFifteenDayRate());


                    if (menu.getIsOneMonth() == true) {
                        oneMonthBox.setChecked(true);}
                        oneMonthPrice.setText("" + menu.getOneMonthRate());
                }

            }
        });

        saveSubscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DocumentReference docref = FirebaseFirestore.getInstance().collection("Menus").document(currentMenuUId);

                HashMap<String,Object> m = new HashMap<>();

                    m.put("isSevenDay",sevenDayBox.isChecked());
                    m.put("sevenDayRate",Integer.parseInt(sevenDayPrice.getText().toString()));

                    m.put("isFifteenDay",fifteenDayBox.isChecked());
                    m.put("fifteenDayRate",Integer.parseInt(fifteenDayPrice.getText().toString()));

                    m.put("isOneMonth",oneMonthBox.isChecked());
                    m.put("oneMonthRate",Integer.parseInt(oneMonthPrice.getText().toString()));

                    docref.set(m, SetOptions.merge());
                Toast.makeText(SubscriptionsActivity.this , "Changes saved",Toast.LENGTH_SHORT).show();
            }
        });

    }
}