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

        // remove .0 in doubles
        DecimalFormat format = new DecimalFormat("#.#");
        format.setDecimalSeparatorAlwaysShown(false);

        // convert input into array
        System.out.println(message);
        String[] temp = message.split("(?<=[()+\\-*/^])|(?=[()+\\-*/^])");

        // PARENTHESES

        int index1 = -1;
        for (int i = 0; i < temp.length; i++) {
            if (temp[i].equals("(")) {
                // gets index of paranthesis
                index1 = i;
                
            } else if (temp[i].equals(")")) {
                // if ) is before ( or ( does not exist
                if (index1 == -1) {
                    return "error: incorrect bracket placement";
                }
                String[] insideParentheses;
                
                // if there is only one element inside the parentheses
                if (index1+1 == i){
                    insideParentheses = Arrays.copyOfRange(temp, index1, i);
                } else {
                    insideParentheses = Arrays.copyOfRange(temp, index1 + 1, i);
                }

                String num = simplify(TextUtils.join("", Arrays.asList(insideParentheses)));
                if (num.equals("error: dividing by zero")) {
                    return "error: dividing by zero";
                }
                temp[i] = num;
                temp = removeElement(temp, index1, i-1);
                i = 0;
                System.out.println(getExpression(temp));
            }
        }

        // EXPONENT

        for (int i = 0; i < temp.length; i++) {
            if (temp[i].equals("^")) {
                temp[i+1] = format.format( Math.pow( Double.parseDouble(temp[i-1]),  Double.parseDouble(temp[i+1]) ));
                temp = removeElement(temp, i-1, i);
                i -= 2;
                System.out.println(getExpression(temp));

            }
        }


        // MULTIPLICATION & DIVISION


        for (int i = 0; i < temp.length; i++) {
            if (temp[i].equals("*")) {
                temp[i+1] = format.format(Double.parseDouble(temp[i - 1]) * Double.parseDouble(temp[i + 1]));
                temp = removeElement(temp, i-1, i);
                i -= 2;
                System.out.println(getExpression(temp));

            } else if (temp[i].equals("/")){
                if (temp[i + 1].equals("0")) {
                    return "error: dividing by zero";
                }
                temp[i+1] = format.format(Double.parseDouble(temp[i - 1]) / Double.parseDouble(temp[i + 1]));
                temp = removeElement(temp, i-1, i);
                i -= 2;
                System.out.println(getExpression(temp));

            }
        }

        // ADDITION & SUBTRACTION

        for (int i = 0; i < temp.length; i++) {
            if (temp[i].equals("+")) {
                temp[i+1] = format.format(Double.parseDouble(temp[i - 1]) + Double.parseDouble(temp[i + 1]));
                temp = removeElement(temp, i-1, i);
                i -= 2;
                System.out.println(getExpression(temp));

            } else if (temp[i].equals("-")){
                temp[i+1] = format.format(Double.parseDouble(temp[i - 1]) - Double.parseDouble(temp[i + 1]));
                temp = removeElement(temp, i-1, i);
                i -= 2;
                System.out.println(getExpression(temp));
            }
        }

        return temp[0];

    }
    private static String[] removeElement(String[] theArray, int firstIndex, int lastIndex){
        // if the index is not in the array or the array has no elements it returns the array
        if (theArray == null || firstIndex < 0 || lastIndex < firstIndex || lastIndex >= theArray.length) {
            return theArray;
        }

        String[] newArray = new String[theArray.length-(lastIndex-firstIndex)-1];

        for (int i = 0, k = 0; i < theArray.length; i++) {
            // skips index if it's inside the range
            if (i <= lastIndex && i >= firstIndex) {
                continue;
            }
            newArray[k] = theArray[i];
            k++;
        }
        return newArray;
    }

    public static String getExpression(String[] arr) {
        return TextUtils.join("", Arrays.asList(arr));
    }
}
