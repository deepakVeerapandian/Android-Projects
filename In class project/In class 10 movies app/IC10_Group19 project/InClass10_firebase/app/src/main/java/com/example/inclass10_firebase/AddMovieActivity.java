package com.example.inclass10_firebase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class AddMovieActivity extends AppCompatActivity {
    private EditText et_movieName;
    private EditText et_description;
    private Spinner spinner_genre;
    private SeekBar seekbar_rating;
    private EditText et_year;
    private EditText et_imdb;
    private Button btn_addMovieDetails;
    private TextView txt_rating;

    public static String TAG_MOVIE = "TAG_MOVIE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_movie);
        setTitle("Add Movie");

        et_movieName = findViewById(R.id.editMovieName);
        et_description = findViewById(R.id.editDescription);
        spinner_genre = findViewById(R.id.spinnerGenre);
        seekbar_rating = findViewById(R.id.seekBarRating);
        et_year = findViewById(R.id.editYear);
        et_imdb = findViewById(R.id.editImdb);
        btn_addMovieDetails = findViewById(R.id.btnAddMovieDetails);
        txt_rating = findViewById(R.id.txtRatingValue);


        String [] genreArray = {"Action", "Animation", "Comedy", "Documentary", "Family", "Horror","Crime", "Others"};
        ArrayList<String> genreList = new ArrayList<>();
        for(String x : genreArray){
            genreList.add(x);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, genreList);
        spinner_genre.setAdapter(adapter);

        seekbar_rating.setMax(5);

        seekbar_rating.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seekbar_rating.setProgress(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                txt_rating.setText(seekbar_rating.getProgress()+"");
            }
        });

        btn_addMovieDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean error = false;
                String movieName = et_movieName.getText().toString();
                String desc = et_description.getText().toString();
                String genre = (String) spinner_genre.getSelectedItem();
                int rating = 0;
                int year = 0;
                String imdb = et_imdb.getText().toString();

                if(movieName.equals("")){
                    error = true;
                    et_movieName.setError("Enter a movie name");
                }
                if(desc.equals("")){
                    error = true;
                    et_description.setError("Enter a description");
                }
                if(et_year.getText().toString().equals("")){
                    error = true;
                    et_year.setError("Enter a year");
                }
                if(imdb.equals("")){
                    error = true;
                    et_imdb.setError("Enter an IMDB link");
                }

                if(movieName.length() > 50){
                    error = true;
                    et_movieName.setError("Enter a movie name less than 50 words");
                }

                if(desc.length() > 1000){
                    error = true;
                    et_description.setError("Enter a movie description less than 1000 words");
                }

                if(!et_year.getText().toString().equals("") && !et_year.getText().toString().equals(null)){
                    year = Integer.parseInt(et_year.getText().toString());
                    int currentYear = Calendar.getInstance().get(Calendar.YEAR);
                    if(year < 1900 || year > currentYear) {
                        et_year.setError("Enter a valid year between 1900 & " + currentYear);
                        error = true;
                    }
                }

                if(!error)
                {
                    rating = seekbar_rating.getProgress();
                    Movie movie = new Movie(movieName,desc,genre,rating,year,imdb);
                    Intent intent = new Intent();
                    intent.putExtra(TAG_MOVIE, movie);
                    setResult(RESULT_OK, intent);
                    Toast.makeText(getApplicationContext(), "New movie added", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Enter Correct Details", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
