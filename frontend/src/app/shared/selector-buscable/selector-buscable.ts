import { Component, computed, effect, ElementRef, HostListener, inject, input, output, signal, untracked } from '@angular/core';

export interface OpcionSelector {
    id: string;
    nombre: string;
}

@Component({
    selector: 'app-selector-buscable',
    templateUrl: './selector-buscable.html'
})
export class SelectorBuscable {
    opciones = input.required<OpcionSelector[]>();
    placeholder = input('Selecciona una opción');
    deshabilitado = input(false);
    seleccion = output<string>();
    texto = signal('');
    abierto = signal(false);
    private readonly elemento = inject(ElementRef<HTMLElement>);

    opcionesFiltradas = computed(() => {
        const busqueda = this.normalizar(this.texto());
        const opciones = this.opciones();

        if (!busqueda) {
            return opciones;
        }

        return opciones.filter(opcion =>
            this.normalizar(opcion.nombre).startsWith(busqueda)
        );
    });

    constructor() {
        effect(() => {
            const opciones = this.opciones();
            const textoActual = untracked(this.texto);

            if (textoActual && !opciones.some(opcion => opcion.nombre === textoActual)) {
                this.texto.set('');
            }
        });
    }

    escribir(evento: Event): void {
        const valor = (evento.target as HTMLInputElement).value;
        this.texto.set(valor);
        this.abierto.set(true);
        this.seleccion.emit('');
    }

    limpiar(): void {
        this.texto.set('');
        this.seleccion.emit('');
        this.abierto.set(true);
    }

    abrir(): void {
        if (!this.deshabilitado()) {
            this.abierto.set(true);
        }
    }

    seleccionar(opcion: OpcionSelector): void {
        this.texto.set(opcion.nombre);
        this.abierto.set(false);
        this.seleccion.emit(opcion.id);
    }

    seleccionarPrimera(evento: Event): void {
        if (!this.abierto()) {
            return;
        }

        const primera = this.opcionesFiltradas()[0];

        if (!primera) {
            return;
        }

        evento.preventDefault();
        this.seleccionar(primera);
    }

    cerrar(): void {
        this.abierto.set(false);
    }

    @HostListener('document:click', ['$event'])
    cerrarAlHacerClickFuera(evento: Event): void {
        const objetivo = evento.target as Node;

        if (!this.elemento.nativeElement.contains(objetivo)) {
            this.cerrar();
        }
    }

    private normalizar(valor: string): string {
        return valor
            .normalize('NFD')
            .replace(/[\u0300-\u036f]/g, '')
            .trim()
            .toLowerCase();
    }
}