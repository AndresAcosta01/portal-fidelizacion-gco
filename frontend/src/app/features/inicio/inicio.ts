import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-inicio',
  imports: [RouterLink],
  templateUrl: './inicio.html',
  styleUrl: './inicio.css'
})
export class Inicio {

  marcas = [
    {
      nombre: 'Americanino',
      logo: 'https://res.cloudinary.com/lrur0zig/image/upload/v1788741251/Icono-americanino.png'
    },
    {
      nombre: 'American Eagle',
      logo: 'https://res.cloudinary.com/lrur0zig/image/upload/v1788740287/logo-american-eagle.svg'
    },
    {
      nombre: 'Chevignon',
      logo: 'https://res.cloudinary.com/lrur0zig/image/upload/v1788740368/Icono-chevignon.png'
    },
    {
      nombre: 'Esprit',
      logo: 'https://res.cloudinary.com/lrur0zig/image/upload/v1788740571/Icono-esprit.png'
    },
    {
      nombre: 'Naf Naf',
      logo: 'https://res.cloudinary.com/lrur0zig/image/upload/v1788740061/Icono-Naf-Naf.png'
    },
    {
      nombre: 'Rifle',
      logo: 'https://res.cloudinary.com/lrur0zig/image/upload/v1788741059/logo-rifle.png'
    }
  ];

}