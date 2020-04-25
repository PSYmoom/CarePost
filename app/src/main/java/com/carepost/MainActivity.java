package com.carepost;
import java.util.ArrayList;
import java.util.List;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
                List<String> subList = new ArrayList<>();
                List<List<String>> infoList = new ArrayList<>();

                EditText frstName = findViewById(R.id.frstName);
                EditText lastName = findViewById(R.id.lastName);
                EditText aptNum = findViewById(R.id.aptNum);
                TextView success = findViewById(R.id.success);

                subList.add(frstName.getText().toString());
                subList.add(lastName.getText().toString());
                subList.add(aptNum.getText().toString());
                infoList.add(subList);
                success.setAlpha(0.2f);
                frstName.setText("");
                lastName.setText("");
                aptNum.setText("");
            }
        });
    }
}
