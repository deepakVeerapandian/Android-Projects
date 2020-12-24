package com.example.inclass03;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private RadioGroup rg_gender;
    private RadioButton rb_male;
    private RadioButton rb_female;
    private ImageView iv_gender;
    private Button btn_save;
    private EditText et_firstName;
    private EditText et_LastName;
    private String flag_image = "";
    public static String TAG_IMAGE = "TAG_IMAGE";
    public static String TAG_FIRSTNAME = "TAG_FIRSTNAME";
    public static String TAG_LASTNAME = "TAG_LASTNAME";
    public static int REQ_CODE = 100;
    String gender = "";
    String firstName = "";
    String lastName = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("My Profile");

        rg_gender = findViewById(R.id.rgGender);
        rb_male = findViewById(R.id.rbtnMale);
        rb_female = findViewById(R.id.rbtnFemale);
        iv_gender = findViewById(R.id.imgGender);
        btn_save = findViewById(R.id.btnSave);
        et_firstName = findViewById(R.id.editFirstName);
        et_LastName = findViewById(R.id.editLastName);


        rg_gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (radioGroup.getCheckedRadioButtonId()){
                    case R.id.rbtnMale:
                        iv_gender.setImageDrawable(getDrawable(R.drawable.male));
                        flag_image = "Male";
                        break;
                    case R.id.rbtnFemale:
                        iv_gender.setImageDrawable(getDrawable(R.drawable.female));
                        flag_image = "Female";
                        break;
                    default:
                        flag_image = "";
                        break;
                }
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firstName =  et_firstName.getText().toString();
                lastName = et_LastName.getText().toString();

                if(!firstName.equals("") &&  !lastName.equals("") && flag_image!="")
                {
                    Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                    intent.putExtra(TAG_IMAGE,flag_image);
                    intent.putExtra(TAG_FIRSTNAME, firstName);
                    intent.putExtra(TAG_LASTNAME, lastName);
//                startActivity(intent);
                    startActivityForResult(intent, REQ_CODE);
                }
                else
                {
                    if(firstName.equals(""))
                        et_firstName.setError("Enter a name");
                    if(lastName.equals(""))
                        et_LastName.setError("Enter a name");
                    if(flag_image == "")
                        Toast.makeText(getApplicationContext(), "Select a gender", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQ_CODE && resultCode == RESULT_OK ){
            gender = data.getExtras().getString(TAG_IMAGE);
            firstName = data.getExtras().getString(TAG_FIRSTNAME);
            lastName = data.getExtras().getString(TAG_LASTNAME);

            et_firstName.setText(firstName);
            et_LastName.setText(lastName);
            if(gender.equals("Male")) {
                iv_gender.setImageDrawable(getDrawable(R.drawable.male));
                rb_male.setChecked(true);
            }
            else {
                iv_gender.setImageDrawable(getDrawable(R.drawable.female));
                rb_female.setChecked(true);
            }
        }
    }
}
