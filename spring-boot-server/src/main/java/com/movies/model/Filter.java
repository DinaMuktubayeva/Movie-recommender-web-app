package com.movies.model;

/**
 * Interface for filter applied to movies
 */

public interface Filter {
	public boolean satisfies(String id);
}
