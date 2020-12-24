package com.example.homework04;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    private Button btn_addMovie;
    private Button btn_edit;
    private Button btn_deleteMovie;
    private Button btn_listByYear;
    private Button btn_listByRating;

    public ArrayList<Movie> movieList = new ArrayList<>();
    public String [] movieArray = {};
    public String action = "";
    public int movieEditIndex = -1;
    public static String TAG_MOVIELIST = "TAG_MOVIELIST";
    public static String TAG_SELECTEDMOVIE = "TAG_SELECTEDMOVIE";
    public static String TAG_SELECTED_MOVIE_INDEX = "TAG_SELECTED_MOVIE_INDEX";
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("My Favourite Movies");

        btn_addMovie = findViewById(R.id.btnAddMovie);
        btn_edit = findViewById(R.id.btnEdit);
        btn_deleteMovie = findViewById(R.id.btnDeleteMovie);
        btn_listByYear = findViewById(R.id.btnListByYear);
        btn_listByRating = findViewById(R.id.btnListByRating);

        builder = new AlertDialog.Builder(this);
        showMoviesAlertDialog("");

        btn_addMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                action = "add";
                Intent intentAdd = new Intent(MainActivity.this, AddMovieActivity.class);
                startActivityForResult(intentAdd, 1);
            }
        });

        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                action = "edit";
                AlertDialog moviesAlert = builder.create();
                moviesAlert.show();
                showMoviesAlertDialog(action);
            }
        });

        btn_deleteMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                action = "delete";
                AlertDialog moviesAlert = builder.create();
                moviesAlert.show();
                showMoviesAlertDialog(action);

//                if(movieEditIndex != -1)
//                    movieList.remove(movieEditIndex);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /******ADD MOVIE******/
        if(requestCode == 1 && resultCode == RESULT_OK){
            Movie movie = (Movie) data.getExtras().getSerializable(AddMovieActivity.TAG_MOVIE);
            movieList.add(movie);
            movieArray = new String[movieList.size()];
            int i = 0;
            for(Movie x : movieList){
                movieArray[i] = x.getMovieName();
                i++;
            }
//            showMoviesAlertDialog(action);
            showMoviesAlertDialog("");
        }

        /******EDIT MOVIE******/
        if(requestCode == 2 && resultCode == RESULT_OK) {
            Movie editedMovie = (Movie) data.getExtras().getSerializable(EditMovieActivity.TAG_EDIT_MOVIE);
            Log.d("demo",editedMovie.toString());
//            String movieNameBeforeEditing = data.getExtras().getString(EditMovieActivity.TAG_PREV_MOVIE_NAME);
//            Log.d("demo",movieNameBeforeEditing.toString());


            //needed
//            int i = 0;
//            movieArray = new String[movieList.size()];
//            for(Movie movie : movieList){
//                if(movie.getMovieName().equals(movieNameBeforeEditing)){
//                    movieList.set(i, editedMovie);
//                    movieArray[i] = editedMovie.getMovieName();
////                    break;
//                }
//                movieArray[i] = movie.getMovieName();
//                i++;
//            }

            movieList.set(movieEditIndex, editedMovie);
            movieArray[movieEditIndex] = editedMovie.getMovieName();


//            for( i=0;i<movieArray.length;i++){
//                Log.d("demo","--"+movieArray[i]);
//            }
//            showMoviesAlertDialog(action);
            showMoviesAlertDialog("");
        }
    }

    public void showMoviesAlertDialog(final String action){
        builder.setTitle("Pick a Movie")
                .setCancelable(true)
                .setItems(movieArray, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        for(Movie selectedMovie : movieList){
//                            if(selectedMovie.getMovieName().equals(movieArray[which]))
//                            {
                        Movie selectedMovie = movieList.get(which);
                                 switch (action){
//                                    case "add":
//                                        Intent intentAdd = new Intent(MainActivity.this, AddMovieActivity.class);
//                                        startActivityForResult(intentAdd, 1);
                                    case "edit":
                                        Intent intentEdit = new Intent(MainActivity.this, EditMovieActivity.class);
                                        intentEdit.putExtra(TAG_SELECTEDMOVIE,selectedMovie);
//                                        intentEdit.putExtra(TAG_SELECTED_MOVIE_INDEX, which);
                                        movieEditIndex = which;
                                        startActivityForResult(intentEdit, 2);
                                        break;
                                    case "delete":
                                        deleteMovie(which);
                                        break;
                                    default:
                                        break;

//                                }
//                            }
                        }
                    }
                });

    }

    public void deleteMovie(int movieDeleteIndex){
//        movieList.remove(movie);
//        movieArray = (movieList.isEmpty()) ? new String[0] : new String[movieList.size()];
//        int i = 0;
//        if(!movieList.isEmpty()){
//            for(Movie x : movieList)
//            {
//                movieArray[i] = x.getMovieName();
//                i++;
//            }
//        }
//        showMoviesAlertDialog("");

//        movieEditIndex = which;
        String deletedMovie = movieList.get(movieDeleteIndex).getMovieName();
        Toast.makeText(this, deletedMovie + " was deleted", Toast.LENGTH_SHORT).show();

        movieList.remove(movieDeleteIndex);
        movieArray = new String[movieList.size()];
        int i = 0;
        for(Movie x : movieList){
            movieArray[i] = x.getMovieName();
            i++;
        }
        showMoviesAlertDialog("");
    }
}
