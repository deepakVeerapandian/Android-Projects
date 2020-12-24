//Deepak Veerapandian 801100869, Rishi Kumar Gnanasundaram 801101490
package com.example.inclass10_firebase;

import androidx.annotation.NonNull;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

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
    AlertDialog.Builder builder;
    FirebaseFirestore db;

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
        db = FirebaseFirestore.getInstance();
        fetchMovieArrayList();
//        showMoviesAlertDialog("");

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
                fetchMovieArrayList();
                AlertDialog moviesAlert = builder.create();
                moviesAlert.show();
                showMoviesAlertDialog(action);
            }
        });

        btn_deleteMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                action = "delete";
                fetchMovieArrayList();
                AlertDialog moviesAlert = builder.create();
                moviesAlert.show();
                showMoviesAlertDialog(action);
            }
        });

        btn_listByYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                fetchMovieArrayList();
                Intent intent = new Intent(MainActivity.this, MovieByYear.class);
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                intent.putExtra(TAG_MOVIELIST, movieList);
                if(!movieList.isEmpty())
                    startActivity(intent);
                else
                    Toast.makeText(getApplicationContext(), "No movie to display", Toast.LENGTH_SHORT).show();
            }
        });

        btn_listByRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                fetchMovieArrayList();
                Intent intent = new Intent(MainActivity.this, MovieByRating.class);
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                intent.putExtra(TAG_MOVIELIST, movieList);
                if(!movieList.isEmpty())
                    startActivity(intent);
                else
                    Toast.makeText(getApplicationContext(), "No movie to display", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /******ADD MOVIE******/
        if(requestCode == 1 && resultCode == RESULT_OK){
            final Movie movie = (Movie) data.getExtras().getSerializable(AddMovieActivity.TAG_MOVIE);

            Map<String , Object> movieMap = movie.toHashMap();
            db.collection("movies").document(movie.getMovieName())
                    .set(movieMap)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Log.d("movie", movie.getMovieName()+"added successfully");
                            }
                            else{
                                Log.d("movie", task.getException().toString());
                            }
                        }
                    });

            movieList.add(movie);
            movieArray = new String[movieList.size()];
            int i = 0;
            for(Movie x : movieList){
                movieArray[i] = x.getMovieName();
                i++;
            }
            showMoviesAlertDialog("");
        }

        /******EDIT MOVIE******/
        if(requestCode == 2 && resultCode == RESULT_OK) {
            Movie editedMovie = (Movie) data.getExtras().getSerializable(EditMovieActivity.TAG_EDIT_MOVIE);
            movieList.set(movieEditIndex, editedMovie);
            movieArray[movieEditIndex] = editedMovie.getMovieName();
            showMoviesAlertDialog("");
        }
//        fetchMovieArrayList();
    }

    public void showMoviesAlertDialog(final String action){
//        fetchMovieArrayList();
        builder.setTitle("Pick a Movie")
                .setCancelable(true)
                .setItems(movieArray, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Movie selectedMovie = movieList.get(which);
                        switch (action){
                            case "edit":
                                Intent intentEdit = new Intent(MainActivity.this, EditMovieActivity.class);
                                intentEdit.putExtra(TAG_SELECTEDMOVIE,selectedMovie);
                                movieEditIndex = which;
                                startActivityForResult(intentEdit, 2);
                                break;
                            case "delete":
                                deleteMovie(which);
                                break;
                            default:
                                break;
                        }
                    }
                });
    }

    public void deleteMovie(int movieDeleteIndex){
        final String deletedMovie = movieList.get(movieDeleteIndex).getMovieName();
//        Toast.makeText(this, deletedMovie + " was deleted", Toast.LENGTH_SHORT).show();

        db.collection("movies").document(deletedMovie)
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getApplicationContext(), deletedMovie + " was deleted", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Log.d("movies",  task.getException().toString());
                        }
                    }
                });

        movieList.remove(movieDeleteIndex);
        movieArray = new String[movieList.size()];
        int i = 0;
        for(Movie x : movieList){
            movieArray[i] = x.getMovieName();
            i++;
        }
        showMoviesAlertDialog("");
    }

    public void createMovieArray(){
        movieArray = new String[movieList.size()];
        int i = 0;
        for(Movie x : movieList){
            movieArray[i] = x.getMovieName();
            i++;
        }
    }

    public void fetchMovieArrayList(){
        movieArray = new String[movieList.size()];
//        movieList.clear();
        db.collection("movies").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                movieList.clear();
                for(DocumentSnapshot documentSnapshot : queryDocumentSnapshots ){
                    Movie movie = new Movie(documentSnapshot.getData());
                    movieList.add(movie);
                    createMovieArray();
                }
                if(action.equals("edit"))
                    showMoviesAlertDialog("edit");
                else if (action.equals("delete"))
                    showMoviesAlertDialog("delete");
                else
                    showMoviesAlertDialog("");
            }
        });
    }
}

