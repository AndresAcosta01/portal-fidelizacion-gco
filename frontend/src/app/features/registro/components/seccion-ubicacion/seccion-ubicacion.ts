import { Component, input, output } from '@angular/core';
import { FormGroup, ReactiveFormsModule } from '@angular/forms';
import { PaisRespuesta } from '../../../../core/services/pais';
import { DepartamentoRespuesta } from '../../../../core/services/departamento';
import { CiudadRespuesta } from '../../../../core/services/ciudad';
import { SelectorBuscable } from '../../../../shared/selector-buscable/selector-buscable';

@Component({
  selector: 'app-seccion-ubicacion',
  imports: [ReactiveFormsModule, SelectorBuscable],
  templateUrl: './seccion-ubicacion.html'
})
export class SeccionUbicacion {
  formulario = input.required<FormGroup>();
  paises = input.required<PaisRespuesta[]>();
  departamentos = input.required<DepartamentoRespuesta[]>();
  ciudades = input.required<CiudadRespuesta[]>();
  paisSeleccionado = output<string>();
  departamentoSeleccionado = output<string>();

  seleccionarPais(idPais: string): void {
    this.formulario().get('idPais')?.setValue(idPais);
    this.formulario().get('idDepartamento')?.reset('');
    this.formulario().get('idCiudad')?.reset('');
    this.formulario().get('idCiudad')?.disable();

    if (!idPais) {
      this.formulario().get('idDepartamento')?.disable();
      this.paisSeleccionado.emit('');
      return;
    }

    this.formulario().get('idDepartamento')?.enable();
    this.paisSeleccionado.emit(idPais);
  }

  seleccionarDepartamento(idDepartamento: string): void {
    this.formulario().get('idDepartamento')?.setValue(idDepartamento);
    this.formulario().get('idCiudad')?.reset('');

    if (!idDepartamento) {
      this.formulario().get('idCiudad')?.disable();
      this.departamentoSeleccionado.emit('');
      return;
    }

    this.formulario().get('idCiudad')?.enable();
    this.departamentoSeleccionado.emit(idDepartamento);
  }

  seleccionarCiudad(idCiudad: string): void {
    this.formulario().get('idCiudad')?.setValue(idCiudad);
  }

  campoInvalido(nombreCampo: string): boolean {
    const campo = this.formulario().get(nombreCampo);
    return !!(campo && campo.invalid && campo.touched);
  }
}