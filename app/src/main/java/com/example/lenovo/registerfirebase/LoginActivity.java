package com.example.lenovo.registerfirebase;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import static android.view.View.VISIBLE;

public class LoginActivity extends AppCompatActivity {

    EditText loginEmail,loginPassword;
    Button loginUserButtonTAB;
    Button loginUser;

    private String TAG ="LoginActivity";

    private FirebaseAuth mAuth;
    private DatabaseReference firebaseDatabase,firebaseDatabase1;
    Query query;
    LinearLayout loginUserSection;

    public static ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth    =    FirebaseAuth.getInstance();
        firebaseDatabase    =   FirebaseDatabase.getInstance().getReference().child("Users");

        firebaseDatabase.keepSynced(true);
        loginEmail  =   (EditText) findViewById(R.id.login_email);
        loginPassword   =   (EditText) findViewById(R.id.login_password);

        loginUser       =   (Button) findViewById(R.id.login_user_button);
        loginUserButtonTAB  =   (Button) findViewById(R.id.login_user);
        loginUserSection    =   (LinearLayout) findViewById(R.id.login_user_section);


        loginUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                loginUserSection.setVisibility(VISIBLE);

                loginUser.setBackgroundColor(Color.parseColor("#5ba8b7"));
                loginUser.setTextColor(Color.WHITE);


                progressDialog = new ProgressDialog(LoginActivity.this);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setTitle("Please Wait");
                progressDialog.setMessage("Logging into your account...");
                progressDialog.setIndeterminate(true);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

                String lEmail = loginEmail.getText().toString();
                String lpassword = loginPassword.getText().toString();
                mAuth.signInWithEmailAndPassword(lEmail,lpassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            checkUserExist();

                        }
                        else
                        {
                            Toast.makeText(LoginActivity.this,"Your credentials are wrong",Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });



        loginUserButtonTAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUserSection.setVisibility(VISIBLE);

                loginUserButtonTAB.setBackgroundColor(Color.parseColor("#5ba8b7"));
                loginUserButtonTAB.setTextColor(Color.WHITE);


            }
        });


    }

    private void checkUserExist()
    {
        final String usrid = mAuth.getCurrentUser().getUid();
        Log.i(TAG,"userid id"+usrid);

        firebaseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Log.i(TAG,"snap shot"+dataSnapshot);
                if(dataSnapshot.hasChild(usrid))
                {
                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
  }
                else
                {
                    Toast.makeText(LoginActivity.this,"You need to setup your acccount",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
