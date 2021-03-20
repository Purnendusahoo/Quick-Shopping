package com.example.quickshopping;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Signup extends AppCompatActivity {
    EditText Name, Phone,Email,Password,Confirm_password;
    Button reg_btn;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;

    private static final String TAG = "EmailPassword";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_signup);
        TextView login=findViewById(R.id.firstLogin);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Signup.this.finish();
            }
        });

        reg_btn=(Button) findViewById(R.id.finalSignup);
        Name=(EditText) findViewById(R.id.userName);
        Phone=(EditText) findViewById(R.id.phone);
        Email=(EditText) findViewById(R.id.email);
        Password=(EditText) findViewById(R.id.psw);
        Confirm_password=(EditText) findViewById(R.id.cpsw);

        databaseReference=FirebaseDatabase.getInstance().getReference("users");

        firebaseAuth=FirebaseAuth.getInstance();

        reg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = Name.getText().toString();
                final String phone = Phone.getText().toString();
                final String email = Email.getText().toString();
                String password = Password.getText().toString();
                String confirm_password = Confirm_password.getText().toString();

                if (TextUtils.isEmpty(name)) {
                    Toast.makeText(Signup.this, "Please Enter your Name", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(phone)) {
                    Toast.makeText(Signup.this, "Please Enter Mobile Number", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(Signup.this, "Please Enter Email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(Signup.this, "Please Enter Password", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(confirm_password)) {
                    Toast.makeText(Signup.this, "Please Enter confirm Password", Toast.LENGTH_SHORT).show();
                    return;
                }

                firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(Signup.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information

                                    userDetails information= new userDetails(
                                                    name,
                                                    phone,
                                                    email
                                            );

                                            FirebaseDatabase.getInstance().getReference("users")
                                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                    .setValue(information).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    Toast.makeText(Signup.this,"Your account successfully created",Toast.LENGTH_SHORT).show();
                                                    startActivity(new Intent(getApplicationContext(),dashboardActivity.class));

                                                }
                                            });
                                } else {
                                    // If sign in fails, display a message to the user.
                                  Toast.makeText(Signup.this,"Registration failed",Toast.LENGTH_SHORT).show();
                                }

                                // ...
                            }
                        });
                }
        });
    }
}