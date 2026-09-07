import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';

import { environment } from '../../../environments/environment';


export interface PaisRespuesta {
  id: string;
  nombre: string;
  activo: boolean;
}


@Injectable({
  providedIn: 'root'
})
export class Pais {

  private readonly http = inject(HttpClient);

  private readonly url =
    `${environment.apiUrl}/paises`;


  obtenerPaises(): Observable<PaisRespuesta[]> {

    return this.http.get<PaisRespuesta[]>(
      this.url
    );

  }

}