import { Component, OnInit } from '@angular/core';
import { HttpClientService } from '../service/http-client.service';

@Component({
  selector: 'app-recommendations',
  templateUrl: './recommendations.component.html',
  styleUrls: ['./recommendations.component.sass']
})

// Shows a list of recommendations received from the server
export class RecommendationsComponent implements OnInit {
  movies: any[] = [];

  constructor(
    private httpClientService: HttpClientService,
  ) { }

  ngOnInit(): void {
    this.httpClientService.getRecommendations().subscribe(
      response => this.handleSuccessfulResponse(response),
    );
  }

  handleSuccessfulResponse(response: any) {
    this.movies = response;
  }
}
