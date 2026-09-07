import { Component, inject } from '@angular/core';
import { Router, RouterLink } from '@angular/router';

@Component({
  selector: 'app-header',
  imports: [RouterLink],
  templateUrl: './header.html'
})
export class Header {

  private readonly router = inject(Router);

  esRegistro(): boolean {
    return this.router.url.startsWith('/registro');
  }

}