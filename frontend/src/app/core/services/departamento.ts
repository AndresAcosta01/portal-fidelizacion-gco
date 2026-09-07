import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';

import { environment } from '../../../environments/environment';


export interface DepartamentoRespuesta {
  id: string;
  nombre: string;
  activo: boolean;
  idPais: string;
}


@Injectable({
  providedIn: 'root'
})
export class Departamento {

  private readonly http = inject(HttpClient);

  private readonly url =
    `${environment.apiUrl}/departamentos`;


  obtenerDepartamentos(): Observable<DepartamentoRespuesta[]> {

    return this.http.get<DepartamentoRespuesta[]>(
      this.url
    );

  }

}