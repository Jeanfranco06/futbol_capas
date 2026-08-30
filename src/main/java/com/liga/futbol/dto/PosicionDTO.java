package com.liga.futbol.dto;

public class PosicionDTO implements Comparable<PosicionDTO> {

    private Long equipoId;
    private String equipoNombre;
    private int partidosJugados;
    private int partidosGanados;
    private int partidosEmpatados;
    private int partidosPerdidos;
    private int golesAFavor;
    private int golesEnContra;

    public PosicionDTO() {
    }

    public PosicionDTO(Long equipoId, String equipoNombre) {
        this.equipoId = equipoId;
        this.equipoNombre = equipoNombre;
        this.partidosJugados = 0;
        this.partidosGanados = 0;
        this.partidosEmpatados = 0;
        this.partidosPerdidos = 0;
        this.golesAFavor = 0;
        this.golesEnContra = 0;
    }

    public int getDiferenciaGoles() {
        return golesAFavor - golesEnContra;
    }

    public int getPuntos() {
        return (partidosGanados * 3) + (partidosEmpatados * 1);
    }

    // Criterio de ordenamiento oficial de la liga:
    // 1. Puntos DESC
    // 2. Diferencia de Goles DESC
    // 3. Goles a Favor DESC
    // 4. Nombre ASC
    @Override
    public int compareTo(PosicionDTO otro) {
        if (this.getPuntos() != otro.getPuntos()) {
            return Integer.compare(otro.getPuntos(), this.getPuntos());
        }
        if (this.getDiferenciaGoles() != otro.getDiferenciaGoles()) {
            return Integer.compare(otro.getDiferenciaGoles(), this.getDiferenciaGoles());
        }
        if (this.golesAFavor != otro.golesAFavor) {
            return Integer.compare(otro.golesAFavor, this.golesAFavor);
        }
        return this.equipoNombre.compareToIgnoreCase(otro.equipoNombre);
    }

    public Long getEquipoId() {
        return equipoId;
    }

    public void setEquipoId(Long equipoId) {
        this.equipoId = equipoId;
    }

    public String getEquipoNombre() {
        return equipoNombre;
    }

    public void setEquipoNombre(String equipoNombre) {
        this.equipoNombre = equipoNombre;
    }

    public int getPartidosJugados() {
        return partidosJugados;
    }

    public void setPartidosJugados(int partidosJugados) {
        this.partidosJugados = partidosJugados;
    }

    public int getPartidosGanados() {
        return partidosGanados;
    }

    public void setPartidosGanados(int partidosGanados) {
        this.partidosGanados = partidosGanados;
    }

    public int getPartidosEmpatados() {
        return partidosEmpatados;
    }

    public void setPartidosEmpatados(int partidosEmpatados) {
        this.partidosEmpatados = partidosEmpatados;
    }

    public int getPartidosPerdidos() {
        return partidosPerdidos;
    }

    public void setPartidosPerdidos(int partidosPerdidos) {
        this.partidosPerdidos = partidosPerdidos;
    }

    public int getGolesAFavor() {
        return golesAFavor;
    }

    public void setGolesAFavor(int golesAFavor) {
        this.golesAFavor = golesAFavor;
    }

    public int getGolesEnContra() {
        return golesEnContra;
    }

    public void setGolesEnContra(int golesEnContra) {
        this.golesEnContra = golesEnContra;
    }
}
