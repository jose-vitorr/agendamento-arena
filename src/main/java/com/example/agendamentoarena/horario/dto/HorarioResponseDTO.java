package com.example.agendamentoarena.horario.dto;

import com.example.agendamentoarena.horario.Horario;

import java.time.LocalDate;
import java.time.LocalTime;

public record HorarioResponseDTO(
        Long id,
        Long quadraId,
        String nomeQuadra,
        LocalDate data,
        LocalTime horaInicio,
        LocalTime horaFim,
        String status
) {
    public static HorarioResponseDTO from(Horario horario) {
        return new HorarioResponseDTO(
                horario.getId(),
                horario.getQuadra().getId(),
                horario.getQuadra().getNome(),
                horario.getData(),
                horario.getHoraInicio(),
                horario.getHoraFim(),
                horario.getStatus()
        );
    }
}