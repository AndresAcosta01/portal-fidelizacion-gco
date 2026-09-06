package com.gco.fidelizacion.services.pais;

import java.util.List;
import java.util.UUID;

import com.gco.fidelizacion.dtos.pais.PaisRequestDTO;
import com.gco.fidelizacion.dtos.pais.PaisResponseDTO;

public interface IServicioPais {

    PaisResponseDTO crear(PaisRequestDTO dto);
    List<PaisResponseDTO> listar();
    PaisResponseDTO buscarPorId(UUID id);
    PaisResponseDTO actualizar(UUID id, PaisRequestDTO dto);
    void eliminar(UUID id);

}
