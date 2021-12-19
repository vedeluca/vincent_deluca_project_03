package com.example.vincent_deluca_project_03;

import java.io.Serializable;

public class MovieModel implements Serializable {
    public String description;
    public String director;
    public String length;
    public float rating;
    public String stars;
    public String title;
    public String url;
    public String year;

    public MovieModel(String description,
                      String director,
                      String length,
                      float rating,
                      String stars,
                      String title,
                      String url,
                      String year) {
        this.description = description;
        this.director = director;
        this.length = length;
        this.rating = rating;
        this.stars = stars;
        this.title = title;
        this.url = url;
        this.year = year;
    }
}
