# Movie-Recommender-Web-App
The client (Angular) gets a list of movies to rate from the server (Spring Boot) and sends the posts the ratings back. It is then redirected to the recommendations page where it gets a list of recommended movies from the server.

1. Start the server by running the SpringBootServerApplication.java in 'src/main/java/com/movies'
2. Run the server with the command 'ng serve' in the directory ''
3. Go to localhost:4200/movies
4. Rate movies on scale from 0 to 10 (highest) and submit the ratings
5. Be redirected to the recommendations page

The server stores the data on movie and raters. Whenever the form is submitted, the server compares the user's ratings to the ratings of other raters and selects the movies which received high ratings from them and which have not been rated by the user.
