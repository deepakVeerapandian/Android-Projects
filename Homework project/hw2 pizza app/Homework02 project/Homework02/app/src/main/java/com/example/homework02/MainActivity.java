//Deepak Veerapandian 801100869, Rishi Kumar Gnanasundaram 801101490
package com.example.homework02;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ProgressBar prg_bar;
    private Button btn_addTopping;
    private Button btn_clearPizza;
    private LinearLayout topping_row1;
    private LinearLayout topping_row2;
    private CheckBox cb_delivery;
    private Button btn_checkout;
    int noOfToppings = 0;
    String currentTopping = "";
    boolean isDelivery = false;
    public static String TAG_DELIVERY = "TAG_DELIVERY";
    public static String TAG_TOPPINGS = "TAG_TOPPINGS";
    int REQ_CODE = 100;

    ArrayList<String> toppingsAdded = new ArrayList<>();
    ImageView[] imgTopping = new ImageView[10];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Pizza Store");

        topping_row1 = findViewById(R.id.linearRow1);
        topping_row2 = findViewById(R.id.linearRow2);
        prg_bar = findViewById(R.id.prgBar);
        btn_addTopping = findViewById(R.id.btnAddTopping);
        btn_clearPizza = findViewById(R.id.btnClear);
        cb_delivery = findViewById(R.id.cbDelivery);
        btn_checkout = findViewById(R.id.btnCheckOut);
        final String [] toppings = {"Bacon", "Cheese", "Garlic", "Green pepper", "Mushroom", "Olive", "Onion", "Red pepper"};

        prg_bar.setMax(10);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Choose a Topping")
            .setCancelable(false)
            .setItems(toppings, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    currentTopping = toppings[i];
                    toppingsAdded.add(currentTopping);
                    createToppingImg();
                    prg_bar.setProgress(noOfToppings + 1);
                }
            });

        btn_addTopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noOfToppings = prg_bar.getProgress();
                if(noOfToppings < 10) {
                    AlertDialog toppingList = builder.create();
                    toppingList.show();
                }
                else
                    Toast.makeText(getApplicationContext(), "Maximum Topping capacity reached!", Toast.LENGTH_SHORT).show();
            }

        });

        btn_clearPizza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearPizza();
            }
        });

        cb_delivery.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(isDelivery)
                    isDelivery = false;
                else
                    isDelivery = true;
            }
        });

        btn_checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(toppingsAdded.size() == 0)
                    Toast.makeText(getApplicationContext(), "Select at least one topping", Toast.LENGTH_SHORT).show();
                else{
                    Intent intent = new Intent(MainActivity.this, OrderActivity.class);
                    intent.putExtra(TAG_DELIVERY, isDelivery);
                    intent.putStringArrayListExtra(TAG_TOPPINGS, toppingsAdded);
                    startActivityForResult(intent, REQ_CODE);
                }
            }
        });
    }

    public void createToppingImg(){
        topping_row1.removeAllViews();
        topping_row2.removeAllViews();
        for(int k=0; k < toppingsAdded.size(); k++)
        {
            imgTopping[k] = new ImageView(this);
            switch (toppingsAdded.get(k)){
                case "Bacon":
                    imgTopping[k].setImageDrawable(getDrawable(R.drawable.bacon));
                    break;
                case "Cheese":
                    imgTopping[k].setImageDrawable(getDrawable(R.drawable.cheese));
                    break;
                case "Garlic":
                    imgTopping[k].setImageDrawable(getDrawable(R.drawable.garlic));
                    break;
                case "Green pepper":
                    imgTopping[k].setImageDrawable(getDrawable(R.drawable.green_pepper));
                    break;
                case "Mushroom":
                    imgTopping[k].setImageDrawable(getDrawable(R.drawable.mashroom));
                    break;
                case "Olive":
                    imgTopping[k].setImageDrawable(getDrawable(R.drawable.olive));
                    break;
                case "Onion":
                    imgTopping[k].setImageDrawable(getDrawable(R.drawable.onion));
                    break;
                case "Red pepper":
                    imgTopping[k].setImageDrawable(getDrawable(R.drawable.red_pepper));
                    break;
            }

            imgTopping[k].setId(k);

            imgTopping[k].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    noOfToppings = prg_bar.getProgress();
                    prg_bar.setProgress(noOfToppings - 1);
                    toppingsAdded.remove(view.getId());
                    createToppingImg();
                }
            });

            if(k < 5)
                topping_row1.addView(imgTopping[k]);
            else
                topping_row2.addView(imgTopping[k]);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQ_CODE && resultCode == RESULT_OK){
            clearPizza();
        }
    }

    public void clearPizza(){
        prg_bar.setProgress(0);
        topping_row1.removeAllViews();
        topping_row2.removeAllViews();
        toppingsAdded.clear();
        if(cb_delivery.isChecked())
            cb_delivery.toggle();
    }
}
