import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';

import { environment } from '../../../environments/environment';


export interface MarcaRespuesta {
  id: string;
  nombre: string;
  descripcion: string | null;
  logoUrl: string | null;
  sitioWeb: string | null;
  activo: boolean;
}


@Injectable({
  providedIn: 'root'
})
export class Marca {

  private readonly http = inject(HttpClient);

  private readonly url =
    `${environment.apiUrl}/marcas`;


  obtenerMarcas(): Observable<MarcaRespuesta[]> {

    return this.http.get<MarcaRespuesta[]>(
      this.url
    );

  }

}