package com.buyukeryazilim.winpolmobil;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    String licenceDb;

    private FirebaseAuth mAuth;

    EditText emailText;
    EditText passwordText;

    CheckBox chBxLogin;

    Context context;

    SharedPreferenc sharedPreferenc;

    FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        emailText = findViewById(R.id.emailText);
        passwordText = findViewById(R.id.passwordText);

        chBxLogin = findViewById(R.id.checkBoxLogin);

        context = this;

        firebaseDatabase = FirebaseDatabase.getInstance();

        sharedPreferenc = new SharedPreferenc();

        if (sharedPreferenc.getValueBoolean(context, "checkBox")) {

            emailText.setText(sharedPreferenc.getValue(context, "email"));
            passwordText.setText(sharedPreferenc.getValue(context, "password"));
            chBxLogin.setChecked(true);
        }
    }

    public void signIn(View view) {

        if (emailText.getText().toString().equals("admin@buyukeryazilim.com") && passwordText.getText().toString().equals("buyuker")) {
            Intent intent = new Intent(getApplicationContext(), LicencePage.class);
            startActivity(intent);
        } else {

            try {

                mAuth.signInWithEmailAndPassword(emailText.getText().toString(), passwordText.getText().toString())
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {

                                    FirebaseUser user = mAuth.getCurrentUser();

                                    /*String licence = sharedPreferenc.getValue(context, "licence");

                                    Intent intent = new Intent(getApplicationContext(), QueryPageActivity.class);
                                    startActivity(intent);*/

                                    getDataFirebase();

                                }
                            }
                        }).addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(getApplicationContext(), e.getLocalizedMessage().toString(), Toast.LENGTH_LONG).show();

                    }
                });

            } catch (Exception e) {

                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();

            }

            if (chBxLogin.isChecked()) {

                sharedPreferenc.save(context, "email", emailText.getText().toString());
                sharedPreferenc.save(context, "password", passwordText.getText().toString());
                sharedPreferenc.saveBoolean(context, "checkBox", true);

            } else {

                sharedPreferenc.save(context, "email", "");
                sharedPreferenc.save(context, "password", "");
                sharedPreferenc.saveBoolean(context, "checkBox", false);

            }
        }
    }

    public void signUpLoginActivity(View view) {

        Intent intent = new Intent(getApplicationContext(), AdminPage.class);
        startActivity(intent);

    }

    private void getDataFirebase() {


        String emailTextStr = emailText.getText().toString().replace(".", " ");

        DatabaseReference newReference = firebaseDatabase.getReference("Companies").child("licences").child(emailTextStr).child("licence");
        newReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                System.out.println("dataSnapshot login= " + dataSnapshot.getValue());

                licenceDb = dataSnapshot.getValue().toString();

                String licence = sharedPreferenc.getValue(context, "licence");

                if (licenceDb.equals(licence)) {
                    //System.out.println("dataSnapshot true= ", licenceDb.equals(licence));

                    Intent intent = new Intent(getApplicationContext(), QueryPageActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(context, "Lütfen Lisansınız İçin Winpol Bilişim ile Görüşünüz.", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
