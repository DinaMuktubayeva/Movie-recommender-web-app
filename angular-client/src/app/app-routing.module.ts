import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { MovieComponent } from "./movie/movie.component"
import { RecommendationsComponent } from './recommendations/recommendations.component';

const routes: Routes = [
  { path: 'movies', component: MovieComponent },
  { path: 'recommendations', component: RecommendationsComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})

export class AppRoutingModule { }