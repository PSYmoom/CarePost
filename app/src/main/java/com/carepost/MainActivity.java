package com.carepost;
import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.MailTo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;
import android.content.Intent;
import android.widget.Toast;
import java.util.*;

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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

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
                            displaySuccess("FAILED!");
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
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        String email = "";
        if(tenantArrayList.size()==1){
            i.putExtra(Intent.EXTRA_EMAIL, new String[]{tenantArrayList.get(0).getMail()});
            i.putExtra(Intent.EXTRA_SUBJECT, "Pick Up Timings for Today");
            email = tenantArrayList.get(0).getName() + " (Apt " + tenantArrayList.get(0).getAptNum() + ")\n Pick up at: 9:00 - 17:00 \n\n";
            i.putExtra(Intent.EXTRA_TEXT, email);
            startActivity(Intent.createChooser(i, "Send mail..."));
        }
        else if(tenantArrayList.size() == 0) {
             infoBox.setText("FAILED!");
        }
        else {
            for (int j = 0; j < tenantArrayList.size(); j++) {
                if (j != 0)
                    email += ",";
                email += tenantArrayList.get(j).getMail();
            }
            i.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
            email = "";
            i.putExtra(Intent.EXTRA_SUBJECT, "Pick Up Timings for Today");
            for (int j = 0; j < tenantArrayList.size(); j++) {
                float startTime = (9 + j * 8 / tenantArrayList.size());
                float startMin;
                float endMin;
                if(j!=0)
                    startMin = (startTime % 1)*3/5 + 6;
                else
                    startMin = (startTime % 1)*3/5;
                if(startMin >= 60){
                    startMin -= 60;
                    startTime += 1;
                }
                if(startMin <= 0){
                    startMin += 60;
                    startTime -= 1;
                }
                float endTime = startTime + 8 / tenantArrayList.size();
                if(j!= tenantArrayList.size()-1)
                    endMin = startMin - 12;
                else
                    endMin = startMin - 6;
                if(endMin <= 0){
                    endMin += 60;
                    endTime -= 1;
                }
                if(endMin >= 60){
                    endMin -= 60;
                    endTime += 1;
                }
                String sStartTime = "";
                String sEndTime = "";
                if(startTime < 10)
                    sStartTime += "0" + (int)startTime + ":";
                else
                    sStartTime += (int)startTime + ":";
                if(startMin < 10)
                    sStartTime += "0" + (int)startMin;
                else
                    sStartTime += (int)startMin;

                if(endTime < 10)
                    sEndTime += "0" + (int)endTime + ":";
                else
                    sEndTime += (int)endTime + ":";
                if(endMin < 10)
                    sEndTime += "0" + (int)endMin;
                else
                    sEndTime += (int)endMin;
                email += tenantArrayList.get(j).getName() + " (Apt " + tenantArrayList.get(j).getAptNum() + ")\n Pick up at: " + sStartTime + " - " + sEndTime + "\n\n";
            }
            i.putExtra(Intent.EXTRA_TEXT, email);
            startActivity(Intent.createChooser(i, "Send mail..."));
        }
    }

    private void displaySuccess(String text) {
        Random obj = new Random();
        int randNum  = obj.nextInt(0xffffff + 1);
        String col = String.format("#%06x", randNum);
        success.setTextColor(Color.parseColor(col));
        success.setText(text);
    }
}
