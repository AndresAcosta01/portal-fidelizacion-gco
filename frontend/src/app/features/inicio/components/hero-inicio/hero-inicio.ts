import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { MarcaRespuesta } from '../../../../core/services/marca';

@Component({
  selector: 'app-hero-inicio',
  imports: [RouterLink],
  templateUrl: './hero-inicio.html'
})
export class HeroInicio {

  marcas = input.required<MarcaRespuesta[]>();

}