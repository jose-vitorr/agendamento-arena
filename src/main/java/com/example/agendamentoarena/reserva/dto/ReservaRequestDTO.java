package com.example.agendamentoarena.reserva.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record ReservaRequestDTO(

        @NotNull(message = "Horário é obrigatório")
        Long horarioId,

        @NotNull(message = "Modalidade é obrigatória")
        Long modalidadeId,

        @NotNull(message = "Forma de pagamento é obrigatória")
        @Pattern(regexp = "pix|cartao", message = "Forma de pagamento deve ser 'pix' ou 'cartao'")
        String formaPagamento,

        // true = paga integral, false = paga só o sinal (RN01/RN02)
        @NotNull(message = "Informe se deseja pagamento integral")
        Boolean pagamentoIntegral
) {}