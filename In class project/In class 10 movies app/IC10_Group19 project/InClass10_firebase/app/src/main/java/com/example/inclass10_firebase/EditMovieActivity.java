package com.example.inclass10_firebase;

import androidx.annotation.NonNull;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;

public class EditMovieActivity extends AppCompatActivity {
    private EditText et_movieName;
    private EditText et_description;
    private Spinner spinner_genre;
    private SeekBar seekbar_rating;
    private EditText et_year;
    private EditText et_imdb;
    private Button btn_saveMovieDetails;
    private TextView txt_rating;

    public static String TAG_EDIT_MOVIE = "TAG_EDIT_MOVIE";
    String originalMovieName = "";
    FirebaseFirestore db;
    Movie movie_Details;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_movie);
        setTitle("Edit Movie");

        db = FirebaseFirestore.getInstance();

        et_movieName = findViewById(R.id.editMovieName_EDIT);
        et_description = findViewById(R.id.editDescription_EDIT);
        spinner_genre = findViewById(R.id.spinnerGenre_EDIT);
        seekbar_rating = findViewById(R.id.seekBarRating_EDIT);
        et_year = findViewById(R.id.editYear_EDIT);
        et_imdb = findViewById(R.id.editImdb_EDIT);
        btn_saveMovieDetails = findViewById(R.id.btnSaveChanges);
        txt_rating = findViewById(R.id.txtRatingValue_EDIT);

        String [] genreArray = {"Action", "Animation", "Comedy", "Documentary", "Family", "Horror","Crime", "Others"};
        ArrayList<String> genreList = new ArrayList<>();
        for(String x : genreArray)
            genreList.add(x);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, genreList);
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

        /*********SETTING THE SELECTED  MOVIE DETAILS*********/
        movie_Details = (Movie) getIntent().getExtras().getSerializable(MainActivity.TAG_SELECTEDMOVIE);
        originalMovieName = movie_Details.getMovieName();

        db.collection("movies")
                .whereEqualTo("movieName", originalMovieName)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot queryDocumentSnapshot: task.getResult()) {
                                Movie movieDetails = new Movie(queryDocumentSnapshot.getData());

                                et_movieName.setText(movieDetails.getMovieName());
                                et_description.setText(movieDetails.getDescription());
                                et_year.setText(movieDetails.getYear() + "");
                                et_imdb.setText(movieDetails.getImdb());
                                seekbar_rating.setProgress(movieDetails.getRating());
                                txt_rating.setText(movieDetails.getRating() + "");

                                int i = adapter.getPosition(movieDetails.getGenre());
                                spinner_genre.setSelection(i);
                            }
                        }
                        else{
                            Log.d("movies", task.getException().toString());
                        }
                    }
                });



        btn_saveMovieDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean error = false;
                final String movieName = et_movieName.getText().toString();
                final String desc = et_description.getText().toString();
                final String genre = (String) spinner_genre.getSelectedItem();
                final int rating = 0;
                int year = 0;
                final String imdb = et_imdb.getText().toString();

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
                    if(movieName == originalMovieName)
                    {
                        editMovie(rating,movieName,desc,genre,year,imdb);
                    }
                    else
                    {
                        final int finalYear = year;
                        db.collection("movies").document(originalMovieName)
                                .delete()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            editMovie(rating,movieName,desc,genre, finalYear,imdb);
                                        }
                                    }
                                });
                    }

                }
                else{
                    Toast.makeText(getApplicationContext(), "Enter Correct Details", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    public void editMovie(int rating, String movieName, String desc, String genre, int year, String imdb){
        rating = seekbar_rating.getProgress();
        movie_Details.setMovieName(movieName);
        movie_Details.setDescription(desc);
        movie_Details.setGenre(genre);
        movie_Details.setRating(rating);
        movie_Details.setYear(year);
        movie_Details.setImdb(imdb);
        final Intent intent = new Intent();

        db.collection("movies").document(movie_Details.getMovieName())
                .set(movie_Details.toHashMap())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            intent.putExtra(TAG_EDIT_MOVIE, movie_Details);
                            setResult(RESULT_OK, intent);
                            Toast.makeText(getApplicationContext(), "Saved Movie details", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                });
    }
}
