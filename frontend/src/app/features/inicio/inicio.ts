import { Component, signal } from '@angular/core';

import { MarcaRespuesta } from '../../core/services/marca';
import { HeroInicio } from './components/hero-inicio/hero-inicio';
import { SeccionMarcas } from './components/seccion-marcas/seccion-marcas';
import { CtaRegistro } from './components/cta-registro/cta-registro';

@Component({
  selector: 'app-inicio',
  imports: [HeroInicio, SeccionMarcas, CtaRegistro],
  templateUrl: './inicio.html'
})
export class Inicio {
  marcas = signal<MarcaRespuesta[]>([
    {
      id: 'americanino',
      nombre: 'Americanino',
      descripcion: 'Marca del programa de fidelización GCO',
      logoUrl: 'https://res.cloudinary.com/lrur0zig/image/upload/v1788741251/Icono-americanino.png',
      sitioWeb: null,
      activo: true
    },
    {
      id: 'american-eagle',
      nombre: 'American Eagle',
      descripcion: 'Marca del programa de fidelización GCO',
      logoUrl: 'https://res.cloudinary.com/lrur0zig/image/upload/v1788740287/logo-american-eagle.svg',
      sitioWeb: null,
      activo: true
    },
    {
      id: 'chevignon',
      nombre: 'Chevignon',
      descripcion: 'Marca del programa de fidelización GCO',
      logoUrl: 'https://res.cloudinary.com/lrur0zig/image/upload/v1788740368/Icono-chevignon.png',
      sitioWeb: null,
      activo: true
    },
    {
      id: 'esprit',
      nombre: 'Esprit',
      descripcion: 'Marca del programa de fidelización GCO',
      logoUrl: 'https://res.cloudinary.com/lrur0zig/image/upload/v1788740571/Icono-esprit.png',
      sitioWeb: null,
      activo: true
    },
    {
      id: 'naf-naf',
      nombre: 'Naf Naf',
      descripcion: 'Marca del programa de fidelización GCO',
      logoUrl: 'https://res.cloudinary.com/lrur0zig/image/upload/v1788740061/Icono-Naf-Naf.png',
      sitioWeb: null,
      activo: true
    },
    {
      id: 'rifle',
      nombre: 'Rifle',
      descripcion: 'Marca del programa de fidelización GCO',
      logoUrl: 'https://res.cloudinary.com/lrur0zig/image/upload/v1788741059/logo-rifle.png',
      sitioWeb: null,
      activo: true
    }
  ]);
}