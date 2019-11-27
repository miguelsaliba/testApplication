package com.example.testapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class DisplayMessageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        assert message != null;
        message = simplify(message); // sends user input to simplify method

        // Capture the layout's TextView and set the string as its text
        TextView textView = findViewById(R.id.textView);
        textView.setText(message);
    }

    private String simplify(String message) {

        // Gets the index of the last iteration of each operator

        int lastMult = message.lastIndexOf("*");
        int lastDiv = message.lastIndexOf("/");
        int lastSub = message.lastIndexOf("-");
        int lastAdd = message.lastIndexOf("+");

        double num;

        // If the last operator is multiplication
        if (lastMult > lastDiv && lastMult > lastSub && lastMult > lastAdd){
            // num is the last number in the input string
            num = Double.parseDouble(message.substring(lastMult+1));
            // removes the last number and operator from the string
            message = message.substring(0,lastMult);

            // multiplies the last number by a recursive method so that it goes from left to right
            return String.valueOf(Double.parseDouble(simplify(message)) * num);

        }
        if (lastDiv > lastSub && lastDiv > lastAdd){
            num = Double.parseDouble(message.substring(lastDiv+1));
            message = message.substring(0,lastDiv);

            return String.valueOf(Double.parseDouble(simplify(message)) / num);

        }
        if (lastSub > lastAdd){
            num = Double.parseDouble(message.substring(lastSub+1));
            message = message.substring(0,lastSub);

            return String.valueOf(Double.parseDouble(simplify(message)) - num);

        } else if (lastAdd > lastSub){
            num = Double.parseDouble(message.substring(lastAdd+1));
            message = message.substring(0,lastAdd);

            return String.valueOf(Double.parseDouble(simplify(message)) + num);

        } else {
            // If the input has no operators it returns the number alone
            return message;
        }

        /*
        if (message.indexOf('+') >= 0) {
            String[] parts = message.split("\\+");
            message = String.valueOf(Integer.parseInt(parts[0]) + Integer.parseInt(parts[1]));

        } else if (message.indexOf('-') >= 0) {
            String[] parts = message.split("-");
            message = String.valueOf(Integer.parseInt(parts[0]) - Integer.parseInt(parts[1]));

        } else if (message.indexOf('/') >= 0) {
            String[] parts = message.split("/");
            message = String.valueOf(Integer.parseInt(parts[0]) / Integer.parseInt(parts[1]));

        } else if (message.indexOf('*') >= 0) {
            String[] parts = message.split("\\*");
            message = String.valueOf(Integer.parseInt(parts[0]) * Integer.parseInt(parts[1]));

        }

         */
    }
}
