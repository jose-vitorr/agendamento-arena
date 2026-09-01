package com.example.agendamentoarena.horario.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

public record HorarioRequestDTO(

        @NotNull(message = "Quadra é obrigatória")
        Long quadraId,

        @NotNull(message = "Data é obrigatória")
        @Future(message = "Data deve ser futura")
        LocalDate data,

        @NotNull(message = "Hora de início é obrigatória")
        LocalTime horaInicio,

        @NotNull(message = "Hora de fim é obrigatória")
        LocalTime horaFim
) {}