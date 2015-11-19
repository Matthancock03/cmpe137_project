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
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends Activity {

    //declares inputs
    EditText editText1;
    EditText editText2;
    EditText editText3;
    EditText editText4;
    EditText editText5;

    //declares outputs
    TextView PaymentAnswer;
    TextView InterestPaidAnswer;
    TextView MonthlyPaymentAnswer;
    TextView PayoffDateAnswer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //declares buttons
        Button ResetButton = (Button) findViewById(R.id.Reset);
        Button Calculate = (Button) findViewById(R.id.Calculate);

        editText1 = (EditText) findViewById(R.id.HomeValueField);
        editText2 = (EditText) findViewById(R.id.DownPayField);
        editText3 = (EditText) findViewById(R.id.APRField);
        editText4 = (EditText) findViewById(R.id.TermsField);
        editText5 = (EditText) findViewById(R.id.TaxRateField);

        PaymentAnswer = (TextView) findViewById(R.id.PaymentAnswer);
        InterestPaidAnswer = (TextView) findViewById(R.id.InterestPaidAnswer);
        MonthlyPaymentAnswer = (TextView) findViewById(R.id.MonthlyPaymentAnswer);
        PayoffDateAnswer = (TextView) findViewById(R.id.PayoffDateAnswer);

        //checks if reset button is pressed
        ResetButton.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        editText1.setText("");
                        editText2.setText("");
                        editText3.setText("");
                        editText4.setText("");
                        editText5.setText("");
                        PaymentAnswer.setText("");
                        InterestPaidAnswer.setText("");
                        MonthlyPaymentAnswer.setText("");
                        PayoffDateAnswer.setText("");
                    }
                }
        );

        //checks if calculate button is pressed
        Calculate.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        //converting inputs to Doubles to be used for calculations
                        double Homevalue = 0;
                            try {
                                Homevalue = Double.parseDouble(editText1.getText().toString().replaceAll("[,]", ""));
                            }
                            catch (NumberFormatException e){

                            }
                        double DownPay = 0;
                            try{
                                DownPay = Double.parseDouble(editText2.getText().toString().replaceAll("[,]", ""));
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
                                TaxRate = Double.parseDouble(editText5.getText().toString());
                            }
                            catch (NumberFormatException e){

                            }

                        //gets calendar month
                        Calendar cal = Calendar.getInstance();
                        SimpleDateFormat month_date = new SimpleDateFormat("MMMM");
                        String month_name = month_date.format(cal.getTime());

                        //gets calendar year
                        cal.get(Calendar.YEAR);
                        cal.add(Calendar.YEAR, (int)Terms);

                        //calculations
                        double Tax = Homevalue * Terms * TaxRate/100;
                        double Principal = Homevalue - DownPay;
                        APR = (APR/100)/12;
                        Terms = Terms*12;
                        double MonthlyPayment = Principal*((APR*Math.pow((1+APR), Terms))/(Math.pow((1+APR), Terms)-1));
                        double TotalIntPaid = Terms * MonthlyPayment - Principal;

                        //sets outputs
                        DecimalFormat format = new DecimalFormat("#,###.00");
                        PaymentAnswer.setText(format.format(Tax));
                        InterestPaidAnswer.setText(format.format(TotalIntPaid));
                        MonthlyPaymentAnswer.setText(format.format(MonthlyPayment));
                        PayoffDateAnswer.setText(month_name + " " + cal.get(Calendar.YEAR));

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

}
