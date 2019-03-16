package com.buyukeryazilim.winpolmobil;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class AdminPage extends AppCompatActivity {

    private FirebaseAuth mAuth;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference myRef;

    EditText emailText;
    EditText passwordText;

    Spinner spinCompanyName;

    Context context = this;

    ArrayList<String> companyNamesFB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_page);

        mAuth = FirebaseAuth.getInstance();

        emailText = findViewById(R.id.emailText);
        passwordText = findViewById(R.id.passwordText);

        spinCompanyName = (Spinner) findViewById(R.id.spinnerCompanyName);

        companyNamesFB = new ArrayList<String>();

        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference();

        getDataFirebase();
    }

    public void signUp (View view) {



        mAuth.createUserWithEmailAndPassword(emailText.getText().toString(), passwordText.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            UUID uuid = UUID.randomUUID();
                            String uuidString = uuid.toString();

                            Toast.makeText(getApplicationContext(),"User Created",Toast.LENGTH_LONG).show();
                            String emailTextStr = emailText.getText().toString().replace("."," ");
                            myRef.child("Companies").child(spinCompanyName.getSelectedItem().toString()).child("users").child(emailTextStr).child("password").setValue(passwordText.getText().toString());
                            //myRef.child("Companies").child(spinCompanyName.getSelectedItem().toString()).child("users").child(emailTextStr).child(uuidString).child("lisans").setValue(uuidString);
                            myRef.child("Companies").child("licences").child(emailTextStr).child("licence").setValue(uuidString);

                            Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                            startActivity(intent);

                        }
                    }
                }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(AdminPage.this,e.getLocalizedMessage().toString(), Toast.LENGTH_LONG).show();

            }
        });

    }

    public void companyButtonOnClick (View view){

        Intent intent = new Intent(getApplicationContext(),NewAddCompanyPage.class);
        startActivity(intent);
    }

    private void getDataFirebase() {

        DatabaseReference newReference = firebaseDatabase.getReference("Companies");
        newReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    if (!ds.getKey().equals("licences")){

                        HashMap<String, Object> hashMap = (HashMap<String, Object>) ds.getValue();
                        companyNamesFB.add((String) hashMap.get("companyName"));
                    }
                }


                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(context  , android.R.layout.simple_spinner_dropdown_item, companyNamesFB);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinCompanyName.setAdapter(dataAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        }
        return super.onKeyDown(keyCode, event);
    }
}
