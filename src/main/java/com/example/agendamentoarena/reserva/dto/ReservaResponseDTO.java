package com.example.agendamentoarena.reserva.dto;

import com.example.agendamentoarena.reserva.Reserva;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public record ReservaResponseDTO(
        Long id,
        Long usuarioId,
        String nomeUsuario,
        Long horarioId,
        LocalDate dataHorario,
        LocalTime horaInicio,
        LocalTime horaFim,
        String nomeQuadra,
        String modalidade,
        BigDecimal valorTotal,
        BigDecimal valorPago,
        BigDecimal saldoRestante,
        String status,
        LocalDateTime criadoEm
) {
    public static ReservaResponseDTO from(Reserva reserva) {
        return new ReservaResponseDTO(
                reserva.getId(),
                reserva.getUsuario().getId(),
                reserva.getUsuario().getNome(),
                reserva.getHorario().getId(),
                reserva.getHorario().getData(),
                reserva.getHorario().getHoraInicio(),
                reserva.getHorario().getHoraFim(),
                reserva.getHorario().getQuadra().getNome(),
                reserva.getModalidade().getNome(),
                reserva.getValorTotal(),
                reserva.getValorPago(),
                reserva.getValorTotal().subtract(reserva.getValorPago()),
                reserva.getStatus(),
                reserva.getCriadoEm()
        );
    }
}