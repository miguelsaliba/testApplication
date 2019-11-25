package com.example.testapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.util.regex.Pattern;

public class DisplayMessageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        simplify(message);

        // Capture the layout's TextView and set the string as its text
        TextView textView = findViewById(R.id.textView);
        textView.setText(message);
    }

    private void simplify(String message) {
        // Separate + sign

        for (int i = 0; i < message.length(); i++) {
            if (message.charAt(i) == '+'){

            }
        }
        assert message != null;
        String[] parts = message.split("\\+");
        message = String.valueOf(Integer.parseInt(parts[0]) + Integer.parseInt(parts[1]));
    }
}
