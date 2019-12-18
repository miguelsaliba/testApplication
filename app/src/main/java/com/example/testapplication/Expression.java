package com.example.testapplication;

import android.text.TextUtils;

import java.text.DecimalFormat;
import java.util.Arrays;

class Expression {
    private StringBuffer steps = new StringBuffer();

    Expression(String message) {
        simplify(message);
    }

    private String simplify(String message) {

        // remove .0 in doubles
        DecimalFormat format = new DecimalFormat("#.#");
        format.setDecimalSeparatorAlwaysShown(false);

        // convert input into array
        steps.append(message).append("\n");
        System.out.println(message);
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
                    return "error: incorrect bracket placement";
                }
                String[] insideParentheses;

                // if there is only one element inside the parentheses
                if (index1+1 == i){
                    insideParentheses = Arrays.copyOfRange(temp, index1, i);
                } else {
                    insideParentheses = Arrays.copyOfRange(temp, index1 + 1, i);
                }

                String num = simplify(getExpression(insideParentheses));
                if (num.equals("error: dividing by zero")) {
                    steps.append("error: dividing by zero\n");
                    return "error: dividing by zero";
                }
                temp[i] = num;
                temp = removeElement(temp, index1, i-1);
                i = 0;
                System.out.println(getExpression(temp));
                steps.append(getExpression(temp)).append("\n");
            }
        }

        // EXPONENT

        for (int i = 0; i < temp.length; i++) {
            if (temp[i].equals("^")) {
                temp[i+1] = format.format( Math.pow( Double.parseDouble(temp[i-1]),  Double.parseDouble(temp[i+1]) ));
                temp = removeElement(temp, i-1, i);
                i -= 2;
                System.out.println(getExpression(temp));
                steps.append(getExpression(temp)).append("\n");

            }
        }


        // MULTIPLICATION & DIVISION


        for (int i = 0; i < temp.length; i++) {
            if (temp[i].equals("*")) {
                temp[i+1] = format.format(Double.parseDouble(temp[i - 1]) * Double.parseDouble(temp[i + 1]));
                temp = removeElement(temp, i-1, i);
                i -= 2;
                System.out.println(getExpression(temp));
                steps.append(getExpression(temp)).append("\n");


            } else if (temp[i].equals("/")){
                if (temp[i + 1].equals("0")) {
                    steps.append("error: dividing by zero\n");
                    return "error: dividing by zero";
                }
                temp[i+1] = format.format(Double.parseDouble(temp[i - 1]) / Double.parseDouble(temp[i + 1]));
                temp = removeElement(temp, i-1, i);
                i -= 2;
                System.out.println(getExpression(temp));
                steps.append(getExpression(temp)).append("\n");

            }
        }

        // ADDITION & SUBTRACTION

        for (int i = 0; i < temp.length; i++) {
            if (temp[i].equals("+")) {
                temp[i+1] = format.format(Double.parseDouble(temp[i - 1]) + Double.parseDouble(temp[i + 1]));
                temp = removeElement(temp, i-1, i);
                i -= 2;
                System.out.println(getExpression(temp));
                steps.append(getExpression(temp)).append("\n");

            } else if (temp[i].equals("-")){
                temp[i+1] = format.format(Double.parseDouble(temp[i - 1]) - Double.parseDouble(temp[i + 1]));
                temp = removeElement(temp, i-1, i);
                i -= 2;
                System.out.println(getExpression(temp));
                steps.append(getExpression(temp)).append("\n");


            }
        }

        return temp[0];

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
