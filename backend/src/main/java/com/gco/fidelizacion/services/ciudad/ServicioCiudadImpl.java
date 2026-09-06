package com.gco.fidelizacion.services.ciudad;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.gco.fidelizacion.dtos.ciudad.CiudadRequestDTO;
import com.gco.fidelizacion.dtos.ciudad.CiudadResponseDTO;
import com.gco.fidelizacion.models.Ciudad;
import com.gco.fidelizacion.models.Departamento;
import com.gco.fidelizacion.repositories.ICiudadRepositorio;
import com.gco.fidelizacion.repositories.IDepartamentoRepositorio;
import com.gco.fidelizacion.validations.ciudad.IValidacionCiudad;

@Service
public class ServicioCiudadImpl implements IServicioCiudad {

    private final ICiudadRepositorio repositorioCiudad;
    private final IDepartamentoRepositorio repositorioDepartamento;
    private final IValidacionCiudad validacionCiudad;

    public ServicioCiudadImpl(ICiudadRepositorio repositorioCiudad, IDepartamentoRepositorio repositorioDepartamento,
            IValidacionCiudad validacionCiudad) {
        this.repositorioCiudad = repositorioCiudad;
        this.repositorioDepartamento = repositorioDepartamento;
        this.validacionCiudad = validacionCiudad;
    }

    @Override
    public CiudadResponseDTO crear(CiudadRequestDTO dto) {

        Ciudad ciudad = dto.toEntity();

        Departamento departamento = repositorioDepartamento.findById(dto.idDepartamento())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "No se encontró el departamento"));

        ciudad.setDepartamento(departamento);

        validacionCiudad.validarCiudad(ciudad);

        return CiudadResponseDTO.fromEntity(repositorioCiudad.save(ciudad));
    }

    @Override
    public List<CiudadResponseDTO> listar() {

        return repositorioCiudad.findAll()
                .stream()
                .map(CiudadResponseDTO::fromEntity)
                .toList();
    }

    @Override
    public CiudadResponseDTO buscarPorId(UUID idCiudad) {

        Ciudad ciudad = repositorioCiudad.findById(idCiudad)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "No se encontró la ciudad"));

        return CiudadResponseDTO.fromEntity(ciudad);
    }

    @Override
    public CiudadResponseDTO actualizar(UUID idCiudad, CiudadRequestDTO dto) {

        Ciudad ciudad = repositorioCiudad.findById(idCiudad)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "No se encontró la ciudad"));

        Departamento departamento = repositorioDepartamento.findById(dto.idDepartamento())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "No se encontró el departamento"));

        ciudad.setNombre(dto.nombre());
        ciudad.setActivo(dto.activo());
        ciudad.setDepartamento(departamento);

        validacionCiudad.validarCiudadActualizacion(idCiudad, ciudad);

        return CiudadResponseDTO.fromEntity(repositorioCiudad.save(ciudad));
    }

    @Override
    public void eliminar(UUID idCiudad) {
        Ciudad ciudad = repositorioCiudad.findById(idCiudad)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "No se encontró la ciudad"));

        repositorioCiudad.delete(ciudad);
    }

}
