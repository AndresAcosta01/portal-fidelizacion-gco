package com.gco.fidelizacion.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gco.fidelizacion.models.Ciudad;

@Repository
public interface ICiudadRepositorio extends JpaRepository<Ciudad, UUID>{

    List<Ciudad> findByDepartamento_IdAndActivoTrue(UUID idDepartamento);

    Boolean existsByDepartamento_IdAndNombreIgnoreCase(UUID idDepartamento, String nombre);
}
