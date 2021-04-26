package com.movies.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A smaller version of MovieDatabase that stores movies that have more than 25
 * reviews in RaterDatabase
 */

public class PopularMovieDatabase {
    private static HashMap<String, Movie> movies; // movie ids to movie objects

    public static void initialize() {
        movies = new HashMap<>();
        RaterDatabase.initialize("ratings.csv");
        List<Rater> raters = RaterDatabase.getRaters();
        Map<String, Integer> map = new HashMap<>();

        // count the number of review for every movie
        for (Rater rater : raters) {
            for (String movieID : rater.getItemsRated())
                map.put(movieID, 1 + map.getOrDefault(movieID, 0));
        }

        // store popular movies
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            if (entry.getValue() > 25)
                movies.put(entry.getKey(), MovieDatabase.getMovie(entry.getKey()));
        }
    }

    public static boolean containsID(String id) {
        return movies.containsKey(id);
    }

    public static Movie getMovie(String id) {
        return movies.get(id);
    }

    public static void print() {
        for (Map.Entry<String, Movie> entry : movies.entrySet())
            System.out.println(entry.getValue().toString());
    }
}
