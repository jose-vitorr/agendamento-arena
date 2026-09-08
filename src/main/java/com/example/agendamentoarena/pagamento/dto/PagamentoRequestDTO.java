package com.example.agendamentoarena.pagamento.dto;

import jakarta.validation.constraints.NotNull;

public record PagamentoRequestDTO(

        @NotNull(message = "Reserva é obrigatória")
        Long reservaId,

        @NotNull(message = "Forma de pagamento é obrigatória")
        String formaPagamento
) {}