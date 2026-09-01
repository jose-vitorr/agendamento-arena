package com.example.agendamentoarena.quadra.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuadraResponse {
    
    private Long id;
    private Long arenaId;
    private String nome;
    private BigDecimal valorHora;
    private Integer duracaoMinimaSlotsMinutos;
    private Boolean ativa;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime dataCriacao;
}
