import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';

import { environment } from '../../../environments/environment';


export interface ClienteRegistro {
  numeroIdentificacion: string;
  nombres: string;
  apellidos: string;
  fechaNacimiento: string;
  direccion: string;
  idTipoIdentificacion: string;
  idCiudad: string;
  idMarca: string;
}


export interface ClienteRespuesta {
  id: string;
  numeroIdentificacion: string;
  nombres: string;
  apellidos: string;
  fechaNacimiento: string;
  direccion: string;
  idTipoIdentificacion: string;
  idCiudad: string;
  idMarca: string;
}


@Injectable({
  providedIn: 'root'
})
export class Cliente {

  private readonly http = inject(HttpClient);

  private readonly url =
    `${environment.apiUrl}/clientes`;


  registrarCliente(
    cliente: ClienteRegistro
  ): Observable<ClienteRespuesta> {

    return this.http.post<ClienteRespuesta>(
      this.url,
      cliente
    );

  }

}