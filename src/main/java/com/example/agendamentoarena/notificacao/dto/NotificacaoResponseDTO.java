package com.example.agendamentoarena.notificacao.dto;

import com.example.agendamentoarena.notificacao.Notificacao;

import java.time.LocalDateTime;

public record NotificacaoResponseDTO(
        Long id,
        Long reservaId,
        String canal,
        String statusEnvio,
        LocalDateTime dataEnvio
) {
    public static NotificacaoResponseDTO from(Notificacao notificacao) {
        return new NotificacaoResponseDTO(
                notificacao.getId(),
                notificacao.getReserva().getId(),
                notificacao.getCanal(),
                notificacao.getStatusEnvio(),
                notificacao.getDataEnvio()
        );
    }
}