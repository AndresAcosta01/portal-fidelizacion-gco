import { HttpErrorResponse } from '@angular/common/http';
import { Component, inject, OnInit, signal } from '@angular/core';
import { AbstractControl, FormBuilder, ReactiveFormsModule, ValidationErrors, ValidatorFn, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { finalize, forkJoin } from 'rxjs';
import { Marca, MarcaRespuesta } from '../../core/services/marca';
import { TipoIdentificacion, TipoIdentificacionRespuesta } from '../../core/services/tipo-identificacion';
import { Pais, PaisRespuesta } from '../../core/services/pais';
import { Departamento, DepartamentoRespuesta } from '../../core/services/departamento';
import { Ciudad, CiudadRespuesta } from '../../core/services/ciudad';
import { Cliente, ClienteRegistro } from '../../core/services/cliente';
import { SeccionDatosPersonales } from './components/seccion-datos-personales/seccion-datos-personales';
import { SeccionUbicacion } from './components/seccion-ubicacion/seccion-ubicacion';
import { ConstructorDireccion } from './components/constructor-direccion/constructor-direccion';
import { SeccionMarca } from './components/seccion-marca/seccion-marca';
import { DatosRevisionRegistro, ModalConfirmacion } from './components/modal-confirmacion/modal-confirmacion';
import { ModalExito } from './components/modal-exito/modal-exito';

const fechaNoFutura: ValidatorFn = (control: AbstractControl): ValidationErrors | null => {
  if (!control.value) {
    return null;
  }
  const [anio, mes, dia] = control.value.split('-').map(Number);
  const fechaSeleccionada = new Date(anio, mes - 1, dia);
  const hoy = new Date();
  hoy.setHours(0, 0, 0, 0);
  return fechaSeleccionada > hoy ? { fechaFutura: true } : null;
};

@Component({
  selector: 'app-registro',
  imports: [
    ReactiveFormsModule,
    RouterLink,
    SeccionDatosPersonales,
    SeccionUbicacion,
    ConstructorDireccion,
    SeccionMarca,
    ModalConfirmacion,
    ModalExito
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
  mostrarModalConfirmacion = signal(false);
  mostrarModalExito = signal(false);
  guardandoRegistro = signal(false);
  errorRegistro = signal('');
  direccionConstruida = '';
  datosRevision: DatosRevisionRegistro | null = null;
  private readonly tipoIdentificacionServicio = inject(TipoIdentificacion);
  private readonly paisServicio = inject(Pais);
  private readonly departamentoServicio = inject(Departamento);
  private readonly ciudadServicio = inject(Ciudad);
  private readonly marcaServicio = inject(Marca);
  private readonly clienteServicio = inject(Cliente);
  private readonly formBuilder = inject(FormBuilder);
  private readonly ruta = inject(ActivatedRoute);
  private readonly router = inject(Router);

  formulario = this.formBuilder.nonNullable.group({
    idTipoIdentificacion: ['', Validators.required],
    numeroIdentificacion: ['', Validators.required],
    nombres: ['', Validators.required],
    apellidos: ['', Validators.required],
    fechaNacimiento: ['', [Validators.required, fechaNoFutura]],
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
      marcas: this.marcaServicio.obtenerMarcas()
    })
      .pipe(finalize(() => this.cargandoFormulario.set(false)))
      .subscribe({
        next: (respuesta) => {
          this.tiposIdentificacion.set(respuesta.tiposIdentificacion.filter(tipo => tipo.activo));
          this.paises.set(this.ordenarPorNombre(respuesta.paises.filter(pais => pais.activo)));
          this.marcas.set(respuesta.marcas.filter(marca => marca.activo));
          this.preseleccionarMarca();
        },
        error: (error) => {
          this.errorCargaFormulario.set(true);
          console.error('Error al cargar los datos del formulario:', error);
        }
      });
  }

  cargarDepartamentos(idPais: string): void {
    this.departamentos.set([]);
    this.ciudades.set([]);
    if (!idPais) {
      return;
    }
    this.departamentoServicio.obtenerDepartamentosPorPais(idPais)
      .subscribe({
        next: (respuesta) => {
          this.departamentos.set(this.ordenarPorNombre(respuesta.filter(departamento => departamento.activo)));
        },
        error: (error) => {
          this.departamentos.set([]);
          console.error('Error al cargar los departamentos:', error);
        }
      });
  }

  cargarCiudades(idDepartamento: string): void {
    this.ciudades.set([]);
    if (!idDepartamento) {
      return;
    }
    this.ciudadServicio.obtenerCiudadesPorDepartamento(idDepartamento)
      .subscribe({
        next: (respuesta) => {
          this.ciudades.set(this.ordenarPorNombre(respuesta.filter(ciudad => ciudad.activo)));
        },
        error: (error) => {
          this.ciudades.set([]);
          console.error('Error al cargar las ciudades:', error);
        }
      });
  }

  private ordenarPorNombre<T extends { nombre: string }>(registros: T[]): T[] {
    return [...registros].sort((a, b) => a.nombre.trim().localeCompare(b.nombre.trim(), 'es', { sensitivity: 'base' }));
  }

  preseleccionarMarca(): void {
    const marcaParametro = this.ruta.snapshot.queryParamMap.get('marca');
    if (!marcaParametro) {
      return;
    }
    const marcaSeleccionada = this.marcas().find(marca =>
      marca.id === marcaParametro ||
      marca.nombre.toLowerCase() === marcaParametro.toLowerCase()
    );
    if (marcaSeleccionada) {
      this.formulario.controls.idMarca.setValue(marcaSeleccionada.id);
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
    this.errorRegistro.set('');
    this.mostrarModalConfirmacion.set(true);
  }

  cerrarModalConfirmacion(): void {
    if (this.guardandoRegistro()) {
      return;
    }
    this.errorRegistro.set('');
    this.mostrarModalConfirmacion.set(false);
  }

  confirmarRegistro(): void {
    if (this.formulario.invalid || this.guardandoRegistro()) {
      return;
    }
    const datos = this.formulario.getRawValue();
    const cliente: ClienteRegistro = {
      numeroIdentificacion: datos.numeroIdentificacion.trim(),
      nombres: datos.nombres.trim(),
      apellidos: datos.apellidos.trim(),
      fechaNacimiento: datos.fechaNacimiento,
      direccion: this.direccionConstruida,
      idTipoIdentificacion: datos.idTipoIdentificacion,
      idCiudad: datos.idCiudad,
      idMarca: datos.idMarca
    };
    this.guardandoRegistro.set(true);
    this.errorRegistro.set('');
    this.clienteServicio.registrarCliente(cliente)
      .pipe(finalize(() => this.guardandoRegistro.set(false)))
      .subscribe({
        next: () => {
          this.mostrarModalConfirmacion.set(false);
          this.mostrarModalExito.set(true);
        },
        error: (error: HttpErrorResponse) => {
          const mensajeBackend = error.error?.detail || error.error?.message;
          if (error.status === 409) {
            this.errorRegistro.set(mensajeBackend || 'Ya existe un cliente con ese tipo y número de identificación.');
            return;
          }
          if (error.status === 400) {
            this.errorRegistro.set(mensajeBackend || 'Algunos datos del registro no son válidos. Revisa la información e inténtalo nuevamente.');
            return;
          }
          if (error.status === 404) {
            this.errorRegistro.set(mensajeBackend || 'Uno de los datos seleccionados ya no se encuentra disponible.');
            return;
          }
          this.errorRegistro.set('No fue posible completar el registro. Inténtalo nuevamente.');
        }
      });
  }

  aceptarRegistroExitoso(): void {
    this.mostrarModalExito.set(false);
    this.router.navigate(['/']);
  }
}