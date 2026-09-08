package com.gco.fidelizacion.services.marca;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.gco.fidelizacion.dtos.marca.MarcaRequestDTO;
import com.gco.fidelizacion.dtos.marca.MarcaResponseDTO;
import com.gco.fidelizacion.models.Marca;
import com.gco.fidelizacion.repositories.IMarcaRepositorio;
import com.gco.fidelizacion.validations.marca.IValidacionMarca;

@Service
public class ServicioMarcaImpl implements IServicioMarca {

    private final IMarcaRepositorio repositorioMarca;
    private final IValidacionMarca validacionMarca;

    public ServicioMarcaImpl(IMarcaRepositorio repositorioMarca, IValidacionMarca validacionMarca) {
        this.repositorioMarca = repositorioMarca;
        this.validacionMarca = validacionMarca;
    }

    @Override
    public MarcaResponseDTO crear(MarcaRequestDTO dto) {

        Marca marca = dto.toEntity();

        validacionMarca.validarMarca(marca);

        return MarcaResponseDTO.fromEntity(repositorioMarca.save(marca));
    }

    @Override
    public List<MarcaResponseDTO> listar() {

        return repositorioMarca.findAll()
                .stream()
                .map(MarcaResponseDTO::fromEntity)
                .toList();
    }

    @Override
    public MarcaResponseDTO buscarPorId(UUID id) {

        Marca marca = repositorioMarca.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "No se encontró la marca"));

        return MarcaResponseDTO.fromEntity(marca);

    }

    @Override
    public MarcaResponseDTO actualizar(UUID id, MarcaRequestDTO dto) {

        Marca marca = repositorioMarca.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "No se encontró la marca"));

        marca.setNombre(dto.nombre());
        marca.setDescripcion(dto.descripcion());
        marca.setActivo(dto.activo());
        marca.setLogoUrl(dto.logoUrl());
        marca.setSitioWeb(dto.sitioWeb());

        validacionMarca.validarMarcaActualizacion(id, marca);

        return MarcaResponseDTO.fromEntity(repositorioMarca.save(marca));
    }

    @Override
    public void eliminar(UUID id) {

        Marca marca = repositorioMarca.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "No se encontró la marca"));

        repositorioMarca.delete(marca);
    }
}
