package com.movies.model;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Generates recommendations for the given rater based on ratings they have
 * given.
 * 
 * Works by computing similarities to other raters and returning a list of the
 * top rated movies from these raters, not seen by the given rater
 */

public class Recommender {
    public Recommender() {
        RaterDatabase.initialize("ratings.csv");
    }

    public int getRaterSize() {
        return RaterDatabase.size();
    }

    public String getID(String title) {
        return MovieDatabase.getMovie(title).getID();
    }

    private double getAverageByID(String id, int minimalRaters) {
        double ave = 0.0;
        int numRaters = 0;

        for (Rater rater : RaterDatabase.getRaters()) {
            ArrayList<String> items = rater.getItemsRated();

            for (String item : items) {
                if (item.equals(id)) {
                    ave += rater.getRating(item);
                    numRaters++;
                }
            }
        }

        if (numRaters >= minimalRaters)
            return ave / numRaters;
        else
            return 0.0;
    }

    public ArrayList<Rating> getAverageRatings(int minimalRaters) {
        ArrayList<Rating> ratings = new ArrayList<Rating>();
        ArrayList<String> movies = MovieDatabase.filterBy(new TrueFilter());
        double aveRating;

        for (String movie : movies) {
            aveRating = getAverageByID(movie, minimalRaters);
            Rating rating = new Rating(MovieDatabase.getTitle(movie), aveRating);

            if (aveRating != 0.0)
                ratings.add(rating);
        }

        return ratings;
    }

    public ArrayList<Rating> getAverageRatingsByFilter(int minimalRaters, Filter filterCriteria) {
        ArrayList<Rating> ratings = new ArrayList<Rating>();
        ArrayList<String> movies = MovieDatabase.filterBy(filterCriteria);
        double aveRating;

        for (String movie : movies) {
            aveRating = getAverageByID(movie, minimalRaters);
            Rating rating = new Rating(MovieDatabase.getTitle(movie), aveRating);

            if (aveRating != 0.0)
                ratings.add(rating);
        }

        return ratings;
    }

    private double dotProduct(Rater me, Rater r) {
        double dot_product = 0.0;

        for (String item : me.getItemsRated()) {
            if (r.hasRating(item))
                dot_product += (me.getRating(item) - 5.0) * (r.getRating(item) - 5.0);
        }

        return dot_product;
    }

    private ArrayList<Rating> getSimilarities(String id) {
        ArrayList<Rating> ratings = new ArrayList<Rating>();
        Rater me = RaterDatabase.getRater(id);

        for (Rater rater : RaterDatabase.getRaters()) {
            if (!rater.getID().equals(id)) {
                double similarity = dotProduct(rater, me);

                if (similarity > 0.0)
                    ratings.add(new Rating(rater.getID(), similarity));
            }
        }

        Collections.sort(ratings);
        return ratings;
    }

    /*
     * use the top raters in positive ratings include only those movies that have at
     * least minimal ratings from the top ratings compute weighted average:
     * similarity*rating they gave the movie total weighted average: sum / number of
     * ratings
     */
    public ArrayList<Rating> getSimilarRatings(String id, int numSimilarRaters, int minimalRaters) {
        ArrayList<Rating> similarRatings = new ArrayList<Rating>();
        ArrayList<Rating> ratings = getSimilarities(id);
        Rater me = RaterDatabase.getRater(id);

        TrueFilter truefilter = new TrueFilter();
        ArrayList<String> movies = MovieDatabase.filterBy(truefilter);

        /*
         * Put the top similar raters into an ArryaList
         */
        ArrayList<Rating> topRatings = new ArrayList<Rating>();
        numSimilarRaters = Math.min(numSimilarRaters, ratings.size());
        for (int i = 0; i < numSimilarRaters; i++) {
            topRatings.add(ratings.get(i));
        }

        /*
         * Iterate through movies not seen by the chosen rater Select those that have
         * the highest weighted average rating from the top similar raters The movies
         * that don't have enough ratings are
         */
        for (String item : movies) {
            if (!me.hasRating(item)) {
                double weighedAveRating = 0.0;
                int count = 0;

                for (int i = 0; i < numSimilarRaters; i++) {
                    String raterID = topRatings.get(i).getItem();
                    Rater rater = RaterDatabase.getRater(raterID);

                    if (rater.hasRating(item)) {
                        count++;
                        double hisRating = rater.getRating(item);
                        double similarity = topRatings.get(i).getValue();
                        weighedAveRating += similarity * hisRating;
                    }
                }

                if (count >= minimalRaters) {
                    similarRatings.add(new Rating(item, weighedAveRating / count));
                    System.out.println(item);
                }
            }
        }

        Collections.sort(similarRatings);
        return similarRatings;
    }

    public ArrayList<Rating> getSimilarRatingsByFilter(String id, int numSimilarRaters, int minimalRaters,
            Filter filterCriteria) {
        ArrayList<Rating> similarRatings = new ArrayList<Rating>();
        ArrayList<Rating> ratings = getSimilarities(id);
        Rater me = RaterDatabase.getRater(id);
        ArrayList<String> movies = MovieDatabase.filterBy(filterCriteria);

        /*
         * Put the top similar raters into an ArrayList
         */
        ArrayList<Rating> topRatings = new ArrayList<Rating>();
        for (int i = 0; i < numSimilarRaters; i++) {
            if (i >= ratings.size() - 1)
                break;
            topRatings.add(ratings.get(i));
        }

        /*
         * Iterate through movies not seen by the chosen rater Select those that have
         * the highest weighted average rating from the top similar raters The movies
         * that don't have enough ratings are
         */
        for (String item : movies) {
            if (me.hasRating(item)) {
                double weighedAveRating = 0.0;
                int count = 0;

                for (int i = 0; i < numSimilarRaters; i++) {
                    String raterID = topRatings.get(i).getItem();
                    Rater rater = RaterDatabase.getRater(raterID);
                    if (rater.hasRating(item)) {
                        count++;
                        double hisRating = rater.getRating(item);
                        double similarity = topRatings.get(i).getValue();
                        weighedAveRating += similarity * hisRating;
                    }
                }

                if (count >= minimalRaters) {
                    similarRatings.add(new Rating(item, weighedAveRating / count));
                }
            }
        }

        Collections.sort(similarRatings, Collections.reverseOrder());
        return similarRatings;
    }
}
