package com.movies.controllers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.movies.model.Recommender;
import com.movies.model.Movie;
import com.movies.model.MovieDatabase;
import com.movies.model.PopularMovieDatabase;
import com.movies.model.Rating;
import com.movies.model.Rater;
import com.movies.model.RaterDatabase;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class Controller {
    private List<Movie> movies;

    // Returs a list of movies suggested to users for rating
    // Their ratings will then be used to generate recommendations
    @GetMapping("/movies")
    public List<Movie> firstPage() {
        initialize();
        return movies;
    }

    // Gets user ratings on the given movies
    @PostMapping("/movies")
    public HttpStatus postBody(@RequestBody String data) {
        System.out.println("\nReceived " + data);
        addRater(data);
        return HttpStatus.OK;
    }

    // Returns recommendations for the rater added earlier
    @GetMapping("/recommendations")
    public List<Movie> sendRecommendations() {
        return generateRecommendationsFor(Integer.toString(RaterDatabase.size()));
    }

    // Initializes databases and the first list for rating
    public void initialize() {
        MovieDatabase.initialize("movies.csv");
        RaterDatabase.initialize("ratings.csv");
        PopularMovieDatabase.initialize();
        movies = createList();
    }

    // Creates a random list of movies for rating
    // This list consits of 8 popular movies and 2 other movies
    private static List<Movie> createList() {
        Set<Movie> res = new HashSet<>();
        Random rand = new Random();
        Integer idx = rand.nextInt(9999999);

        // Take 8 popular movies
        while (res.size() != 8) {
            while (!PopularMovieDatabase.containsID(idx.toString()))
                idx = rand.nextInt(9999999);

            res.add(PopularMovieDatabase.getMovie(idx.toString()));
            idx = rand.nextInt(9999999);
        }

        // Take 2 other movies
        while (res.size() != 10) {
            while (!MovieDatabase.containsID(idx.toString()))
                idx = rand.nextInt(9999999);

            res.add(MovieDatabase.getMovie(idx.toString()));
            idx = rand.nextInt(9999999);
        }

        return new ArrayList<>(res);
    }

    // Extracts the current user's ratings from the string and adds the data to the
    // database
    private void addRater(String data) {
        String[] values = data.split(",\"ratings\"", 2);
        String movie_ids = values[0].substring(values[0].indexOf("[") + 1, values[0].indexOf("]") + 1);
        String ratings_string = values[1].substring(values[1].indexOf("[") + 1, values[1].indexOf("]") + 1);

        List<String> movies = new ArrayList<>();
        List<Double> ratings = new ArrayList<>();

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < movie_ids.length(); i++) {
            if (movie_ids.charAt(i) == ']' || movie_ids.charAt(i) == ',') {
                movies.add(sb.toString());
                sb.setLength(0);
            } else if ('0' <= movie_ids.charAt(i) && movie_ids.charAt(i) <= '9') {
                sb.append(movie_ids.charAt(i));
            }
        }

        for (int i = 0; i < ratings_string.length(); i++) {
            if (ratings_string.charAt(i) == ']' || ratings_string.charAt(i) == ',') {
                ratings.add(Double.parseDouble(sb.toString()));
                sb.setLength(0);
            } else if ('0' <= ratings_string.charAt(i) && ratings_string.charAt(i) <= '9') {
                sb.append(ratings_string.charAt(i));
            }
        }

        Rater rater = new Rater(Integer.toString(RaterDatabase.size()));
        for (int i = 0; i < movies.size(); i++)
            rater.addRating(movies.get(i), ratings.get(i));

        RaterDatabase.addRater(rater);
    }

    // Gets recommendations for the user
    private List<Movie> generateRecommendationsFor(String raterID) {
        System.out.println("Get recommendations for " + raterID);
        List<Movie> movies = new ArrayList<>();
        Recommender system = new Recommender();
        ArrayList<Rating> similar = system.getSimilarRatings(raterID, 10, 2);
        int total = similar.size();
        int recommended = 10;

        if (total != 0) {
            recommended = Math.min(recommended, total);

            for (int i = 0; i < recommended; i++) {
                String id = similar.get(i).getItem();
                movies.add(MovieDatabase.getMovie(id));
                System.out.println(MovieDatabase.getMovie(id).toString());
            }
        }

        return movies;
    }
}