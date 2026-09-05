package com.gco.fidelizacion.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gco.fidelizacion.models.Pais;

@Repository
public interface IPaisRepositorio extends JpaRepository<Pais, UUID>{

    List<Pais> findByActivoTrue();

    Boolean existsByNombreIgnoreCase(String nombre);
}
