package com.gco.fidelizacion.services.ciudad;

import java.util.List;
import java.util.UUID;

import com.gco.fidelizacion.dtos.ciudad.CiudadRequestDTO;
import com.gco.fidelizacion.dtos.ciudad.CiudadResponseDTO;

public interface IServicioCiudad {

    CiudadResponseDTO crear(CiudadRequestDTO dto);
    List<CiudadResponseDTO> listar();
    CiudadResponseDTO buscarPorId(UUID idCiudad);
    CiudadResponseDTO actualizar(UUID idCiudad, CiudadRequestDTO dto);
    void eliminar(UUID idCiudad);
}
