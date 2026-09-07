import { Component, input } from '@angular/core';
import { FormGroup, ReactiveFormsModule } from '@angular/forms';

import { TipoIdentificacionRespuesta } from '../../../../core/services/tipo-identificacion';

@Component({
  selector: 'app-seccion-datos-personales',
  imports: [ReactiveFormsModule],
  templateUrl: './seccion-datos-personales.html'
})
export class SeccionDatosPersonales {

  formulario = input.required<FormGroup>();
  tiposIdentificacion = input.required<TipoIdentificacionRespuesta[]>();

  campoInvalido(nombreCampo: string): boolean {
    const campo = this.formulario().get(nombreCampo);

    return !!(campo && campo.invalid && (campo.touched || campo.dirty));
  }

  tieneError(nombreCampo: string, tipoError: string): boolean {
    const campo = this.formulario().get(nombreCampo);

    return !!(campo && campo.hasError(tipoError) && (campo.touched || campo.dirty));
  }

}