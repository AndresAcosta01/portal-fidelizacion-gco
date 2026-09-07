import { Component, inject, OnInit, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { finalize, forkJoin } from 'rxjs';

import { Marca, MarcaRespuesta } from '../../core/services/marca';
import { TipoIdentificacion, TipoIdentificacionRespuesta } from '../../core/services/tipo-identificacion';
import { Pais, PaisRespuesta } from '../../core/services/pais';
import { Departamento, DepartamentoRespuesta } from '../../core/services/departamento';
import { Ciudad, CiudadRespuesta } from '../../core/services/ciudad';

import { SeccionDatosPersonales } from './components/seccion-datos-personales/seccion-datos-personales';
import { SeccionUbicacion } from './components/seccion-ubicacion/seccion-ubicacion';
import { ConstructorDireccion } from './components/constructor-direccion/constructor-direccion';
import { SeccionMarca } from './components/seccion-marca/seccion-marca';
import { DatosRevisionRegistro, ModalConfirmacion } from './components/modal-confirmacion/modal-confirmacion';

@Component({
  selector: 'app-registro',
  imports: [
    ReactiveFormsModule,
    RouterLink,
    SeccionDatosPersonales,
    SeccionUbicacion,
    ConstructorDireccion,
    SeccionMarca,
    ModalConfirmacion
  ],
  templateUrl: './registro.html'
})
export class Registro implements OnInit {

  tiposIdentificacion = signal<TipoIdentificacionRespuesta[]>([]);
  paises = signal<PaisRespuesta[]>([]);
  departamentos = signal<DepartamentoRespuesta[]>([]);
  ciudades = signal<CiudadRespuesta[]>([]);
  marcas = signal<MarcaRespuesta[]>([]);

  cargandoFormulario = signal(true);
  errorCargaFormulario = signal(false);

  direccionConstruida = '';
  mostrarModalConfirmacion = false;
  datosRevision: DatosRevisionRegistro | null = null;

  private readonly tipoIdentificacionServicio = inject(TipoIdentificacion);
  private readonly paisServicio = inject(Pais);
  private readonly departamentoServicio = inject(Departamento);
  private readonly ciudadServicio = inject(Ciudad);
  private readonly marcaServicio = inject(Marca);
  private readonly formBuilder = inject(FormBuilder);
  private readonly ruta = inject(ActivatedRoute);

  formulario = this.formBuilder.nonNullable.group({
    idTipoIdentificacion: ['', Validators.required],
    numeroIdentificacion: ['', Validators.required],
    nombres: ['', Validators.required],
    apellidos: ['', Validators.required],
    fechaNacimiento: ['', Validators.required],
    idPais: ['', Validators.required],
    idDepartamento: [{ value: '', disabled: true }, Validators.required],
    idCiudad: [{ value: '', disabled: true }, Validators.required],
    idMarca: ['', Validators.required],

    direccion: this.formBuilder.nonNullable.group({
      tipoVia: ['', Validators.required],
      numeroVia: ['', [Validators.required, Validators.pattern(/^[0-9]+$/)]],
      letraVia: ['', Validators.pattern(/^[A-Za-z]*$/)],
      bis: [false],
      numeroCruce: ['', [Validators.required, Validators.pattern(/^[0-9]+$/)]],
      letraCruce: ['', Validators.pattern(/^[A-Za-z]*$/)],
      numeroPlaca: ['', [Validators.required, Validators.pattern(/^[0-9]+$/)]],
      complemento: ['']
    })
  });

  ngOnInit(): void {
    this.cargarDatosFormulario();
  }

  cargarDatosFormulario(): void {
    this.cargandoFormulario.set(true);
    this.errorCargaFormulario.set(false);

    forkJoin({
      tiposIdentificacion: this.tipoIdentificacionServicio.obtenerTiposIdentificacion(),
      paises: this.paisServicio.obtenerPaises(),
      departamentos: this.departamentoServicio.obtenerDepartamentos(),
      ciudades: this.ciudadServicio.obtenerCiudades(),
      marcas: this.marcaServicio.obtenerMarcas()
    })
      .pipe(finalize(() => this.cargandoFormulario.set(false)))
      .subscribe({
        next: (respuesta) => {
          this.tiposIdentificacion.set(respuesta.tiposIdentificacion.filter(tipo => tipo.activo));
          this.paises.set(respuesta.paises.filter(pais => pais.activo));
          this.departamentos.set(respuesta.departamentos.filter(departamento => departamento.activo));
          this.ciudades.set(respuesta.ciudades.filter(ciudad => ciudad.activo));
          this.marcas.set(respuesta.marcas.filter(marca => marca.activo));

          this.preseleccionarMarca();
        },

        error: (error) => {
          this.errorCargaFormulario.set(true);
          console.error('Error al cargar los datos del formulario:', error);
        }
      });
  }

  preseleccionarMarca(): void {
    const idMarca = this.ruta.snapshot.queryParamMap.get('marca');

    if (!idMarca) {
      return;
    }

    const marcaExiste = this.marcas().some(marca => marca.id === idMarca);

    if (marcaExiste) {
      this.formulario.controls.idMarca.setValue(idMarca);
    }
  }

  actualizarDireccion(direccion: string): void {
    this.direccionConstruida = direccion;
  }

  revisarFormulario(): void {
    if (this.formulario.invalid) {
      this.formulario.markAllAsTouched();
      return;
    }

    const datos = this.formulario.getRawValue();

    const tipoIdentificacion = this.tiposIdentificacion().find(tipo => tipo.id === datos.idTipoIdentificacion);
    const pais = this.paises().find(pais => pais.id === datos.idPais);
    const departamento = this.departamentos().find(departamento => departamento.id === datos.idDepartamento);
    const ciudad = this.ciudades().find(ciudad => ciudad.id === datos.idCiudad);
    const marca = this.marcas().find(marca => marca.id === datos.idMarca);

    this.datosRevision = {
      tipoIdentificacion: tipoIdentificacion?.nombre ?? '',
      numeroIdentificacion: datos.numeroIdentificacion,
      nombres: datos.nombres,
      apellidos: datos.apellidos,
      fechaNacimiento: datos.fechaNacimiento,
      pais: pais?.nombre ?? '',
      departamento: departamento?.nombre ?? '',
      ciudad: ciudad?.nombre ?? '',
      direccion: this.direccionConstruida,
      marca: marca?.nombre ?? '',
      logoMarca: marca?.logoUrl ?? null
    };

    this.mostrarModalConfirmacion = true;
  }

  cerrarModalConfirmacion(): void {
    this.mostrarModalConfirmacion = false;
  }

  confirmarRegistro(): void {
    console.log('Confirmación aceptada:', this.formulario.getRawValue());
  }

}