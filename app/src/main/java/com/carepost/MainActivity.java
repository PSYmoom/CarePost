package com.carepost;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private Button addMore;
    private Button submitButton;
    private EditText frstName;
    private EditText lastName;
    private EditText aptNum;
    private TextView success;
    private TextView infoBox;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference tenantsRef = db.collection("tenants");

    private ArrayList<Tenant> tenantArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addMore = findViewById(R.id.addMore);
        submitButton = findViewById(R.id.submitButton);
        frstName = findViewById(R.id.frstName);
        lastName = findViewById(R.id.lastName);
        aptNum = findViewById(R.id.aptNum);
        success = findViewById(R.id.success);
        infoBox = findViewById(R.id.infoBox);

        tenantArrayList = new ArrayList<Tenant>();
    }

    public void addMoreButton(View v) {
        success.setText("");

        String fullName = frstName.getText().toString() + " " + lastName.getText().toString();
        int num;

        try {
            num = Integer.parseInt(aptNum.getText().toString());
        } catch (NumberFormatException e) {
            num = -1;
        }

        tenantsRef.whereEqualTo("Name", fullName)
                .whereEqualTo("AptNum", num)
                .limit(1)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        String data = "";
                        if (!queryDocumentSnapshots.isEmpty()) {
                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                Tenant tenant = documentSnapshot.toObject(Tenant.class);
                                tenantArrayList.add(tenant);
                                displaySuccess("SUCCESS!");
                            }
                        } else {
                            displaySuccess("FAILURE!");
                        }
                        infoBox.setText(data);
                    }
                });

        frstName.setText("");
        lastName.setText("");
        aptNum.setText("");
    }

    public void submitButton(View v) {
        success.setText("");
        String output = "";

        for (Tenant tenant : tenantArrayList) {
            String documentId = tenant.getName();
            String title = tenant.getMail();
            int description = tenant.getAptNum();

            output += "ID: " + documentId
                    + "\nTitle: " + title + "\nDescription: " + description + "\n\n";
        }

        infoBox.setText(output);
    }

    private void displaySuccess(String text) {
        Random obj = new Random();
        int randNum  = obj.nextInt(0xffffff + 1);
        String col = String.format("#%06x", randNum);
        success.setTextColor(Color.parseColor(col));
        success.setText(text);
    }
}
