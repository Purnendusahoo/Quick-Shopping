package com.example.quickshopping;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class viewInventoryActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    RecyclerView mrecyclerview;
    DatabaseReference mdatabaseReference;
    private TextView totalnoofitem, totalnoofsum;
    private int counttotalnoofitem =0;
    int sum=0;
    Button payment;

    String GOOGLE_PAY_PACKAGE_NAME = "com.google.android.apps.nbu.paisa.user";
    int GOOGLE_PAY_REQUEST_CODE = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_inventory);
        totalnoofitem= findViewById(R.id.totalnoitem);
        totalnoofsum = findViewById(R.id.totalsum);
        payment=findViewById(R.id.payment);
        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser users = firebaseAuth.getCurrentUser();
        String finaluser=users.getEmail();
        String resultemail = finaluser.replace(".","");
        mdatabaseReference = FirebaseDatabase.getInstance().getReference("Users").child(resultemail).child("Items");
        mrecyclerview = findViewById(R.id.recyclerViews);
        LinearLayoutManager manager= new LinearLayoutManager(this);
        mrecyclerview.setLayoutManager(manager);
        mrecyclerview.setHasFixedSize(true);
        mrecyclerview.setLayoutManager(new LinearLayoutManager(this));

        mdatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    counttotalnoofitem = (int) dataSnapshot.getChildrenCount();
                    totalnoofitem.setText(Integer.toString(counttotalnoofitem));
                }else{
                    totalnoofitem.setText("0");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        mdatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //int sum=0;
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    Map<String,Object> map = (Map<String, Object>) ds.getValue();
                    Object price = map.get("itemprice");
                    int pValue = Integer.parseInt(String.valueOf(price));
                    sum += pValue;
                    totalnoofsum.setText(String.valueOf(sum));

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });


        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String amount= String.valueOf(sum);
                String name="Purnendu Sahoo";
                String UpiID="purnendusahoo16@okhdfcbank";
                payUsingUpi(amount,name,UpiID);

            }
        });
    }

    private void payUsingUpi(String amount, String name, String upiID) {
        Log.e("main",name+"--up--"+upiID+"--"+amount);
        Uri uri=Uri.parse("upi://pay").buildUpon()
                .appendQueryParameter("pa", upiID)
                .appendQueryParameter("pn", name)
                //.appendQueryParameter("mc", "your-merchant-code")
                .appendQueryParameter("tr", "your-transaction-ref-id")
                .appendQueryParameter("tn", "your-transaction-note")
                .appendQueryParameter("am", amount)
                .appendQueryParameter("cu", "INR")
                .build();
//                .appendQueryParameter("url", "your-transaction-url")

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(uri);
        intent.setPackage(GOOGLE_PAY_PACKAGE_NAME);
        startActivityForResult(intent, GOOGLE_PAY_REQUEST_CODE);
    }

    @Override
    protected  void  onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Items, scanItemsActivity.UsersViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Items, scanItemsActivity.UsersViewHolder>
                (  Items.class,
                        R.layout.list_layout,
                        scanItemsActivity.UsersViewHolder.class,
                        mdatabaseReference )
        {
            @Override
            protected void populateViewHolder(scanItemsActivity.UsersViewHolder viewHolder, Items model, int position){

                viewHolder.setDetails(getApplicationContext(),model.getItembarcode(),model.getItemcategory(),model.getItemname(),model.getItemprice());
            }
        };

        mrecyclerview.setAdapter(firebaseRecyclerAdapter);
    }
}
