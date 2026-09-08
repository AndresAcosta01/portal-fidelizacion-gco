import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { MarcaRespuesta } from '../../../../core/services/marca';

@Component({
  selector: 'app-seccion-marcas',
  imports: [RouterLink],
  templateUrl: './seccion-marcas.html'
})
export class SeccionMarcas {

  marcas = input.required<MarcaRespuesta[]>();

}