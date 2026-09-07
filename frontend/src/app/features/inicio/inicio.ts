import { Component, inject, OnInit, signal } from '@angular/core';

import { Marca, MarcaRespuesta } from '../../core/services/marca';
import { HeroInicio } from './components/hero-inicio/hero-inicio';
import { SeccionMarcas } from './components/seccion-marcas/seccion-marcas';
import { CtaRegistro } from './components/cta-registro/cta-registro';

@Component({
  selector: 'app-inicio',
  imports: [HeroInicio, SeccionMarcas, CtaRegistro],
  templateUrl: './inicio.html',
  styleUrl: './inicio.css'
})
export class Inicio implements OnInit {

  marcas = signal<MarcaRespuesta[]>([]);

  private readonly marcaServicio = inject(Marca);

  ngOnInit(): void {
    this.cargarMarcas();
  }

  cargarMarcas(): void {
    this.marcaServicio.obtenerMarcas().subscribe({
      next: (marcas) => {
        this.marcas.set(marcas.filter(marca => marca.activo));
      },
      error: (error) => {
        console.error('Error al cargar las marcas:', error);
      }
    });
  }

}