import { Component, input, output } from '@angular/core';

@Component({
  selector: 'app-modal-exito',
  imports: [],
  templateUrl: './modal-exito.html'
})
export class ModalExito {

  nombre = input.required<string>();
  marca = input.required<string>();
  logoMarca = input<string | null>(null);

  aceptar = output<void>();

}