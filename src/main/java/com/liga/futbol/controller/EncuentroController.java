package com.liga.futbol.controller;

import com.liga.futbol.dto.EncuentroRequestDTO;
import com.liga.futbol.dto.EncuentroResponseDTO;
import com.liga.futbol.dto.PosicionDTO;
import com.liga.futbol.service.EncuentroService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/encuentros")
@CrossOrigin(origins = "*")
public class EncuentroController {

    private final EncuentroService encuentroService;

    public EncuentroController(EncuentroService encuentroService) {
        this.encuentroService = encuentroService;
    }

    @PostMapping
    public ResponseEntity<?> registrarEncuentro(@RequestBody EncuentroRequestDTO dto) {
        try {
            EncuentroResponseDTO creado = encuentroService.registrarEncuentro(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(creado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<EncuentroResponseDTO>> listarEncuuentros() {
        return ResponseEntity.ok(encuentroService.listarEncuentros());
    }

    @GetMapping("/tabla-posiciones")
    public ResponseEntity<List<PosicionDTO>> obtenerTablaPosiciones() {
        return ResponseEntity.ok(encuentroService.obtenerTablaPosiciones());
    }
}
