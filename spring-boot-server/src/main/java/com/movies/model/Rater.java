package com.movies.model;

import java.util.ArrayList;
import java.util.HashMap;

// Represents rater dara

public class Rater {
    private String myID;
    private HashMap<String, Rating> myRatings; // movie id to rating given

    public Rater(String id) {
        myID = id;
        myRatings = new HashMap<String, Rating>();
    }

    public void addRating(String item, double rating) {
        myRatings.put(item, new Rating(item, rating));
    }

    public boolean hasRating(String item) {
        return myRatings.containsKey(item);
    }

    public String getID() {
        return myID;
    }

    public double getRating(String item) {
        return myRatings.get(item).getValue();
    }

    public int numRatings() {
        return myRatings.size();
    }

    public ArrayList<String> getItemsRated() {
        ArrayList<String> list = new ArrayList<String>();

        for (String item : myRatings.keySet())
            list.add(item);

        return list;
    }
}
