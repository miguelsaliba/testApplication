package com.example.testapplication;

import android.text.TextUtils;

import java.text.DecimalFormat;
import java.util.Arrays;

class Expression {
    private StringBuffer steps = new StringBuffer();
    private DecimalFormat format = new DecimalFormat("#.#");

    Expression(String message) {
        // remove .0 in doubles
        format.setDecimalSeparatorAlwaysShown(false);
        simplify(message);
    }

    private void simplify(String message) {


        // convert input into array
        steps.append(message).append("\n");
        String[] temp = message.split("(?<=[()+\\-*/^])|(?=[()+\\-*/^])");

        // PARENTHESES

        int index1 = -1;
        for (int i = 0; i < temp.length; i++) {
            if (temp[i].equals("(")) {
                // gets index of parenthesis
                index1 = i;

            } else if (temp[i].equals(")")) {
                // if ) is before ( or ( does not exist
                if (index1 == -1) {
                    steps.append("error: incorrect bracket placement\n");
                    return;
                }
                String[] insideParentheses;

                // if there is only one element inside the parentheses
                if (index1+1 == i){
                    insideParentheses = Arrays.copyOfRange(temp, index1, i);
                } else {
                    insideParentheses = Arrays.copyOfRange(temp, index1 + 1, i);
                }

                String num = solve(insideParentheses);
                if (num.equals("ERR0")) {
                    steps.append("error: dividing by zero\n");
                    return;
                }
                temp[i] = num;
                temp = removeElement(temp, index1, i-1);
                i = 0;
                steps.append(getExpression(temp)).append("\n");
            }
        }

        temp[0] = solve(temp);
        if (temp[0].equals("ERR0")){
            steps.append("error: dividing by zero\n");
            temp[0] = "error: dividing by zero";
        }
    }

    private String solve(String[] temp){
        for (int i = 0; i < temp.length; i++) {
            if (temp[i].equals("^")) {
               temp[i+1] = format.format( Math.pow( Double.parseDouble(temp[i-1]),  Double.parseDouble(temp[i+1]) ));
               temp = removeElement(temp, i-1, i);
               i -= 2;
             steps.append(getExpression(temp)).append("\n");
            }
        }
        for (int i = 0; i < temp.length; i++) {
            if (temp[i].equals("*")){
                temp[i+1] = format.format(Double.parseDouble(temp[i-1]) * Double.parseDouble(temp[i+1]));
                temp = removeElement(temp, i-1, i);
                i -= 1;
                steps.append(getExpression(temp)).append("\n");
            } else if (temp[i].equals("/")){
                if (temp[i + 1].equals("0")){
                    return "ERR0";
                }
                temp[i+1] = format.format(Double.parseDouble(temp[i-1]) / Double.parseDouble(temp[i+1]));
                temp = removeElement(temp, i-1, i);
                i -= 1;
                steps.append(getExpression(temp)).append("\n");
            }
        }

        for (int i = 0; i < temp.length; i++) {
            if (temp[i].equals("+")){
                temp[i+1] = format.format(Double.parseDouble(temp[i-1]) + Double.parseDouble(temp[i+1]));
                temp = removeElement(temp, i-1, i);
                i -= 1;
                steps.append(getExpression(temp)).append("\n");
            } else if (temp[i].equals("-")){
                temp[i+1] = format.format(Double.parseDouble(temp[i-1]) - Double.parseDouble(temp[i+1]));
                temp = removeElement(temp, i-1, i);
                i -= 1;
                steps.append(getExpression(temp)).append("\n");
            }
        }
        return getExpression(temp);
    }

    String getSteps(){
        return steps.toString();
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

    private static String getExpression(String[] arr) {
        return TextUtils.join("", Arrays.asList(arr));
    }
}
