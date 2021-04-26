package com.movies.model;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Stores movie data in a hashmap
 */

public class MovieDatabase {
    private static HashMap<String, Movie> movies; // movie ids to movie objects

    public static void initialize(String moviefile) {
        if (movies == null) {
            movies = new HashMap<String, Movie>();
            loadMovies(moviefile);
        }
    }

    private static void initialize() {
        if (movies == null) {
            movies = new HashMap<String, Movie>();
             loadMovies("movies.csv");
        }
    }

    // Loads movie data from the given file
    private static void loadMovies(String moviefile) {
        CSVParser parser = new CSVParser(moviefile);

        for (ArrayList<String> rec : parser) {
            if (rec.size() == 8) {
                Movie movie = new Movie(rec.get(0), rec.get(1), rec.get(2), rec.get(3), rec.get(4), rec.get(5),
                        Integer.parseInt(rec.get(6)), rec.get(7));
                movies.put(movie.getID(), movie);
            }
        }
    }

    public static boolean containsID(String id) {
        initialize();
        return movies.containsKey(id);
    }

    public static int getYear(String id) {
        initialize();
        return movies.get(id).getYear();
    }

    public static String getGenres(String id) {
        initialize();
        return movies.get(id).getGenres();
    }

    public static String getTitle(String id) {
        initialize();
        return movies.get(id).getTitle();
    }

    public static Movie getMovie(String id) {
        initialize();
        return movies.get(id);
    }

    public static String getPoster(String id) {
        initialize();
        return movies.get(id).getPoster();
    }

    public static int getMinutes(String id) {
        initialize();
        return movies.get(id).getMinutes();
    }

    public static String getCountry(String id) {
        initialize();
        return movies.get(id).getCountry();
    }

    public static String getDirector(String id) {
        initialize();
        return movies.get(id).getDirector();
    }

    public static int size() {
        initialize();
        return movies.size();
    }

    public static ArrayList<String> filterBy(Filter f) {
        initialize();
        ArrayList<String> list = new ArrayList<String>();
        for (String id : movies.keySet()) {
            if (f.satisfies(id)) {
                list.add(id);
            }
        }

        return list;
    }
}
