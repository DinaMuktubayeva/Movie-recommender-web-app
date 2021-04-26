package com.movies.model;

/**
 * Chooses all items
 */

public class TrueFilter implements Filter {
	@Override
	public boolean satisfies(String id) {
		return true;
	}
}