import { Component, input } from '@angular/core';
import { FormGroup, ReactiveFormsModule } from '@angular/forms';

import { PaisRespuesta } from '../../../../core/services/pais';
import { DepartamentoRespuesta } from '../../../../core/services/departamento';
import { CiudadRespuesta } from '../../../../core/services/ciudad';

@Component({
  selector: 'app-seccion-ubicacion',
  imports: [
    ReactiveFormsModule
  ],
  templateUrl: './seccion-ubicacion.html'
})
export class SeccionUbicacion {

  formulario = input.required<FormGroup>();

  paises =
    input.required<PaisRespuesta[]>();

  departamentos =
    input.required<DepartamentoRespuesta[]>();

  ciudades =
    input.required<CiudadRespuesta[]>();

  departamentosFiltrados: DepartamentoRespuesta[] = [];

  ciudadesFiltradas: CiudadRespuesta[] = [];

  cambiarPais(): void {

    const idPais =
      this.formulario()
        .get('idPais')
        ?.value;

    this.formulario()
      .get('idDepartamento')
      ?.reset('');

    this.formulario()
      .get('idCiudad')
      ?.reset('');

    this.ciudadesFiltradas = [];

    this.formulario()
      .get('idCiudad')
      ?.disable();

    if (!idPais) {

      this.departamentosFiltrados = [];

      this.formulario()
        .get('idDepartamento')
        ?.disable();

      return;

    }

    this.departamentosFiltrados =
      this.departamentos().filter(
        departamento =>
          departamento.idPais === idPais
      );

    this.formulario()
      .get('idDepartamento')
      ?.enable();

  }

  cambiarDepartamento(): void {

    const idDepartamento =
      this.formulario()
        .get('idDepartamento')
        ?.value;

    this.formulario()
      .get('idCiudad')
      ?.reset('');

    if (!idDepartamento) {

      this.ciudadesFiltradas = [];

      this.formulario()
        .get('idCiudad')
        ?.disable();

      return;

    }

    this.ciudadesFiltradas =
      this.ciudades().filter(
        ciudad =>
          ciudad.idDepartamento === idDepartamento
      );

    this.formulario()
      .get('idCiudad')
      ?.enable();

  }

  campoInvalido(nombreCampo: string): boolean {

    const campo =
      this.formulario().get(nombreCampo);

    return !!(
      campo &&
      campo.invalid &&
      campo.touched
    );

  }

}