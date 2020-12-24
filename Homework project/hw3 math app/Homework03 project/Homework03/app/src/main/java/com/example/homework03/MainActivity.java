//Deepak Veerapandian 801100869, Rishi Kumar Gnanasundaram 801101490
package com.example.homework03;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    private SeekBar seekBar;
    private TextView tv_count;
    private TextView tv_minimum;
    private TextView tv_maximum;
    private TextView tv_average;
    private Button btn_generate;
    private Handler handler;

    ProgressBar progress;
    public int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Homework03");

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

        btn_generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            if(count!=0) {
//                    Thread thread = new Thread(new ThreadCall());
//                    thread.start();
                ExecutorService taskPool = Executors.newFixedThreadPool(2);
                taskPool.execute(new ThreadCall());
                progress.setVisibility(View.VISIBLE);
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }
            else {
                Toast.makeText(getApplicationContext(), "Select a value", Toast.LENGTH_SHORT).show();
                tv_average.setText("");
                tv_maximum.setText("");
                tv_minimum.setText("");
            }
            }
        });

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message message) {
                ArrayList<Double> doubles = (ArrayList<Double>) message.getData().getSerializable("list");
                Collections.sort(doubles);
                double sum = 0.0;
                int length = doubles.size();
                for(Double x : doubles)
                    sum = sum + x;
                tv_minimum.setText(doubles.get(0)+"");
                tv_maximum.setText(doubles.get(length-1)+"");
                tv_average.setText((sum/length)+"");
                progress.setVisibility(View.INVISIBLE);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                return false;
            }
        });
    }

    class ThreadCall implements Runnable {

        void sendMsg(ArrayList<Double> outputList){
            Bundle bundle = new Bundle();
            bundle.putSerializable("list", outputList);
            Message msg = new Message();
            msg.setData(bundle);
            handler.sendMessage(msg);
        }

        @Override
        public void run() {
            ArrayList<Double> doubles = HeavyWork.getArrayNumbers(count);
            sendMsg(doubles);
        }
    }
}
