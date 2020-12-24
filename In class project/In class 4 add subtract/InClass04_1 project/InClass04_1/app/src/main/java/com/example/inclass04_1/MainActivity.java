package com.example.inclass04_1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {
    private SeekBar seekBar;
    private TextView tv_count;
    private TextView tv_minimum;
    private TextView tv_maximum;
    private TextView tv_average;
    private Button btn_generate;

    ProgressBar progress;
//    public int progressValue = 0;
    public int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("InClass4");

        seekBar = findViewById(R.id.seekBar);
        tv_count = findViewById(R.id.valueCount);
        tv_minimum = findViewById(R.id.valueMinimum);
        tv_maximum = findViewById(R.id.valueMaximum);
        tv_average = findViewById(R.id.valueAverage);
        btn_generate = findViewById(R.id.btnGenerate);
        progress = (ProgressBar)findViewById(R.id.pb_progress);

        seekBar.setMax(10);


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                seekBar.setProgress(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                tv_count.setText((seekBar.getProgress()) + " Times");
                count = seekBar.getProgress();
            }
        });

//        ProgressDialog progressbar = new ProgressBar(this);
        btn_generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(count!=0) {
                    new asynClass().execute(count);
                    progress.setVisibility(View.VISIBLE);



                }
                else {
                    Toast.makeText(getApplicationContext(), "Select a value", Toast.LENGTH_SHORT).show();
                    tv_average.setText("");
                    tv_maximum.setText("");
                    tv_minimum.setText("");
                }
            }
        });
    }

    class asynClass extends AsyncTask<Integer,String, ArrayList<Double>>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(ArrayList<Double> doubles) {
            super.onPostExecute(doubles);
            Collections.sort(doubles);

            double sum = 0.0;
            int length = doubles.size();
            for(Double x : doubles)
                sum = sum + x;

            tv_minimum.setText(doubles.get(0)+"");
            tv_maximum.setText(doubles.get(length-1)+"");
            tv_average.setText((sum/length)+"");
            progress.setVisibility(View.INVISIBLE);
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected ArrayList<Double> doInBackground(Integer... integers) {
            return (HeavyWork.getArrayNumbers(integers[0]));
        }
    }
}
