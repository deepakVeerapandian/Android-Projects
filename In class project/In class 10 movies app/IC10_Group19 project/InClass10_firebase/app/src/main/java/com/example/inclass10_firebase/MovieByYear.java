package com.example.inclass10_firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.Comparator;

public class MovieByYear extends AppCompatActivity {
    private TextView tv_movieName;
    private TextView tv_description;
    private TextView tv_genre;
    private TextView tv_rating;
    private TextView tv_year;
    private TextView tv_imdb;
    private TextView tv_heading;
    private Button btn_finish;
    private ImageView img_first;
    private ImageView img_last;
    private ImageView img_previous;
    private ImageView img_next;

    public ArrayList<Movie> movieList;
    public int currentPage = 0;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_by_year);
        setTitle("Movies by year");

        tv_movieName = findViewById(R.id.txtNameValue_YEAR);
        tv_description = findViewById(R.id.editDescription_YEAR);
        tv_genre = findViewById(R.id.txtGenreValue_YEAR);
        tv_rating = findViewById(R.id.txtRatingValue_YEAR);
        tv_year = findViewById(R.id.txtYearValue_YEAR);
        tv_imdb = findViewById(R.id.txtIMDBValue_YEAR);
        tv_heading = findViewById(R.id.txtYearHeading);
        btn_finish = findViewById(R.id.btnFinish_YEAR);
        img_first = findViewById(R.id.imgFirst_YEAR);
        img_last = findViewById(R.id.imgLast_YEAR);
        img_next = findViewById(R.id.imgNext_YEAR);
        img_previous = findViewById(R.id.imgPrevious_YEAR);

//        movieList = (ArrayList<Movie>) getIntent().getSerializableExtra(MainActivity.TAG_MOVIELIST);
//        Collections.sort(movieList, yearComparator);

        db = FirebaseFirestore.getInstance();
        movieList = new ArrayList<Movie>();

        db.collection("movies").orderBy("year", Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()){
                                Movie movie = new Movie(queryDocumentSnapshot.getData());
                                movieList.add(movie);
                            }
                            setValues(0);
                        }
                        else{
                            Log.d("movies", task.getException().toString());
                        }
                    }
                });

        tv_heading.setText("Movies by year");


        btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        img_first.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPage = 0;
                setValues(currentPage);
            }
        });

        img_last.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPage = movieList.size()-1;
                setValues(currentPage);
            }
        });

        img_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentPage < movieList.size()-1) {
                    currentPage = currentPage + 1;
                    setValues(currentPage);
                }
            }
        });

        img_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentPage > 0) {
                    currentPage = currentPage - 1;
                    setValues(currentPage);
                }
            }
        });
    }

    public static Comparator<Movie> yearComparator = new Comparator<Movie>() {
        @Override
        public int compare(Movie o1, Movie o2) {
            return o1.getYear()-o2.getYear();
        }
    };

    public void setValues(int index){
        Movie movie = movieList.get(index);
        tv_movieName.setText(movie.getMovieName());
        tv_description.setText(movie.getDescription());
        tv_genre.setText(movie.getGenre());
        tv_rating.setText(movie.getRating()+" / 5");
        tv_year.setText(movie.getYear()+"");
        tv_imdb.setText(movie.getImdb());
    }

}

