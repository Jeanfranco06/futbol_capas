package com.liga.futbol.repository;

import com.liga.futbol.entity.Encuentro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EncuentroRepository extends JpaRepository<Encuentro, Long> {

    // Buscar todos los encuentros donde un equipo participó como local o visitante
    List<Encuentro> findByEquipoLocalIdOrEquipoVisitanteId(Long equipoLocalId, Long equipoVisitanteId);
}
