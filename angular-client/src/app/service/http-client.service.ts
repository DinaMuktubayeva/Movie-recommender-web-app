import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

export class Movie {
  constructor(
    public id: string,
    public title: string,
    public year: string,
    public country: string,
    public genres: string,
    public director: string,
    public minutes: string,
    public poster: string
  ) { }
}

@Injectable({
  providedIn: 'root'
})

export class HttpClientService {
  constructor(
    private httpClient: HttpClient
  ) {
  }

  // gets a list of movies
  getMovies() {
    console.log("getMovies()");
    return this.httpClient.get<Movie[]>('http://localhost:8080/movies');
  }

  // posts user ratings
  postRatings(data: any) {
    console.log("postRatings()");
    return this.httpClient.post<String>('http://localhost:8080/movies', JSON.stringify(data));
  }

  // gets a list of recommended movies
  getRecommendations(){
    console.log("getRecommendations()");
    return this.httpClient.get<Movie[]>('http://localhost:8080/recommendations');
  }
}