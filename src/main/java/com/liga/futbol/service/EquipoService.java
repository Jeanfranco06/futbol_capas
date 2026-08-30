package com.liga.futbol.service;

import com.liga.futbol.entity.Equipo;
import com.liga.futbol.repository.EquipoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EquipoService {

    private final EquipoRepository equipoRepository;

    // Inyección de dependencias por constructor (Buena práctica recomendada)
    public EquipoService(EquipoRepository equipoRepository) {
        this.equipoRepository = equipoRepository;
    }

    @Transactional
    public Equipo registrarEquipo(Equipo equipo) {
        if (equipo.getNombre() == null || equipo.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del equipo no puede estar vacío.");
        }

        String nombreLimpio = equipo.getNombre().trim();

        if (equipoRepository.existsByNombre(nombreLimpio)) {
            throw new IllegalArgumentException("Ya existe un equipo registrado con el nombre: " + nombreLimpio);
        }

        equipo.setNombre(nombreLimpio);
        return equipoRepository.save(equipo);
    }

    @Transactional(readOnly = true)
    public List<Equipo> listarEquipos() {
        return equipoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Equipo obtenerPorId(Long id) {
        return equipoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró el equipo con ID: " + id));
    }
}
