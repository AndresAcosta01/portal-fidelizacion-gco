import { Component, input, output } from '@angular/core';

export interface DatosRevisionRegistro {
  tipoIdentificacion: string;
  numeroIdentificacion: string;
  nombres: string;
  apellidos: string;
  fechaNacimiento: string;
  pais: string;
  departamento: string;
  ciudad: string;
  direccion: string;
  marca: string;
  logoMarca: string | null;
}

@Component({
  selector: 'app-modal-confirmacion',
  imports: [],
  templateUrl: './modal-confirmacion.html'
})
export class ModalConfirmacion {

  datos = input.required<DatosRevisionRegistro>();

  corregir = output<void>();
  confirmar = output<void>();

}