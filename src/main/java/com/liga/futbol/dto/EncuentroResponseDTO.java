package com.liga.futbol.dto;

import java.time.LocalDate;

public class EncuentroResponseDTO {

    private Long id;
    private Long equipoLocalId;
    private String equipoLocalNombre;
    private Long equipoVisitanteId;
    private String equipoVisitanteNombre;
    private Integer golesLocal;
    private Integer golesVisitante;
    private LocalDate fecha;

    public EncuentroResponseDTO() {
    }

    public EncuentroResponseDTO(Long id, Long equipoLocalId, String equipoLocalNombre, Long equipoVisitanteId, String equipoVisitanteNombre, Integer golesLocal, Integer golesVisitante, LocalDate fecha) {
        this.id = id;
        this.equipoLocalId = equipoLocalId;
        this.equipoLocalNombre = equipoLocalNombre;
        this.equipoVisitanteId = equipoVisitanteId;
        this.equipoVisitanteNombre = equipoVisitanteNombre;
        this.golesLocal = golesLocal;
        this.golesVisitante = golesVisitante;
        this.fecha = fecha;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEquipoLocalId() {
        return equipoLocalId;
    }

    public void setEquipoLocalId(Long equipoLocalId) {
        this.equipoLocalId = equipoLocalId;
    }

    public String getEquipoLocalNombre() {
        return equipoLocalNombre;
    }

    public void setEquipoLocalNombre(String equipoLocalNombre) {
        this.equipoLocalNombre = equipoLocalNombre;
    }

    public Long getEquipoVisitanteId() {
        return equipoVisitanteId;
    }

    public void setEquipoVisitanteId(Long equipoVisitanteId) {
        this.equipoVisitanteId = equipoVisitanteId;
    }

    public String getEquipoVisitanteNombre() {
        return equipoVisitanteNombre;
    }

    public void setEquipoVisitanteNombre(String equipoVisitanteNombre) {
        this.equipoVisitanteNombre = equipoVisitanteNombre;
    }

    public Integer getGolesLocal() {
        return golesLocal;
    }

    public void setGolesLocal(Integer golesLocal) {
        this.golesLocal = golesLocal;
    }

    public Integer getGolesVisitante() {
        return golesVisitante;
    }

    public void setGolesVisitante(Integer golesVisitante) {
        this.golesVisitante = golesVisitante;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }
}
