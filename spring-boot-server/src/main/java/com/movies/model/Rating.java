package com.movies.model;

// An immutable passive data object (PDO) to represent the rating data
public class Rating implements Comparable<Rating> {
    private String item; // movie id
    private double value; // rating given

    public Rating(String anItem, double aValue) {
        item = anItem;
        value = aValue;
    }

    public String getItem() {
        return item;
    }

    public double getValue() {
        return value;
    }

    public String toString() {
        return "[" + getItem() + ", " + getValue() + "]";
    }

    public int compareTo(Rating other) {
        if (value < other.value)
            return 1;
        if (value > other.value)
            return -1;

        return 0;
    }
}
