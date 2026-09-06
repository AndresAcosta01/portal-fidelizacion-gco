package com.gco.fidelizacion.services.marca;

import java.util.List;
import java.util.UUID;

import com.gco.fidelizacion.dtos.marca.MarcaRequestDTO;
import com.gco.fidelizacion.dtos.marca.MarcaResponseDTO;

public interface IServicioMarca {

    MarcaResponseDTO crear(MarcaRequestDTO dto);
    List<MarcaResponseDTO> listar();
    MarcaResponseDTO buscarPorId(UUID id);
    MarcaResponseDTO actualizar(UUID id, MarcaRequestDTO dto);
    void eliminar(UUID id);

}
