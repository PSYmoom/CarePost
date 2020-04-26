package com.carepost;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
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
import java.util.Random;
import android.content.Intent;
import android.widget.Toast;
import java.util.*;
//import javax.mail.*;
//import javax.mail.internet.*;
//import javax.activation.*;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference noteRef = db.document("tenants/Symoom Saad");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button addMore = findViewById(R.id.addMore);
        Button submitButton = findViewById(R.id.submitButton);
        addMore.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                EditText frstName = findViewById(R.id.frstName);
                EditText lastName = findViewById(R.id.lastName);
                EditText aptNum = findViewById(R.id.aptNum);
                TextView success = findViewById(R.id.success);
                TextView infoBox = findViewById(R.id.infoBox);
                String cool = infoBox.getText().toString() + " " + frstName.getText().toString() + " " + lastName.getText().toString() + " " + aptNum.getText().toString();
                infoBox.setText(cool);

                success.setAlpha(1f);
                frstName.setText("");
                lastName.setText("");
                aptNum.setText("");

                Random obj = new Random();
                int randNum  = obj.nextInt(0xffffff + 1);
                String col = String.format("#%06x", randNum);

                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/rfc822");
                i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"maazthegreat13@gmail.com"});
                i.putExtra(Intent.EXTRA_SUBJECT, "subject of email");
                i.putExtra(Intent.EXTRA_TEXT   , "body of email");
                startActivity(Intent.createChooser(i, "Send mail..."));

              /*  Intent p = new Intent(Intent.ACTION_SEND);
                p.setType("message/rfc822");
                p.putExtra(Intent.EXTRA_EMAIL  , new String[]{"saadsymoom@gmail.com"});
                p.putExtra(Intent.EXTRA_SUBJECT, "subject of email");
                p.putExtra(Intent.EXTRA_TEXT   , "body of email");
                startActivity(Intent.createChooser(p, "Send mail..."));*/
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                final TextView infoBox = findViewById(R.id.infoBox);
                DocumentReference tenants = db.document("tenants/Symoom Saad");

                tenants.get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if (documentSnapshot.exists()) {
                                    String title = documentSnapshot.getString("Mail");
                                    String description = documentSnapshot.getString("Name");

                                    //Map<String, Object> note = documentSnapshot.getData();

                                    infoBox.setText("Title: " + title + "\n" + "Description: " + description);
                                } else {
                                    Toast.makeText(MainActivity.this, "Document does not exist", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MainActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                                Log.d(TAG, e.toString());
                            }
                        });
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        db.collection("tenants").whereEqualTo("Name", "Symoom Saad").whereEqualTo("AptNum", 1202)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        String data = "";
                        TextView infoBox = findViewById(R.id.infoBox);
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            String documentId = documentSnapshot.getString("Name");
                            String title = documentSnapshot.getString("Mail");

                            data += "ID: " + documentId
                                    + "\nTitle: " + title + "\n\n";
                        }

                        infoBox.setText(data);
                    }
                });
    }
}
