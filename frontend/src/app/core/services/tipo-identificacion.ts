import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';

import { environment } from '../../../environments/environment';


export interface TipoIdentificacionRespuesta {
  id: string;
  codigo: string;
  nombre: string;
  activo: boolean;
}


@Injectable({
  providedIn: 'root'
})
export class TipoIdentificacion {

  private readonly http = inject(HttpClient);

  private readonly url =
    `${environment.apiUrl}/tipos-identificacion`;


  obtenerTiposIdentificacion(): Observable<TipoIdentificacionRespuesta[]> {

    return this.http.get<TipoIdentificacionRespuesta[]>(
      this.url
    );

  }

}