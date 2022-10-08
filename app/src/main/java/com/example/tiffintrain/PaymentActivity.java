package com.example.tiffintrain;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class PaymentActivity extends AppCompatActivity {

    TiffinCentre current_centre;
    TextView Amount;
    Button pay_b;
    String amount;
    FirebaseAuth mAuth ;
    TextView upi_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        mAuth = FirebaseAuth.getInstance();

        ActivityCompat.requestPermissions(PaymentActivity.this,new String[]{Manifest.permission.READ_SMS}, PackageManager.PERMISSION_GRANTED);
        Intent i = getIntent();

        amount = i.getStringExtra("Amount1");
        current_centre = (TiffinCentre) i.getSerializableExtra("Current_centre");

        String UpiId = current_centre.getUpi_id();

       // Toast.makeText(PaymentActivity.this,"Cost is : " + cost,Toast.LENGTH_SHORT).show();

        Amount = (TextView) findViewById(R.id.amount_final);
        Amount.setText(amount);

        upi_id = (TextView) findViewById(R.id.upi_id);
        upi_id.setText(UpiId);

        pay_b = (Button) findViewById(R.id.check_pay_button);

        pay_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                read_sms(view);

            }
        });

    }

    public void read_sms(View view){

        String Currentdate = new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault()).format(new Date());

        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy'T'hh:mm:ss");
        String selectedDate = Currentdate+"T"+"00:00:00";

        Date dateStart = null;
        try {
            dateStart = formatter.parse(selectedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String filter = "date>=" + dateStart.getTime();

       // Toast.makeText(PaymentActivity.this,"Todays date is  : " + Currentdate,Toast.LENGTH_SHORT).show();

        Cursor cursor = getContentResolver().query(Uri.parse("content://sms/inbox"),new String[] {"body","address"},filter,null,null);

       // Toast.makeText(PaymentActivity.this,"Msg Count is  : " + cursor.getCount(),Toast.LENGTH_SHORT).show();

        cursor.moveToFirst();

        int flag=0;
        int msgsize = cursor.getCount();
        int i;
        String paytm_user_name = current_centre.getPaytm_username();
      //  Toast.makeText(PaymentActivity.this,"Paytm username is  : " + paytm_user_name,Toast.LENGTH_SHORT).show();

       for(i=0;i<msgsize;i++){

            String address = cursor.getString(1);
            String body = cursor.getString(0);

            if(address.contains("Paytm") && body.contains(paytm_user_name) && body.contains(amount)){
                flag=1;

                Toast.makeText(PaymentActivity.this,"Payment Successful : ",Toast.LENGTH_SHORT).show();

                String user_email = Objects.requireNonNull(mAuth.getCurrentUser()).getEmail();
                String tiffin_center_email = current_centre.getEmail();
                Orders order = new Orders(user_email,tiffin_center_email,amount);

                FirebaseFirestore.getInstance().collection("Orders").add(order).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(PaymentActivity.this,"Added to databse ",Toast.LENGTH_SHORT).show();
                    }
                });

                break;
            }

            cursor.moveToNext();
        }
        if(flag!=1){
            Toast.makeText(PaymentActivity.this,"Please make Payment : ",Toast.LENGTH_SHORT).show();
        }
    }

}