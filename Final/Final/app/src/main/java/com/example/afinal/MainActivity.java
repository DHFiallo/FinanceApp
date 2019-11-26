// MainActivity.java
// Calculates a bill total based on a tip percentage
package com.example.afinal;

import android.support.v7.app.AppCompatActivity; // base class
import android.os.Bundle; // for saving state information
import android.text.Editable; // for EditText event handling
import android.text.TextWatcher; // EditText listener
import android.widget.EditText; // for bill amount input
import android.widget.SeekBar; // for changing the tip percentage
import android.widget.SeekBar.OnSeekBarChangeListener; // SeekBar listener
import android.widget.TextView; // for displaying text

import java.text.NumberFormat; // for currency formatting

// MainActivity class for the TIp Calculator app
public class MainActivity extends AppCompatActivity {

    // currency and percent formatter objects
    private static final NumberFormat currencyFormat=
            NumberFormat.getCurrencyInstance();
    private static final NumberFormat percentFormat=
            NumberFormat.getPercentInstance();
    private static final NumberFormat integerFormat=
            NumberFormat.getIntegerInstance();

    private double amount=0.0; // amount entered by user
    private int years=0; // years entered by user
    private double percent=0.15; // initial loan percentage
    private TextView amountTextView; // shows formatted amount
    private TextView yearTextView; // total loan months
    private TextView percentTextView; // shows loan percentage
    private TextView interestTextView; // shows calculated interest amount
    private TextView totalTextView; // shows calculated total bill amount
    private TextView paymentTextView; // shows calculated monthly payment

    // called when the activity is first created
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); // call superclass's version
        setContentView(R.layout.activity_main); // inflate the GUI

        // get references to programmatically manipulated TextViews
        amountTextView=(TextView) findViewById(R.id.amountTextView);
        yearTextView=(TextView) findViewById(R.id.yearTextView);
        percentTextView=(TextView) findViewById(R.id.percentTextView);
        interestTextView=(TextView) findViewById(R.id.interestTextView);
        totalTextView=(TextView) findViewById(R.id.totalTextView);
        paymentTextView=(TextView) findViewById(R.id.paymentTextView);
        interestTextView.setText(currencyFormat.format(0));
        totalTextView.setText(currencyFormat.format(0));
        paymentTextView.setText(currencyFormat.format(0));

        // set amountEditText's TextWatcher
        EditText amountEditText=
                (EditText) findViewById(R.id.amountEditText);
        amountEditText.addTextChangedListener(amountEditTextWatcher);

        // set monthEditText's TextWatcher
        EditText yearEditText=
                (EditText) findViewById(R.id.yearEditText);
        yearEditText.addTextChangedListener(yearEditTextWatcher);

        // set percentSeekBar's OnSeekBarChangeListener
        SeekBar percentSeekBar=
                (SeekBar) findViewById(R.id.percentSeekBar);
        percentSeekBar.setOnSeekBarChangeListener(seekBarListener);
    }

    //calculate and display tip and total amounts
    private void calculate(){
        //format percent and display in percentTextView
        percentTextView.setText(percentFormat.format(percent));

        // calculate the interest and total
        double p=amount; // principle
        int n=12; // number of compound periods per year
        double r=percent; // rate
        double t=years; // years

        double total=p*(Math.pow(1+(r/n),(n*t)));
        double interest=total-amount;
        double payment=total/(t*12);

        //display interest and total formatted as currency
        interestTextView.setText(currencyFormat.format(interest));
        totalTextView.setText(currencyFormat.format(total));
        if(amount!=0&&years!=0) {
            paymentTextView.setText(currencyFormat.format(payment));
        }
    }

    //listener object for the SeekBar's progress changed events
    private final OnSeekBarChangeListener seekBarListener=
            new OnSeekBarChangeListener(){
                // update percent, then call calculate
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress,
                                              boolean fromUser){
                    percent=progress/100.0; // set percent based on progress
                    calculate(); // calculate and display tip and total
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar){}

                @Override
                public void onStopTrackingTouch(SeekBar seekBar){}
            };

    // listener object for the EditText's text-changed events
    private final TextWatcher amountEditTextWatcher=new TextWatcher(){
        // called when the user modifies the bill amount
        @Override
        public void onTextChanged(CharSequence s, int start,
                                  int before, int count){

            try { // get bill amount and display currency formatted value
                amount = Double.parseDouble(s.toString()) / 100.0;
                amountTextView.setText(currencyFormat.format(amount));
            }
            catch(NumberFormatException e) { // if s is empty or non-numeric
                amountTextView.setText("");
                amount = 0.0;
            }

            calculate(); // update the trip and total TextViews
        }

        @Override
        public void afterTextChanged(Editable s){}

        @Override
        public void beforeTextChanged(
                CharSequence s,int start,int count, int after){}
    };
    // listener object for the EditText's text-changed events
    private final TextWatcher yearEditTextWatcher=new TextWatcher(){
        // called when the user modifies the months
        @Override
        public void onTextChanged(CharSequence s, int start,
                                  int before, int count){

            try { // get years and display value
                years = Integer.parseInt(s.toString());
                yearTextView.setText(integerFormat.format(years));
            }
            catch(NumberFormatException e) { // if s is empty or non-numeric
                yearTextView.setText("");
                years = 0;
            }

            calculate(); // update the trip and total TextViews
        }

        @Override
        public void afterTextChanged(Editable s){}

        @Override
        public void beforeTextChanged(
                CharSequence s,int start,int count, int after){}
    };
}
