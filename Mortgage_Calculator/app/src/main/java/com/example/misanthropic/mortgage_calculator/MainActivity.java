package com.example.misanthropic.mortgage_calculator;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import java.lang.Math;
import java.text.DecimalFormat;

public class MainActivity extends Activity {

    EditText editText1;
    EditText editText2;
    EditText editText3;
    EditText editText4;
    EditText editText5;

    TextView answer1;
    TextView answer2;
    TextView answer3;
    TextView answer4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button ResetButton = (Button) findViewById(R.id.Reset);
        Button Calculate = (Button) findViewById(R.id.Calculate);

        editText1 = (EditText) findViewById(R.id.HomeValueField);
        editText2 = (EditText) findViewById(R.id.DownPayField);
        editText3 = (EditText) findViewById(R.id.APRField);
        editText4 = (EditText) findViewById(R.id.TermsField);
        editText5 = (EditText) findViewById(R.id.TaxRateField);

        answer1 = (TextView) findViewById(R.id.Answer1);
        answer2 = (TextView) findViewById(R.id.Answer2);
        answer3 = (TextView) findViewById(R.id.Answer3);
        answer4 = (TextView) findViewById(R.id.Answer4);

        ResetButton.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        editText1.setText("");
                        editText2.setText("");
                        editText3.setText("");
                        editText4.setText("");
                        editText5.setText("");
                    }
                }
        );

        Calculate.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        double Homevalue = 0;
                            try {
                                Homevalue = Double.parseDouble(editText1.getText().toString().replaceAll("[.,]", ""));
                            }
                            catch (NumberFormatException e){

                            }
                        double DownPay = 0;
                            try{
                                DownPay = Double.parseDouble(editText2.getText().toString().replaceAll("[.,]",""));
                            }
                            catch (NumberFormatException e){

                            }
                        double APR = 0;
                            try {
                                APR = Double.parseDouble(editText3.getText().toString());
                            }
                            catch (NumberFormatException e){

                            }
                        double Terms = 0;
                            try {
                                Terms = Double.parseDouble(editText4.getText().toString());
                            }
                            catch (NumberFormatException e){

                            }
                        double TaxRate = 0;
                            try {
                                Double.parseDouble(editText5.getText().toString());
                            }
                            catch (NumberFormatException e){

                            }

                        APR = (APR/100)/12;
                        Terms = Terms*12;

                        double MonthlyPayment = DownPay*((APR*Math.pow((1+APR), Terms))/(Math.pow((1+APR), Terms)-1));

                        DecimalFormat format = new DecimalFormat("#,###.00");
                        answer3.setText(format.format(MonthlyPayment));
                    }
                }
        );

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void calculateMortgage(Menu menu){

    }
}
