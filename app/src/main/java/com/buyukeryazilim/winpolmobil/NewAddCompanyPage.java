package com.buyukeryazilim.winpolmobil;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NewAddCompanyPage extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference myRef;

    EditText eTextCompanyName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_add_company_page);

        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference();

        eTextCompanyName = findViewById(R.id.editTextCompanyName);
    }

    public void addCompanyButtonOnClick(View view) {

        myRef.child("Companies").child(eTextCompanyName.getText().toString()).child("companyName").setValue(eTextCompanyName.getText().toString());

        Toast.makeText(getApplicationContext(), "Firma Başarılı Bir Şekilde Kayıt Edildi", Toast.LENGTH_LONG).show();

        Intent intent = new Intent(getApplicationContext(), NewAddCompanyPage.class);
        startActivity(intent);

    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        }
        return super.onKeyDown(keyCode, event);
    }
}
