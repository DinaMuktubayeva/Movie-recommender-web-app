import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, FormArray, FormControl } from "@angular/forms";
import { HttpClient } from '@angular/common/http';
import { HttpClientService } from '../service/http-client.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-movie',
  templateUrl: './movie.component.html',
  styleUrls: ['./movie.component.sass']
})

// Represents a form that asks users to rate movies received from the server
export class MovieComponent implements OnInit {
  movies: any[] = [];
  form!: FormGroup;

  constructor(
    private httpClientService: HttpClientService,
    private httpClient: HttpClient,
    private fb: FormBuilder,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.httpClientService.getMovies().subscribe(
      response => this.handleSuccessfulResponse(response),
    );

    this.form = this.fb.group({
      movie_ids: this.fb.array([]),
      ratings: this.fb.array([])
    })
  }

  handleSuccessfulResponse(response: any) {
    this.movies = response;

    // add the initial values
    this.movies.map(movie => {
      this.movieIds.push(new FormControl(movie.id));
      this.ratingForms.push(new FormControl(0));
    })
  }

  get movieIds() {
    return this.form.get('movie_ids') as FormArray
  }

  get ratingForms() {
    return this.form.get('ratings') as FormArray
  }

  addRating(i: number, event: any) {
    this.ratingForms.at(i).patchValue(event.target.value);
  }

  // send user ratings and redirect to the recommendations page
  onSubmit() {    
    this.httpClientService.postRatings(this.form.value).subscribe();
    this.router.navigate(['/recommendations'])
  }
}
