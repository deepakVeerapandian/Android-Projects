package com.example.inclass10_firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MovieByRating extends AppCompatActivity {
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
        setContentView(R.layout.activity_movie_by_rating);
        setTitle("Movies by rating");

        tv_movieName = findViewById(R.id.txtNameValue_RATING);
        tv_description = findViewById(R.id.editDescription_RATING);
        tv_genre = findViewById(R.id.txtGenreValue_RATING);
        tv_rating = findViewById(R.id.txtRatingValue_RATING);
        tv_year = findViewById(R.id.txtYearValue_RATING);
        tv_imdb = findViewById(R.id.txtIMDBValue_RATING);
        tv_heading = findViewById(R.id.txtRatingHeading);
        btn_finish = findViewById(R.id.btnFinish_RATING);
        img_first = findViewById(R.id.imgFirst_RATING);
        img_last = findViewById(R.id.imgLast_RATING);
        img_next = findViewById(R.id.imgNext_RATING);
        img_previous = findViewById(R.id.imgPrevious_RATING);

//        movieList = (ArrayList<Movie>) getIntent().getSerializableExtra(MainActivity.TAG_MOVIELIST);
//        Collections.sort(movieList, ratingComparator);

        tv_heading.setText("Movies by rating");
//        setValues(0);

        db = FirebaseFirestore.getInstance();
        movieList = new ArrayList<Movie>();

        db.collection("movies").orderBy("rating", Query.Direction.DESCENDING)
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

    public static Comparator<Movie> ratingComparator = new Comparator<Movie>() {
        @Override
        public int compare(Movie o1, Movie o2) {
            return o2.getRating()-o1.getRating();
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

