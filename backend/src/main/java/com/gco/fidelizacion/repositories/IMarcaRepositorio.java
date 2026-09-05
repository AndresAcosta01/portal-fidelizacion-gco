package com.gco.fidelizacion.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gco.fidelizacion.models.Marca;

@Repository
public interface IMarcaRepositorio extends JpaRepository<Marca, UUID>{

    List<Marca> findByActivoTrue();

    Boolean existsByNombreIgnoreCase(String nombre);
}
