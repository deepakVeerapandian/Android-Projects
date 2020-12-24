//Deepak Veerapandian 801100869, Rishi Kumar Gnanasundaram 801101490
package com.example.homework02;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

public class OrderActivity extends AppCompatActivity {
    private TextView tv_basePrice;
    private TextView tv_toppingsPrice;
    private TextView tv_deliveryCost;
    private TextView tv_totalCost;
    private TextView tv_toppingsAdded;
    private Button btn_finish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        setTitle("Pizza Order");

        tv_basePrice = findViewById(R.id.valueBasePrice);
        tv_toppingsPrice = findViewById(R.id.valueToppings);
        tv_deliveryCost = findViewById(R.id.valueDeliveryCost);
        tv_totalCost = findViewById(R.id.valueTotalCost);
        tv_toppingsAdded = findViewById(R.id.valueToppingsAdded);
        btn_finish = findViewById(R.id.btnFinish);

        double basePrice = 6.5;
        boolean isDelivery = getIntent().getExtras().getBoolean(MainActivity.TAG_DELIVERY);
        ArrayList<String> toppingList = getIntent().getStringArrayListExtra(MainActivity.TAG_TOPPINGS);
        double toppingsPrice = toppingList.size() * 1.5;
        double deliveryPrice = (isDelivery)? 4.0 : 0.0;

        tv_toppingsAdded.setText(toppingList.toString().replace("[","").replace("]",""));
        tv_basePrice.setText(basePrice+"$");
        tv_deliveryCost.setText(deliveryPrice+"$");
        tv_toppingsPrice.setText(toppingsPrice+"$");
        tv_totalCost.setText((basePrice+deliveryPrice+toppingsPrice)+"$");

        btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        });

    }
}
