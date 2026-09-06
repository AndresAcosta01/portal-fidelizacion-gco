package com.gco.fidelizacion.services.tipoidentificacion;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.gco.fidelizacion.dtos.tipoidentificacion.TipoIdentificacionRequestDTO;
import com.gco.fidelizacion.dtos.tipoidentificacion.TipoIdentificacionResponseDTO;
import com.gco.fidelizacion.models.TipoIdentificacion;
import com.gco.fidelizacion.repositories.ITipoIdentificacionRepositorio;
import com.gco.fidelizacion.validations.tipoidentificacion.IValidacionTipoIdentificacion;

@Service
public class ServicioTipoIdentificacionImpl implements IServicioTipoIdentificacion {

    private final ITipoIdentificacionRepositorio repositorioTipoIdentificacion;
    private final IValidacionTipoIdentificacion validacionTipoIdentificacion;

    public ServicioTipoIdentificacionImpl(ITipoIdentificacionRepositorio repositorioTipoIdentificacion,
            IValidacionTipoIdentificacion validacionTipoIdentificacion) {
        this.repositorioTipoIdentificacion = repositorioTipoIdentificacion;
        this.validacionTipoIdentificacion = validacionTipoIdentificacion;
    }

    @Override
    public TipoIdentificacionResponseDTO crear(TipoIdentificacionRequestDTO dto) {

        TipoIdentificacion tipoIdentificacion = dto.toEntity();

        validacionTipoIdentificacion.validarTipoIdentificacion(tipoIdentificacion);

        return TipoIdentificacionResponseDTO.fromEntity(repositorioTipoIdentificacion.save(tipoIdentificacion));
    }

    @Override
    public List<TipoIdentificacionResponseDTO> listar() {

        return repositorioTipoIdentificacion.findAll()
                .stream()
                .map(TipoIdentificacionResponseDTO::fromEntity)
                .toList();
    }

    @Override
    public TipoIdentificacionResponseDTO buscarPorId(UUID id) {

        TipoIdentificacion tipoIdentificacion = repositorioTipoIdentificacion.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "No se encontró el tipo de identificación"));

        return TipoIdentificacionResponseDTO.fromEntity(tipoIdentificacion);
    }

    @Override
    public TipoIdentificacionResponseDTO actualizar(UUID id, TipoIdentificacionRequestDTO dto) {

        TipoIdentificacion tipoIdentificacion = repositorioTipoIdentificacion.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "No se encontró el tipo de identificación"));

        tipoIdentificacion.setNombre(dto.nombre());
        tipoIdentificacion.setCodigo(dto.codigo());
        tipoIdentificacion.setActivo(dto.activo());

        validacionTipoIdentificacion.validarTipoIdentificacionActualizacion(id, tipoIdentificacion);

        return TipoIdentificacionResponseDTO.fromEntity(repositorioTipoIdentificacion.save(tipoIdentificacion));
    }

    @Override
    public void eliminar(UUID id) {

        TipoIdentificacion tipoIdentificacion = repositorioTipoIdentificacion.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "No se encontró el tipo de identificación"));
        repositorioTipoIdentificacion.delete(tipoIdentificacion);
    }
}
