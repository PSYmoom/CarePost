package com.carepost;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class LoginPage extends AppCompatActivity {
    private EditText username;
    private EditText password;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference adminsRef = db.collection("admins");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
    }

    public void loginButton(View v) {
        String user = username.getText().toString();
        String pass = password.getText().toString();

        adminsRef.whereEqualTo("Username", user)
                .whereEqualTo("Password", pass)
                .limit(1)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            Toast.makeText(getApplicationContext(), "Success!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginPage.this, AddTenantsActivity.class));
                        } else {
                            Toast.makeText(getApplicationContext(), "Invalid username or password!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        username.setText("");
        password.setText("");
    }

    public void mailManButton(View v) {
        startActivity(new Intent(this, MainActivity.class));
    }
}
