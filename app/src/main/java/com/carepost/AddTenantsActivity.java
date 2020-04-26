package com.carepost;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Random;

public class AddTenantsActivity extends AppCompatActivity {
    private static final String TAG = "AddTenantsActivity";

    private EditText frstName;
    private EditText lastName;
    private EditText aptNum;
    private EditText emailAdd;
    private TextView success;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference tenantsRef = db.collection("tenants");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tenants);

        frstName = findViewById(R.id.frstName);
        lastName = findViewById(R.id.lastName);
        aptNum = findViewById(R.id.aptNum);
        emailAdd = findViewById(R.id.email);
        success = findViewById(R.id.success);
    }

    public void submitButton(View v) {
        success.setText("");
        String name = frstName.getText().toString().trim() + " " + lastName.getText().toString().trim();
        String email = emailAdd.getText().toString().trim();
        int num;

        try {
            num = Integer.parseInt(aptNum.getText().toString().trim());
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter a valid number", Toast.LENGTH_SHORT).show();
            displaySuccess("FAILURE!");
            return;
        }

        if (name.equals(" ") || email.equals("")) {
            displaySuccess("FAILURE!");
        } else {
            Tenant tenant = new Tenant(num, email, name);
            tenantsRef.add(tenant)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Toast.makeText(getApplicationContext(), "Database Updated!", Toast.LENGTH_SHORT).show();
                            displaySuccess("SUCCESS!");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error adding document", e);
                            Toast.makeText(getApplicationContext(), "Error!", Toast.LENGTH_SHORT).show();
                            displaySuccess("FAILURE!");
                        }
                    });
        }
    }

    private void displaySuccess(String text) {
        if (text.equals("FAILED!")){
            success.setTextColor(Color.parseColor("#c71010"));
        } else {
            Random obj = new Random();
            int randNum = obj.nextInt(0xffffff + 1);
            String col = String.format("#%06x", randNum);
            success.setTextColor(Color.parseColor(col));
        }

        success.setText(text);
    }
}
