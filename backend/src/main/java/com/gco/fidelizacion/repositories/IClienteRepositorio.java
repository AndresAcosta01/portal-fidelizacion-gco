package com.gco.fidelizacion.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gco.fidelizacion.models.Cliente;


@Repository
public interface IClienteRepositorio extends JpaRepository<Cliente, UUID>{

    Boolean existsByTipoIdentificacion_IdAndNumeroIdentificacion(UUID idTipoIdentificacion, String numeroIdentificacion);
}
