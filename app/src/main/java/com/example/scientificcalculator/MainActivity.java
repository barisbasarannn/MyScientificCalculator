package com.example.scientificcalculator;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import org.mariuszgromada.math.mxparser.Expression;

public class MainActivity extends AppCompatActivity {

    private TextView previousCalculation;
    private EditText display;
    private CalculationViewModel calculationViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        previousCalculation = findViewById(R.id.previousCalculationView);
        display = findViewById(R.id.displayEditText);

        display.setShowSoftInputOnFocus(false);

        calculationViewModel = new ViewModelProvider(this).get(CalculationViewModel.class);

        calculationViewModel.getAllCalculations().observe(this, calculationEntities -> {

        });
    }

    private boolean isDegreeMode = true;

    private void updateText(String strToAdd) {
        String oldStr = display.getText().toString();
        int cursorPos = display.getSelectionStart();

        if (isLastCharacterOperator(oldStr) && strToAdd.matches("(sin|cos|tan|arcsin|arccos|arctan)\\(")) {
            display.setText(String.format("%s%s", oldStr, strToAdd));
            display.setSelection(cursorPos + strToAdd.length());
        } else {
            if (!strToAdd.matches("[0-9]") && isLastCharacterOperator(oldStr)) {
                String updatedStr = oldStr.substring(0, oldStr.length() - 1);
                display.setText(String.format("%s%s", updatedStr, strToAdd));
                display.setSelection(cursorPos);
            } else {
                String leftStr = oldStr.substring(0, cursorPos);
                String rightStr = oldStr.substring(cursorPos);

                display.setText(String.format("%s%s%s", leftStr, strToAdd, rightStr));
                display.setSelection(cursorPos + strToAdd.length());
            }
        }
    }


    private boolean isLastCharacterOperator(String text) {
        return !text.isEmpty() && "+-×÷".contains(String.valueOf(text.charAt(text.length() - 1)));
    }


    public void zeroBTNPush(View view){
        updateText(getResources().getString(R.string.zeroText));
    }

    public void oneBTNPush(View view){
        updateText(getResources().getString(R.string.oneText));
    }

    public void twoBTNPush(View view){
        updateText(getResources().getString(R.string.twoText));
    }

    public void threeBTNPush(View view){
        updateText(getResources().getString(R.string.threeText));
    }

    public void fourBTNPush(View view){
        updateText(getResources().getString(R.string.fourText));
    }

    public void fiveBTNPush(View view){
        updateText(getResources().getString(R.string.fiveText));
    }

    public void sixBTNPush(View view){
        updateText(getResources().getString(R.string.sixText));
    }

    public void sevenBTNPush(View view){
        updateText(getResources().getString(R.string.sevenText));
    }

    public void eightBTNPush(View view){
        updateText(getResources().getString(R.string.eightText));
    }

    public void nineBTNPush(View view){
        updateText(getResources().getString(R.string.nineText));
    }

    public void multiplyBTNPush(View view){
        updateText(getResources().getString(R.string.multiplyText));
    }

    public void divideBTNPush(View view){
        updateText(getResources().getString(R.string.divideText));
    }

    public void subtractBTNPush(View view){
        updateText(getResources().getString(R.string.subtractText));
    }

    public void addBTNPush(View view){
        updateText(getResources().getString(R.string.addText));
    }

    public void clearBTNPush(View view){
        display.setText("");
        previousCalculation.setText("");
    }

    public void parOpenBTNPush(View view){
        updateText(getResources().getString(R.string.parenthesesOpenText));
    }

    public void parCloseBTNPush(View view){
        updateText(getResources().getString(R.string.parenthesesCloseText));
    }

    public void decimalBTNPush(View view){
        updateText(getResources().getString(R.string.decimalText));
    }

    public void equalBTNPush(View view) {
        String userExp = display.getText().toString();

        if (isExpressionIncomplete(userExp)) {
            int openParenCount = countOccurrences(userExp, '(');
            int closeParenCount = countOccurrences(userExp, ')');

            for (int i = 0; i < openParenCount - closeParenCount; i++) {
                userExp += ")";
            }
        }

        previousCalculation.setText(userExp);

        userExp = userExp.replaceAll(getResources().getString(R.string.divideText), "/");
        userExp = userExp.replaceAll(getResources().getString(R.string.multiplyText), "*");

        try {
            if (userExp.toLowerCase().contains("log")) {
                double argument = extractLogArgument(userExp);

                if (!Double.isNaN(argument) && argument > 0) {
                    double result = Math.log10(argument);
                    String formattedResult = String.format("%.8f", result);

                    display.setText(String.format("%s = %s", userExp, formattedResult));
                    display.setSelection(display.getText().length());

                    long calculationDate = System.currentTimeMillis();

                    CalculationEntity calculationEntity = new CalculationEntity(userExp, formattedResult, calculationDate);

                    calculationViewModel.insertCalculation(calculationEntity);
                } else {
                    display.setText("Error: Invalid Argument for log");
                }
            } else {
                Expression exp = new Expression(userExp);
                double result = exp.calculate();
                String formattedResult = String.format("%s", result);

                display.setText(formattedResult);
                display.setSelection(formattedResult.length());

                long calculationDate = System.currentTimeMillis();

                CalculationEntity calculationEntity = new CalculationEntity(userExp, formattedResult, calculationDate);

                calculationViewModel.insertCalculation(calculationEntity);
            }
        } catch (Exception e) {
            display.setText("Error");
            e.printStackTrace();
        }
    }


    private boolean isExpressionIncomplete(String expression) {
        if (expression.matches(".*\\b(log|ln|sin|cos|tan|arcsin|arccos|arctan)\\(.*$")) {
            return true;
        }

        int openParenCount = countOccurrences(expression, '(');
        int closeParenCount = countOccurrences(expression, ')');

        return openParenCount > closeParenCount;
    }

    private int countOccurrences(String text, char target) {
        int count = 0;
        for (char c : text.toCharArray()) {
            if (c == target) {
                count++;
            }
        }
        return count;
    }

    private double extractLogArgument(String expression) {
        int startIndex = expression.indexOf("(") + 1;
        int endIndex = expression.lastIndexOf(")");
        if (startIndex >= 0 && endIndex >= 0) {
            String argumentStr = expression.substring(startIndex, endIndex);
            return Double.parseDouble(argumentStr);
        }
        return Double.NaN;
    }


    public void backspaceBTNPush(View view){
        int cursorPos = display.getSelectionStart();
        int textLen = display.getText().length();

        if (cursorPos != 0 && textLen != 0){
            SpannableStringBuilder selection = (SpannableStringBuilder) display.getText();
            selection.replace(cursorPos-1, cursorPos, "");
            display.setText(selection);
            display.setSelection(cursorPos-1);
        }
    }

    public void trigSinBTNPush(View view){
        updateText(isDegreeMode ? "sin(" : "sin(rad(");
    }

    public void trigCosBTNPush(View view){
        updateText(isDegreeMode ? "cos(" : "cos(rad(");
    }

    public void trigTanBTNPush(View view){
        updateText(isDegreeMode ? "tan(" : "tan(rad(");
    }

    public void trigArcSinBTNPush(View view){
        updateText(isDegreeMode ? "arcsin(" : "arcsin(rad(");
    }

    public void trigArcCosBTNPush(View view){
        updateText(isDegreeMode ? "arccos(" : "arccos(rad(");
    }

    public void trigArcTanBTNPush(View view) {
        updateText(isDegreeMode ? "arctan(" : "arctan(rad(");

    }
    public void naturalLogBTNPush(View view){
        updateText("ln(");
    }

    public void logBTNPush(View view){
        updateText("log(");
    }

    public void sqrtBTNPush(View view){
        updateText("sqrt(");
    }

    public void absBTNPush(View view){
        updateText("abs(");
    }

    public void piBTNPush(View view){
        updateText("pi");
    }

    public void eBTNPush(View view){
        updateText("e");
    }

    public void xSquaredBTNPush(View view){
        updateText("^(2)");
    }

    public void xPowerYBTNPush(View view){
        updateText("^(");
    }


    private double memoryValue = 0;


    public void memoryClear(View view) {
        memoryValue = 0;
    }

    public void memoryAdd(View view) {
        try {
            String currentText = display.getText().toString();
            double valueToAdd = Double.parseDouble(currentText);
            memoryValue += valueToAdd;
        } catch (NumberFormatException e) {
            display.setText("Error");
            e.printStackTrace();
        }
    }

    public void memorySubtract(View view) {
        try {
            String currentText = display.getText().toString();
            double valueToSubtract = Double.parseDouble(currentText);
            memoryValue -= valueToSubtract;
        } catch (NumberFormatException e) {
            display.setText("Error");
            e.printStackTrace();
        }
    }

    public void memoryRecall(View view) {
        display.setText(String.valueOf(memoryValue));
    }



    public void showPreviousCalculations(View view) {
        Intent intent = new Intent(this, ResultActivity.class);
        startActivity(intent);
    }

    public void toggleDegRad(View view) {
        isDegreeMode = !isDegreeMode;

        Button toggleButton = findViewById(R.id.button48);
        toggleButton.setText(isDegreeMode ? "DEG" : "RAD");
    }




}