package com.example.inclass03;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class Main2Activity extends AppCompatActivity {
    private ImageView img_gender;
    private TextView tv_name;
    private TextView tv_gender;
    private Button btn_edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        setTitle("My Profile");

        img_gender = findViewById(R.id.imgDisplay);
        tv_name = findViewById(R.id.txtFullName);
        tv_gender = findViewById(R.id.txtGender);
        btn_edit = findViewById(R.id.btnEdit);

        final String firstName = getIntent().getExtras().getString(MainActivity.TAG_FIRSTNAME);
        final String lastName = getIntent().getExtras().getString(MainActivity.TAG_LASTNAME);
        final String gender = getIntent().getExtras().getString(MainActivity.TAG_IMAGE);

        tv_name.setText(firstName + " " +lastName);
        tv_gender.setText(gender);
        if(gender.equals("Male"))
            img_gender.setImageDrawable(getDrawable(R.drawable.male));
        else
            img_gender.setImageDrawable(getDrawable(R.drawable.female));

        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentResult = new Intent();
                intentResult.putExtra(MainActivity.TAG_IMAGE, gender);
                intentResult.putExtra(MainActivity.TAG_FIRSTNAME,firstName);
                intentResult.putExtra(MainActivity.TAG_LASTNAME, lastName);

                setResult(RESULT_OK, intentResult);
                finish();
            }
        });

    }
}
