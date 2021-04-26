package com.movies.model.scrapper;

import java.io.FileWriter;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import com.movies.model.CSVParser;

/**
 * Has been used to create a new csv file using data from the original csv file
 * ("ratedmoviesfull.csv") and themoviedb.org API. The poster links in the
 * original file had to be updated.
 * 
 * Define your API key before using
 */

public class Scrapper {
    private static String API_KEY = "";

    public static void main(String[] args) {
        CSVParser parser = new CSVParser("ratedmoviesfull.csv");
        List<List<String>> csvBody = new ArrayList<>();

        // header in the new csv file
        String[] csvHeader = { "id", "title", "year", "country", "genre", "director", "minutes", "poster" };

        // take data from the original file line by line
        for (ArrayList<String> rec : parser) {
            try {
                // get the movie data
                String url = "https://api.themoviedb.org/3/find/tt" + rec.get(0) + "?api_key=" + API_KEY
                        + "&language=en-US&external_source=imdb_id";
                HttpRequest request = HttpRequest.newBuilder(new URI(url)).GET().build();
                HttpResponse<String> res = HttpClient.newBuilder().build().send(request,
                        HttpResponse.BodyHandlers.ofString());

                // extract the poster link from the json (which is stored as a string)
                int i = res.body().indexOf("poster_path") + "poster_path".length() + 4;
                int j = i + res.body().substring(i).indexOf("jpg") + "jpg".length();
                String poster = res.body().substring(i, j);

                // add the line to the new file
                csvBody.add(Arrays.asList(rec.get(0), rec.get(1), rec.get(2), rec.get(3), rec.get(4), rec.get(5),
                        rec.get(6), poster));

            } catch (Exception e) {
                System.err.println(e);
            }
        }

        // write data to the new file line by line
        try {
            FileWriter csvWriter = new FileWriter("movies.csv");

            for (int i = 0; i < csvHeader.length; i++) {
                csvWriter.append(csvHeader[i]);

                if (i != csvHeader.length - 1)
                    csvWriter.append(",");
                else
                    csvWriter.append("\n");
            }

            for (List<String> record : csvBody) {
                csvWriter.append(String.join(",", record));
                csvWriter.append("\n");
            }

            csvWriter.flush();
            csvWriter.close();

        } catch (Exception e) {
            System.err.println(e);
        }
    }
}