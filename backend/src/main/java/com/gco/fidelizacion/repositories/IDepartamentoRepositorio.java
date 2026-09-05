package com.gco.fidelizacion.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gco.fidelizacion.models.Departamento;

@Repository
public interface IDepartamentoRepositorio extends JpaRepository<Departamento, UUID>{

    List<Departamento> findByPais_IdAndActivoTrue(UUID id);

    Boolean existsByPais_IdAndNombreIgnoreCase(UUID id, String nombre);
}
