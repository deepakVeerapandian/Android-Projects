/*
* Assignment : Inclass 02
* Full name : Deepak Veerapandian, Rishikumar Gnanasundaram
*/

package com.example.inclass02;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TextView tv_shape;
    private TextView tv_result;
    private EditText et_length1;
    private EditText et_length2;
    private TextView txt_length1;
    private TextView txt_length2;
    private Button btn_clear;
    private Button btn_calculate;
    private ImageView iv_triangle;
    private ImageView iv_square;
    private ImageView iv_circle;
    double len1 = 0.0;
    double len2 = 0.0;
    String selectedShape = "none";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Area Calculator");

        txt_length1 = findViewById(R.id.txtLength1);
        txt_length2 = findViewById(R.id.txtLength2);
        et_length1 = findViewById(R.id.txtLen1Value);
        et_length2 = findViewById(R.id.txtLen2Value);
        tv_result = findViewById(R.id.txtResult);
        tv_shape = findViewById(R.id.txtMessage);
        btn_calculate = findViewById(R.id.btnCalculate);
        btn_clear = findViewById(R.id.btnClear);
        iv_circle = findViewById(R.id.imgCircle);
        iv_square = findViewById(R.id.imgSquare);
        iv_triangle = findViewById(R.id.imgTriangle);

        tv_result.setVisibility(View.INVISIBLE);

        btn_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearButton();
            }
        });

        btn_calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                tv_result.setVisibility(View.INVISIBLE);
                String temp1 = et_length1.getText().toString();
                String temp2 = et_length2.getText().toString();
                if(temp1.equals("") )
                    et_length1.setError("Enter a value");
                if(temp2.equals(""))
                    et_length2.setError("Enter a value");

                if(!temp1.isEmpty())
                {
                    len1 = Double.parseDouble(temp1);

                    double result = 0.0;
                    tv_result.setVisibility(View.VISIBLE);

                    switch (selectedShape){
                        case "triangle":
                            if(!temp2.isEmpty())
                            {
                                len2 = Double.parseDouble(temp2);
                                result = 0.5 * len1 * len2;
                            }
                            break;
                        case "square":
                            result = len1 * len1;
                            break;
                        case "circle":
                            result = 3.1416 * len1 * len1;
                            break;
                        case "none":
                            tv_shape.setError("Select a shape");
                            tv_result.setVisibility(View.INVISIBLE);
                    }
                    tv_result.setText(result+"");
                }
            }
        });

        iv_triangle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_length2.setVisibility(View.VISIBLE);
                txt_length2.setVisibility(View.VISIBLE);
                tv_shape.setText("Triangle");
                selectedShape = "triangle";
                clearErrors();
            }
        });

        iv_square.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_length2.setVisibility(View.INVISIBLE);
                txt_length2.setVisibility(View.INVISIBLE);
                tv_shape.setText("Square");
                selectedShape = "square";
                clearErrors();
            }
        });

        iv_circle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_length2.setVisibility(View.INVISIBLE);
                txt_length2.setVisibility(View.INVISIBLE);
                tv_shape.setText("Circle");
                selectedShape = "circle";
                clearErrors();
            }
        });

    }

    public void clearButton(){
        tv_shape.setText("Select a shape");
        et_length1.setText("");
        et_length2.setText("");
        tv_result.setVisibility(View.INVISIBLE);
        et_length2.setVisibility(View.VISIBLE);
        txt_length2.setVisibility(View.VISIBLE);
        selectedShape = "none";
        clearErrors();
    }

    public void clearErrors(){
        tv_shape.setError(null);
        et_length1.setError(null);
        et_length2.setError(null);

//        tv_result.setText("");
//        tv_result.setVisibility(View.INVISIBLE);
    }
}
