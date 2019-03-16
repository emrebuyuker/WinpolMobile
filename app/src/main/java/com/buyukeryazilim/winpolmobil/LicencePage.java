package com.buyukeryazilim.winpolmobil;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LicencePage extends AppCompatActivity {

    EditText eTextEmail;

    FirebaseDatabase firebaseDatabase;

    ArrayList<String> companyNamesFB;

    ArrayList<String> licenceFB;

    Context context = this;

    SharedPreferenc sharedPreferenc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_licence_page);

        eTextEmail = findViewById(R.id.editTextEmailLicancePage);

        licenceFB = new ArrayList<String>();

        firebaseDatabase = FirebaseDatabase.getInstance();

        companyNamesFB = new ArrayList<String>();

        sharedPreferenc = new SharedPreferenc();

    }

    public void licenceOnClick(View view) {

        getDataFirebase();



    }

    private void getDataFirebase() {
        try{
            String emailTextStr = eTextEmail.getText().toString().replace("."," ");

            DatabaseReference newReference = firebaseDatabase.getReference("Companies").child("licences").child(emailTextStr).child("licence");
            newReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    System.out.println("dataSnapshot licence= " + dataSnapshot.getValue());

                    sharedPreferenc.save(context, "licence", dataSnapshot.getValue().toString());

                    Toast.makeText(context, "Lisans İşlemi Başarılı", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }
}
