package com.example.tiffintrain;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class TransactionAdapter extends ArrayAdapter<Transactions> {

    private Context mContext ;
    public TransactionAdapter(Activity context, ArrayList<Transactions> orders) {
        super(context, 0, orders);
        mContext = context ;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.transaction_list_item, parent, false);
        }

        Transactions currentTransaction = getItem(position);

        TextView payerName =(TextView) listItemView.findViewById(R.id.payer_name_text_view);
        payerName.setText(currentTransaction.getUser_email());

        TextView Amount = (TextView) listItemView.findViewById(R.id.amount_text_view);
        Log.d("Timestamp Error", "getView: " + currentTransaction.getAmount());
        Amount.setText("" + currentTransaction.getAmount().substring(3));

        Log.d("Timestamp Error", "getView: " + currentTransaction.getTimestamp());
        TextView timeTextView = listItemView.findViewById(R.id.time_text_view);
        timeTextView.setText(currentTransaction.getTimestamp().substring(11,16));
        TextView dateTextView = listItemView.findViewById(R.id.date_text_view);
        dateTextView.setText(currentTransaction.getTimestamp().substring(0,10));

        return listItemView;
    }

}
