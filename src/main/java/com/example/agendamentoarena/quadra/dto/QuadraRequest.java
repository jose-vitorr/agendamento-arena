package com.example.agendamentoarena.quadra.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuadraRequest {
    
    @NotNull(message = "Arena ID é obrigatório")
    private Long arenaId;
    
    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 2, max = 50)
    private String nome;
    
    @NotNull(message = "Valor da hora é obrigatório")
    @DecimalMin(value = "0.01", message = "Valor deve ser maior que zero")
    private BigDecimal valorHora;
    
    private Integer duracaoMinimaSlotsMinutos; // 60 ou 90
}