package com.liga.futbol.service;

import com.liga.futbol.dto.EncuentroRequestDTO;
import com.liga.futbol.dto.EncuentroResponseDTO;
import com.liga.futbol.dto.PosicionDTO;
import com.liga.futbol.entity.Encuentro;
import com.liga.futbol.entity.Equipo;
import com.liga.futbol.repository.EncuentroRepository;
import com.liga.futbol.repository.EquipoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class EncuentroService {

    private final EncuentroRepository encuentroRepository;
    private final EquipoRepository equipoRepository;

    public EncuentroService(EncuentroRepository encuentroRepository, EquipoRepository equipoRepository) {
        this.encuentroRepository = encuentroRepository;
        this.equipoRepository = equipoRepository;
    }

    @Transactional
    public EncuentroResponseDTO registrarEncuentro(EncuentroRequestDTO dto) {
        // Regla 1: Validar que no sea el mismo equipo
        if (dto.getEquipoLocalId() == null || dto.getEquipoVisitanteId() == null) {
            throw new IllegalArgumentException("Los IDs de los equipos local y visitante son obligatorios.");
        }

        if (dto.getEquipoLocalId().equals(dto.getEquipoVisitanteId())) {
            throw new IllegalArgumentException("Un equipo no puede enfrentarse consigo mismo.");
        }

        // Regla 2: Validar que los goles no sean negativos
        if (dto.getGolesLocal() == null || dto.getGolesLocal() < 0 ||
            dto.getGolesVisitante() == null || dto.getGolesVisitante() < 0) {
            throw new IllegalArgumentException("Los goles no pueden ser nulos ni negativos.");
        }

        if (dto.getFecha() == null) {
            throw new IllegalArgumentException("La fecha del encuentro es obligatoria.");
        }

        // Verificar existencia de equipos en la BD
        Equipo local = equipoRepository.findById(dto.getEquipoLocalId())
                .orElseThrow(() -> new IllegalArgumentException("No existe el equipo local con ID: " + dto.getEquipoLocalId()));

        Equipo visitante = equipoRepository.findById(dto.getEquipoVisitanteId())
                .orElseThrow(() -> new IllegalArgumentException("No existe el equipo visitante con ID: " + dto.getEquipoVisitanteId()));

        // Crear la entidad
        Encuentro encuentro = new Encuentro(local, visitante, dto.getGolesLocal(), dto.getGolesVisitante(), dto.getFecha());
        Encuentro guardado = encuentroRepository.save(encuentro);

        return convertirADTO(guardado);
    }

    @Transactional(readOnly = true)
    public List<EncuentroResponseDTO> listarEncuentros() {
        return encuentroRepository.findAll()
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PosicionDTO> obtenerTablaPosiciones() {
        // 1. Obtener todos los equipos registrados e inicializar su DTO de posición
        List<Equipo> equipos = equipoRepository.findAll();
        Map<Long, PosicionDTO> tablaMap = new HashMap<>();

        for (Equipo eq : equipos) {
            tablaMap.put(eq.getId(), new PosicionDTO(eq.getId(), eq.getNombre()));
        }

        // 2. Obtener todos los encuentros jugados y procesar resultados
        List<Encuentro> encuentros = encuentroRepository.findAll();

        for (Encuentro enc : encuentros) {
            PosicionDTO localDTO = tablaMap.get(enc.getEquipoLocal().getId());
            PosicionDTO visitaDTO = tablaMap.get(enc.getEquipoVisitante().getId());

            // Solo procesamos si ambos equipos existen en el mapa
            if (localDTO != null && visitaDTO != null) {
                // Partidos jugados
                localDTO.setPartidosJugados(localDTO.getPartidosJugados() + 1);
                visitaDTO.setPartidosJugados(visitaDTO.getPartidosJugados() + 1);

                // Goles a favor y en contra
                localDTO.setGolesAFavor(localDTO.getGolesAFavor() + enc.getGolesLocal());
                localDTO.setGolesEnContra(localDTO.getGolesEnContra() + enc.getGolesVisitante());

                visitaDTO.setGolesAFavor(visitaDTO.getGolesAFavor() + enc.getGolesVisitante());
                visitaDTO.setGolesEnContra(visitaDTO.getGolesEnContra() + enc.getGolesLocal());

                // Evaluación del resultado
                if (enc.getGolesLocal() > enc.getGolesVisitante()) {
                    // Gana Local
                    localDTO.setPartidosGanados(localDTO.getPartidosGanados() + 1);
                    visitaDTO.setPartidosPerdidos(visitaDTO.getPartidosPerdidos() + 1);
                } else if (enc.getGolesVisitante() > enc.getGolesLocal()) {
                    // Gana Visitante
                    visitaDTO.setPartidosGanados(visitaDTO.getPartidosGanados() + 1);
                    localDTO.setPartidosPerdidos(localDTO.getPartidosPerdidos() + 1);
                } else {
                    // Empate
                    localDTO.setPartidosEmpatados(localDTO.getPartidosEmpatados() + 1);
                    visitaDTO.setPartidosEmpatados(visitaDTO.getPartidosEmpatados() + 1);
                }
            }
        }

        // 3. Convertir mapa a lista y ordenar según compareTo (Puntos, Dif. Goles, Goles Favor, Nombre)
        List<PosicionDTO> tablaOrdenada = new ArrayList<>(tablaMap.values());
        Collections.sort(tablaOrdenada);

        return tablaOrdenada;
    }

    private EncuentroResponseDTO convertirADTO(Encuentro enc) {
        return new EncuentroResponseDTO(
                enc.getId(),
                enc.getEquipoLocal().getId(),
                enc.getEquipoLocal().getNombre(),
                enc.getEquipoVisitante().getId(),
                enc.getEquipoVisitante().getNombre(),
                enc.getGolesLocal(),
                enc.getGolesVisitante(),
                enc.getFecha()
        );
    }
}
