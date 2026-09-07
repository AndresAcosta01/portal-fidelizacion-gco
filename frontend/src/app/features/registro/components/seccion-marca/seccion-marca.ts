import { Component, input } from '@angular/core';
import { FormGroup, ReactiveFormsModule } from '@angular/forms';

import { MarcaRespuesta } from '../../../../core/services/marca';

@Component({
  selector: 'app-seccion-marca',
  imports: [
    ReactiveFormsModule
  ],
  templateUrl: './seccion-marca.html'
})
export class SeccionMarca {

  formulario =
    input.required<FormGroup>();

  marcas =
    input.required<MarcaRespuesta[]>();

  campoInvalido(
    nombreCampo: string
  ): boolean {

    const campo =
      this.formulario()
        .get(nombreCampo);

    return !!(
      campo &&
      campo.invalid &&
      campo.touched
    );

  }

}