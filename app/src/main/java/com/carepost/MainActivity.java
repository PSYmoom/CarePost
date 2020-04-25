package com.carepost;
import java.util.ArrayList;
import java.util.List;
import androidx.appcompat.app.AppCompatActivity;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.util.Random;
import java.lang.Object;
import java.lang.Object;

public class MainActivity extends AppCompatActivity {

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
                success.setTextColor(Color.parseColor(col));

                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/rfc822");
                i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"maazthegreat13@gmail.com"});
                i.putExtra(Intent.EXTRA_SUBJECT, "subject of email");
                i.putExtra(Intent.EXTRA_TEXT   , "body of email");
                try {
                    startActivity(Intent.createChooser(i, "Send mail..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(MyActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
