import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

export interface CiudadRespuesta {
  id: string;
  nombre: string;
  activo: boolean;
  idDepartamento: string;
}

@Injectable({
  providedIn: 'root'
})
export class Ciudad {
  private readonly http = inject(HttpClient);
  private readonly url = `${environment.apiUrl}/ciudades`;

  obtenerCiudadesPorDepartamento(idDepartamento: string): Observable<CiudadRespuesta[]> {
    return this.http.get<CiudadRespuesta[]>(this.url, {
      params: { idDepartamento }
    });
  }
}