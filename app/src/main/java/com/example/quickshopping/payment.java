package com.example.quickshopping;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class payment extends AppCompatActivity {
    EditText Name, card_no,Exp_date,CVV;
    Button payment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        payment=(Button) findViewById(R.id.payment_submit);
        Name=(EditText) findViewById(R.id.cardName);
        card_no=(EditText) findViewById(R.id.cardNo);
        Exp_date=(EditText) findViewById(R.id.expiryDate);
        CVV=(EditText) findViewById(R.id.cvv);

        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = Name.getText().toString();
                final String cardNo = card_no.getText().toString();
                final String expiryDate = Exp_date.getText().toString();
                final String cvv = CVV.getText().toString();

                if (TextUtils.isEmpty(name)) {
                    Toast.makeText(payment.this, "Please Enter your name as on your card", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(cardNo)) {
                    Toast.makeText(payment.this, "Please Enter your 16 digit Card Number", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (cardNo.length()!=16) {
                    Toast.makeText(payment.this, "Please Enter your 16 digit Card Number", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(expiryDate)) {
                    Toast.makeText(payment.this, "Please Enter Expiry month and year (MM/YY)", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(cvv)) {
                    Toast.makeText(payment.this, "Please Enter CVV number", Toast.LENGTH_SHORT).show();
                    return;
                }
                ConfirmOrder();
                Toast.makeText(payment.this,"PAYMENT SUCCESSFUL",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(),dashboardActivity.class));
            }
        });
    }

    private void ConfirmOrder() {
        final String saveCurrentDate,saveCurrentTime;
        Calendar calForDate= Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd,YYYY");
        saveCurrentDate=currentDate.format(calForDate.getTime());
        SimpleDateFormat currentTime= new SimpleDateFormat("HH:MM:SS a");
        saveCurrentTime=currentDate.format(calForDate.getTime());

        final DatabaseReference OrdersRef= FirebaseDatabase.getInstance().getReference().child("Orders");

            
    }
}