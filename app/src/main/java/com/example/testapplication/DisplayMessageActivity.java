package com.example.testapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat;
import java.util.Arrays;

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
    public static String simplify(String message) {

        DecimalFormat format = new DecimalFormat("#.#");
        format.setDecimalSeparatorAlwaysShown(false);

        String[] temp = message.split("(?<=[()+\\-*/^])|(?=[()+\\-*/^])");
        System.out.println(Arrays.asList(temp).toString().substring(1).replaceFirst("]", "").replace(", ", ""));

        int index1 = -1;
        for (int i = 0; i < temp.length; i++) {
            if (temp[i].equals("(")) {
                index1 = i;
            } else if (temp[i].equals(")")) {
                if (index1 == -1) {
                    return "error: incorrect bracket placement";
                }
                String[] insideParentheses;
                if (index1+1 == i){
                    insideParentheses = Arrays.copyOfRange(temp, index1, i);
                } else {
                    insideParentheses = Arrays.copyOfRange(temp, index1 + 1, i);
                }
                String num = simplify(TextUtils.join("", Arrays.asList(insideParentheses)));
                temp[i] = num;
                temp = removeElement(temp, index1, i-1);
                i = 0;
                System.out.println(Arrays.asList(temp).toString().substring(1).replaceFirst("]", "").replace(", ", ""));
            }
        }

        for (int i = 0; i < temp.length; i++) {
            if (temp[i].equals("^")) {
                temp[i+1] = format.format( Math.pow( Double.parseDouble(temp[i-1]),  Double.parseDouble(temp[i+1]) ));
                temp = removeElement(temp, i-1, i);
                i -= 2;
                System.out.println(Arrays.asList(temp).toString().substring(1).replaceFirst("]", "").replace(", ", ""));

            }
        }

        for (int i = 0; i < temp.length; i++) {
            if (temp[i].equals("*")) {
                temp[i+1] = format.format(Double.parseDouble(temp[i - 1]) * Double.parseDouble(temp[i + 1]));
                temp = removeElement(temp, i-1, i);
                i -= 2;
                System.out.println(Arrays.asList(temp).toString().substring(1).replaceFirst("]", "").replace(", ", ""));

            } else if (temp[i].equals("/")){
                if (temp[i + 1].equals("0")) {
                    return "error: dividing by zero";
                }
                temp[i+1] = format.format(Double.parseDouble(temp[i - 1]) / Double.parseDouble(temp[i + 1]));
                temp = removeElement(temp, i-1, i);
                i -= 2;
                System.out.println(Arrays.asList(temp).toString().substring(1).replaceFirst("]", "").replace(", ", ""));

            }
        }

        for (int i = 0; i < temp.length; i++) {
            if (temp[i].equals("+")) {
                temp[i+1] = format.format(Double.parseDouble(temp[i - 1]) + Double.parseDouble(temp[i + 1]));
                temp = removeElement(temp, i-1, i);
                i -= 2;
                System.out.println(Arrays.asList(temp).toString().substring(1).replaceFirst("]", "").replace(", ", ""));

            } else if (temp[i].equals("-")){
                temp[i+1] = format.format(Double.parseDouble(temp[i - 1]) - Double.parseDouble(temp[i + 1]));
                temp = removeElement(temp, i-1, i);
                i -= 2;
                System.out.println(Arrays.asList(temp).toString().substring(1).replaceFirst("]", "").replace(", ", ""));

            }
        }

        return temp[0];

        // Gets the index of the last iteration of each operator
        /*
        int lastMul = message.lastIndexOf("*");
        int lastDiv = message.lastIndexOf("/");
        int lastSub = message.lastIndexOf("-");
        int lastAdd = message.lastIndexOf("+");

        double num;

        // If the last operator is multiplication
        if (lastMul > lastDiv && lastMul > lastSub && lastMul > lastAdd){
            // num is the last number in the input string
            num = Double.parseDouble(message.substring(lastMul+1));
            // removes the last number and operator from the string
            message = message.substring(0,lastMul);

            // MAGIC
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
            return message;
        }
        */
    }
    private static String[] removeElement(String[] theArray, int firstIndex, int lastIndex){
        // if the index is not in the array or the array has no elements it returns the array
        if (theArray == null || firstIndex < 0 || lastIndex < firstIndex || lastIndex >= theArray.length) {
            return theArray;
        }

        String[] newArray = new String[theArray.length-(lastIndex-firstIndex)-1];

        for (int i = 0, k = 0; i < theArray.length; i++) {
            if (i <= lastIndex && i >= firstIndex) {
                continue;
            }
            newArray[k] = theArray[i];
            k++;
        }
        return newArray;
    }

}
