package com.gco.fidelizacion.services.pais;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.gco.fidelizacion.dtos.pais.PaisRequestDTO;
import com.gco.fidelizacion.dtos.pais.PaisResponseDTO;
import com.gco.fidelizacion.models.Pais;
import com.gco.fidelizacion.repositories.IPaisRepositorio;
import com.gco.fidelizacion.validations.pais.IValidacionPais;

@Service
public class ServicioPaisImpl implements IServicioPais {

    private final IPaisRepositorio repositorioPais;
    private final IValidacionPais validacionPais;

    public ServicioPaisImpl(IPaisRepositorio repositorioPais, IValidacionPais validacionPais) {
        this.repositorioPais = repositorioPais;
        this.validacionPais = validacionPais;
    }

    @Override
    public PaisResponseDTO crear(PaisRequestDTO dto) {

        Pais pais = dto.toEntity();

        validacionPais.validarPais(pais);

        return PaisResponseDTO.fromEntity(repositorioPais.save(pais));
    }

    @Override
    public List<PaisResponseDTO> listar() {

        return repositorioPais.findAll()
                .stream()
                .map(PaisResponseDTO::fromEntity)
                .toList();
    }

    @Override
    public PaisResponseDTO buscarPorId(UUID id) {

        Pais pais = repositorioPais.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "No se encontró el pais"));
        return PaisResponseDTO.fromEntity(pais);
    }

    @Override
    public PaisResponseDTO actualizar(UUID id, PaisRequestDTO dto) {

        Pais pais = repositorioPais.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "No se encontró el pais"));

        pais.setNombre(dto.nombre());
        pais.setActivo(dto.activo());

        validacionPais.validarPaisActualizacion(id, pais);

        return PaisResponseDTO.fromEntity(repositorioPais.save(pais));
    }

    @Override
    public void eliminar(UUID id) {

        Pais pais = repositorioPais.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "No se encontró el pais"));
        repositorioPais.delete(pais);
    }
}
