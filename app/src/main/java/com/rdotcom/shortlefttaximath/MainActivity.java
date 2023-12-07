package com.rdotcom.shortlefttaximath;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rdotcom.shortlefttaximath.PaymentDetails;
import com.rdotcom.shortlefttaximath.PaymentsListAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.rdotcom.shortlefttaximath.R;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<PaymentDetails> paymentDetailsArrayList;
    EditText edtTotal;
    EditText edtCustomers;

    Button btnCalcChange;

    FloatingActionButton btnGotoChange;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        paymentDetailsArrayList = new ArrayList<>();
        btnGotoChange = findViewById(R.id.floatingActionButton);







        //floating window for the calculations
        Dialog dialog = new Dialog(this);

        dialog.setContentView(R.layout.calculation_screen);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(true);

        btnGotoChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
            }
        });

        edtCustomers = dialog.findViewById(R.id.edtCustomers);
        edtTotal = dialog.findViewById(R.id.edtTotal);
        edtCustomers.setBackgroundResource(com.travijuu.numberpicker.library.R.color.colorLightGray);
        edtTotal.setBackgroundResource(com.travijuu.numberpicker.library.R.color.colorLightGray);



        edtCustomers.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                //clear all views to make space for new ones
                LinearLayout layout = dialog.findViewById(R.id.paymentLayout);


                layout.removeAllViews();
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(0, 20, 0, 0);

                //use the text to add more  views
                String value = editable.toString();

                if(value.equals("")){
                    value = "0";
                }
                int customers = Integer.parseInt(value);

                if((customers < 30)&&(customers > 0)) {
                    //add edittext to layout and set the tag to b able to get back the value
                    for(int i = 0;i < customers;i++) {
                        EditText ed = new EditText(MainActivity.this);
                        ed.setBackgroundResource(com.travijuu.numberpicker.library.R.color.colorLightGray);
                        ed.setLayoutParams(layoutParams);
                        ed.setPadding(40,40,0,40);
                        ed.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED);
                        ed.setTag("edtPayment"+i);
                        layout.addView(ed);
                    }

                    Button btnCalculate = new Button(MainActivity.this);
                    btnCalculate.setTag("btnCalculateChange");
                    btnCalculate.setText("Get Change");
                    btnCalculate.setLayoutParams(layoutParams);
                    layout.addView(btnCalculate);


                    //handle calculations
                    btnCalcChange = layout.findViewWithTag("btnCalculateChange");
                    btnCalcChange.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            int customerPaymentsTotal = 0;

                            //store individual customer payments
                            ArrayList<Integer> customerPayments = new ArrayList<>();

                            //get total money paid
                            String strtotalPayment = edtTotal.getText().toString();
                            if(strtotalPayment.equals("")){
                                strtotalPayment = "0";
                            }
                            int totalPayment = Integer.parseInt(strtotalPayment);


                            //get the values from the tags
                            for(int i = 0;i < customers;i++) {
                                String strtag = "edtPayment"+i;
                                TextView txtCustomerPayment = layout.findViewWithTag(strtag);

                                String strpaymentvalue = txtCustomerPayment.getText().toString();
                                if(strpaymentvalue.equals("")){
                                    strpaymentvalue = "0";
                                }
                                customerPayments.add(Integer.parseInt(strpaymentvalue));
                                customerPaymentsTotal += Integer.parseInt(strpaymentvalue);
                            }



                            //set the change amount value after calculations
                            ConstraintLayout layoutMain = dialog.findViewById(R.id.calcLayout);
                            if(totalPayment >= customerPaymentsTotal){
                                TextView txtChangeView = layoutMain.findViewWithTag("txtChangeview");
                                TextView txtChangeTitle = layoutMain.findViewWithTag("textView5");
                                //handle the calculations
                                txtChangeTitle.setText("Change");
                                txtChangeView.setText("R"+(totalPayment-customerPaymentsTotal));





                                //add payment to the list
                                int initialCP = 0;
                                String paymentStatement = "R"+totalPayment+" :     ";
                                for(int cp : customerPayments){
                                    int counter = 0;

                                    if(initialCP != cp) {
                                        initialCP = cp;

                                        for (int cp2 : customerPayments) {
                                            if(initialCP == cp2){
                                                counter +=1;
                                            }
                                        }

                                        paymentStatement += counter+"->[ R"+initialCP+" ]   ,  ";
                                    }
                                }


                                paymentDetailsArrayList.add(new PaymentDetails(paymentStatement,("Change : R"+(totalPayment-customerPaymentsTotal))));

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        RecyclerView recyclerView = findViewById(R.id.recyclerView);
                                        PaymentsListAdapter recyclerAdapter = new PaymentsListAdapter(paymentDetailsArrayList);
                                        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                                        recyclerView.setAdapter(recyclerAdapter);
                                    }
                                });




                            }else{
                                Snackbar snackbar = Snackbar.make(layoutMain,"You were'nt payed the whole amount!!",Snackbar.LENGTH_LONG);
                                snackbar.show();
                            }



                        }
                    });
                }




            }
        });

        dialog.show();
    }
}