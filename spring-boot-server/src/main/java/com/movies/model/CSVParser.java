package com.movies.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
import org.springframework.util.ResourceUtils;

/**
 * Parses a csv file with headers by storing lines in a list and extracting
 * values corresponding to given columns
 */

public class CSVParser implements Iterable<ArrayList<String>> {
  private ArrayList<String> headers; // csv headers
  private ArrayList<ArrayList<String>> records; // csv data

  public CSVParser(String filename) {
    headers = new ArrayList<String>();
    records = new ArrayList<ArrayList<String>>();
    
    try {
      File file = ResourceUtils.getFile("classpath:" + filename);

      try (Scanner scanner = new Scanner(new BufferedReader(new FileReader(file)))) {
        int i = 0;

        // extracting lines and storing in the list
        while (scanner.hasNext()) {
          if (i == 0)
            addColumnTitles(scanner.nextLine());
          else
            records.add(getRecord(scanner.nextLine()));

          i++;
        }
      } catch (Exception e) {
        System.out.println(filename + ": " + e.toString());
      }

    } catch (Exception e) {
      System.err.println(e.toString());
    }
  }

  private void addColumnTitles(String line) {
    try (Scanner scanner = new Scanner(line)) {
      scanner.useDelimiter(",");
      while (scanner.hasNext())
        headers.add(scanner.next());
    }
  }

  // Convertes a string line into an array of data by splitting by delimeters
  private ArrayList<String> getRecord(String line) {
    ArrayList<String> values = new ArrayList<String>();

    // this regexp handles commas inside ""
    String[] tokens = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);

    for (String t : tokens)
      values.add(t);

    return values;

  }

  // returns a record in the given line
  public String get(String data, ArrayList<String> arr) {
    for (int i = 0; i < headers.size(); i++) {
      if (headers.get(i).equals(data))
        return arr.get(i);
    }

    return "";
  }

  @Override
  public Iterator<ArrayList<String>> iterator() {
    Iterator<ArrayList<String>> it = new Iterator<ArrayList<String>>() {
      private int currentIndex = 0;

      @Override
      public boolean hasNext() {
        return records != null && currentIndex < records.size() && records.get(currentIndex) != null;
      }

      @Override
      public ArrayList<String> next() {
        return records.get(currentIndex++);
      }

      @Override
      public void remove() {
        throw new UnsupportedOperationException();
      }
    };

    return it;
  }
}