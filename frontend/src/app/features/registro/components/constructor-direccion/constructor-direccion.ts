import { Component, effect, input, output } from '@angular/core';
import { FormGroup, ReactiveFormsModule } from '@angular/forms';

@Component({
  selector: 'app-constructor-direccion',
  imports: [
    ReactiveFormsModule
  ],
  templateUrl: './constructor-direccion.html'
})
export class ConstructorDireccion {

  formularioDireccion =
    input.required<FormGroup>();

  direccionConstruida =
    output<string>();

  tiposVia = [
    'Calle',
    'Carrera',
    'Avenida',
    'Diagonal',
    'Transversal',
    'Circular'
  ];

  vistaPrevia = '';

  constructor() {

    effect((limpiar) => {

      const formulario =
        this.formularioDireccion();

      this.actualizarDireccion();

      const suscripcion =
        formulario.valueChanges
          .subscribe(() => {
            this.actualizarDireccion();
          });

      limpiar(() => {
        suscripcion.unsubscribe();
      });

    });

  }

  campoInvalido(
    nombreCampo: string
  ): boolean {

    const campo =
      this.formularioDireccion()
        .get(nombreCampo);

    return !!(
      campo &&
      campo.invalid &&
      (
        campo.touched ||
        campo.dirty
      )
    );

  }

  tieneError(
    nombreCampo: string,
    tipoError: string
  ): boolean {

    const campo =
      this.formularioDireccion()
        .get(nombreCampo);

    return !!(
      campo &&
      campo.hasError(tipoError) &&
      (
        campo.touched ||
        campo.dirty
      )
    );

  }

  private actualizarDireccion(): void {

    const direccion =
      this.formularioDireccion()
        .getRawValue();

    const partesVia = [
      direccion.tipoVia,
      direccion.numeroVia,
      direccion.letraVia,
      direccion.bis ? 'Bis' : ''
    ]
      .filter(Boolean)
      .join(' ');

    const partesCruce = [
      direccion.numeroCruce,
      direccion.letraCruce
    ]
      .filter(Boolean)
      .join(' ');

    let direccionParcial =
      partesVia;

    if (partesCruce) {

      direccionParcial +=
        ` # ${partesCruce}`;

    }

    if (direccion.numeroPlaca) {

      direccionParcial +=
        ` - ${direccion.numeroPlaca}`;

    }

    const complemento =
      direccion.complemento
        .trim();

    if (
      complemento &&
      direccionParcial
    ) {

      direccionParcial +=
        `, ${complemento}`;

    }

    this.vistaPrevia =
      direccionParcial
        .trim()
        .toUpperCase();

    if (
      this.formularioDireccion().valid
    ) {

      this.direccionConstruida.emit(
        this.vistaPrevia
      );

      return;

    }

    this.direccionConstruida.emit('');

  }

}