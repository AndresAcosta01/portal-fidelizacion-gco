package com.gco.fidelizacion.services.departamento;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.gco.fidelizacion.dtos.departamento.DepartamentoRequestDTO;
import com.gco.fidelizacion.dtos.departamento.DepartamentoResponseDTO;
import com.gco.fidelizacion.models.Departamento;
import com.gco.fidelizacion.models.Pais;
import com.gco.fidelizacion.repositories.IDepartamentoRepositorio;
import com.gco.fidelizacion.repositories.IPaisRepositorio;
import com.gco.fidelizacion.validations.departamento.IValidacionDepartamento;

@Service
public class ServicioDepartamentoImpl implements IServicioDepartamento {

    private final IDepartamentoRepositorio repositorioDepartamento;
    private final IPaisRepositorio repositorioPais;
    private final IValidacionDepartamento validacionDepartamento;

    public ServicioDepartamentoImpl(IDepartamentoRepositorio repositorioDepartamento, IPaisRepositorio repositorioPais,
            IValidacionDepartamento validacionDepartamento) {
        this.repositorioDepartamento = repositorioDepartamento;
        this.repositorioPais = repositorioPais;
        this.validacionDepartamento = validacionDepartamento;
    }

    @Override
    public DepartamentoResponseDTO crear(DepartamentoRequestDTO dto) {

        Departamento departamento = dto.toEntity();

        Pais pais = repositorioPais.findById(dto.idPais())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "No se encontró el pais"));

        departamento.setPais(pais);

        validacionDepartamento.validarDepartamento(departamento);

        return DepartamentoResponseDTO.fromEntity(repositorioDepartamento.save(departamento));
    }

    @Override
    public List<DepartamentoResponseDTO> listar() {

        return repositorioDepartamento.findAll()
                .stream()
                .map(DepartamentoResponseDTO::fromEntity)
                .toList();
    }

    @Override
    public DepartamentoResponseDTO buscarPorId(UUID id) {

        Departamento departamento = repositorioDepartamento.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "No se encontró el departamento"));

        return DepartamentoResponseDTO.fromEntity(departamento);
    }

    @Override
    public DepartamentoResponseDTO actualizar(UUID id, DepartamentoRequestDTO dto) {

        Departamento departamento = repositorioDepartamento.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "No se encontró el departamento"));

        Pais pais = repositorioPais.findById(dto.idPais())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "No se encontró el pais"));

        departamento.setNombre(dto.nombre());
        departamento.setActivo(dto.activo());
        departamento.setPais(pais);

        validacionDepartamento.validarDepartamentoActualizacion(id, departamento);

        return DepartamentoResponseDTO.fromEntity(repositorioDepartamento.save(departamento));
    }

    @Override
    public void eliminar(UUID id) {

        Departamento departamento = repositorioDepartamento.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "No se encontró el departamento"));

        repositorioDepartamento.delete(departamento);
    }

}
