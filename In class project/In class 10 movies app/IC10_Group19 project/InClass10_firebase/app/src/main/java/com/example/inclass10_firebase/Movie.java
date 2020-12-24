package com.example.inclass10_firebase;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Movie implements Serializable {
    private String movieName;
    private String description;
    private String genre;
    private int rating;
    private int year;
    private String imdb;

    @Override
    public String toString() {
        return "Movie{" +
                "movieName='" + movieName + '\'' +
                ", description='" + description + '\'' +
                ", genre='" + genre + '\'' +
                ", rating=" + rating +
                ", year=" + year +
                ", imdb='" + imdb + '\'' +
                '}';
    }

    public Movie(String movieName, String description, String genre, int rating, int year, String imdb) {
        this.movieName = movieName;
        this.description = description;
        this.genre = genre;
        this.rating = rating;
        this.year = year;
        this.imdb = imdb;
    }

    public Movie(Map<String,Object> movieMap){
        this.movieName = (String) movieMap.get("movieName");
        this.description = (String)movieMap.get("description");
        this.genre = (String)movieMap.get("genre");
        this.rating = (int)(long) movieMap.get("rating");
        this.year = (int)(long)movieMap.get("year");
        this.imdb = (String)movieMap.get("imdb");
    }

    public Map toHashMap(){
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("movieName", this.movieName);
        userMap.put("description", this.description);
        userMap.put("genre", this.genre);
        userMap.put("rating", this.rating);
        userMap.put("year", this.year);
        userMap.put("imdb", this.imdb);

        return userMap;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getImdb() {
        return imdb;
    }

    public void setImdb(String imdb) {
        this.imdb = imdb;
    }
}
