package com.example.homework01;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {
    private EditText et_weight;
    private EditText et_heightFeet;
    private EditText et_heightInches;
    private Button btn_calculate;
    private TextView tv_bmiValue;
    private TextView tv_status;
    double bmi = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("BMI Calculator");

        et_weight =findViewById(R.id.editWeight);
        et_heightFeet = findViewById(R.id.editHeightFeet);
        et_heightInches = findViewById(R.id.editHeightInches);
        btn_calculate = findViewById(R.id.btnCalculate);
        tv_bmiValue = findViewById(R.id.txtBMIvalue);
        tv_status = findViewById(R.id.txtStatus);

        clear();

        btn_calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Weight = et_weight.getText().toString();
                String HeightFeet = et_heightFeet.getText().toString();
                String HeightInches = et_heightInches.getText().toString();
                boolean error = false;

                if(Weight.equals("")) {
                    et_weight.setError("Enter a weight");
                    error = true;
                }
                if(HeightFeet.equals("")){
                    et_heightFeet.setError("Enter a height");
                    error = true;
                }
                if(HeightInches.equals("")){
                    et_heightInches.setError("Enter a height/Enter 0");
                    error = true;
//                    et_heightInches.setText("0");
                }

                DecimalFormat df = new DecimalFormat("0.00");

                if(!error){
                    double weight = Double.parseDouble(Weight);
                    int htFeet = Integer.parseInt(HeightFeet);
                    double htInches = HeightInches.equals("")? 0 : Double.parseDouble(HeightInches);
                    if(weight != 0 && htFeet != 0){
                        if(htInches < 12){
                            htInches = (htFeet*12) + htInches;
                            bmi = ((weight / (htInches * htInches)) * 703);

                            tv_bmiValue.setVisibility(View.VISIBLE);
                            tv_bmiValue.setText(df.format(bmi)+"");

                            String status = "";
                            if(bmi < 18.5)
                                status = "Underweight";
                            else if(bmi >= 18.5 && bmi <= 24.9)
                                status = "Normal weight";
                            else if(bmi >= 25 && bmi <= 29.9)
                                status = "Overweight";
                            else
                                status = "Obesity";

                            tv_status.setVisibility(View.VISIBLE);
                            tv_status.setText("You are "+status);
                            Toast.makeText(getApplicationContext(), "BMI Calculated!", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            et_heightInches.setError("Enter a value less than 12");
                            et_heightInches.setText("");
                            showInvalidInput();
                        }
                    }
                    else{
                        if (weight == 0) {
                            et_weight.setError("Enter a valid weight");
                        }
                        if (htFeet == 0) {
                            et_heightFeet.setError("Enter a valid height");
                        }
                        showInvalidInput();
                    }
                }
                else
                    showInvalidInput();
            }
        });
    }

    public void clear(){
        tv_status.setVisibility(View.INVISIBLE);
        tv_bmiValue.setVisibility(View.INVISIBLE);
        et_weight.setError(null);
        et_heightFeet.setError(null);
        et_heightInches.setError(null);
    }

    public void showInvalidInput(){
        tv_status.setVisibility(View.INVISIBLE);
        tv_bmiValue.setVisibility(View.INVISIBLE);
        Toast.makeText(getApplicationContext(), "Invalid Input!", Toast.LENGTH_SHORT).show();
    }
}
