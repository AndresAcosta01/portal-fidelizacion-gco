package com.gco.fidelizacion.services.departamento;

import java.util.List;
import java.util.UUID;

import com.gco.fidelizacion.dtos.departamento.DepartamentoRequestDTO;
import com.gco.fidelizacion.dtos.departamento.DepartamentoResponseDTO;

public interface IServicioDepartamento {

    DepartamentoResponseDTO crear(DepartamentoRequestDTO dto);
    List<DepartamentoResponseDTO> listar();
    DepartamentoResponseDTO buscarPorId(UUID id);
    DepartamentoResponseDTO actualizar(UUID id, DepartamentoRequestDTO dto);
    void eliminar(UUID id);
}
