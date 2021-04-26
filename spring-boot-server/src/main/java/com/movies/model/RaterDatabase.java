package com.movies.model;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Stores data about the raters that made reviews on movies in MovieDatabase
 */

public class RaterDatabase {
    private static HashMap<String, Rater> raters; // rater id to rater object

    private static void initialize() {
        if (raters == null)
            initialize("ratings.csv");
    }

    public static void initialize(String filename) {
        if (raters == null) {
            raters = new HashMap<String, Rater>();
            addRatings(filename);
        }
        System.out.println(RaterDatabase.size());
    }

    public static void addRatings(String filename) {
        initialize();
        CSVParser csvp = new CSVParser(filename);

        for (ArrayList<String> rec : csvp) {
            String id = csvp.get("rater_id", rec);
            String item = csvp.get("movie_id", rec);
            String rating = csvp.get("rating", rec);

            if (rating.isEmpty())
                addRaterRating(id, item, 0.0);
            else
                addRaterRating(id, item, Double.parseDouble(rating));
        }
    }

    public static void addRater(Rater rater) {

        for (String item : rater.getItemsRated()) {
            System.out.println(item + "->" + rater.getRating(item));
        }
        raters.put(rater.getID(), rater);
        System.out.println("Added rater " + rater.getID());
    }

    public void updateRater(String id, Rater rater) {
        raters.replace(id, rater);
    }

    public static void addRaterRating(String raterID, String movieID, double rating) {
        initialize();
        Rater rater = null;

        if (raters.containsKey(raterID)) {
            rater = raters.get(raterID);
        } else {
            rater = new Rater(raterID);
            raters.put(raterID, rater);
        }

        rater.addRating(movieID, rating);
    }

    public static Rater getRater(String id) {
        initialize();
        return raters.get(id);
    }

    public static ArrayList<Rater> getRaters() {
        initialize();
        ArrayList<Rater> list = new ArrayList<Rater>(raters.values());
        return list;
    }

    public static int size() {
        initialize();
        return raters.size();
    }
}
