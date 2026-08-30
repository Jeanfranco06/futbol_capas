package com.liga.futbol.repository;

import com.liga.futbol.entity.Equipo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EquipoRepository extends JpaRepository<Equipo, Long> {

    // Método derivado: Spring Data JPA genera automáticamente la consulta SQL:
    // SELECT * FROM equipos WHERE nombre = ?
    Optional<Equipo> findByNombre(String nombre);

    // Verifica si ya existe un equipo con ese nombre
    boolean existsByNombre(String nombre);
}
