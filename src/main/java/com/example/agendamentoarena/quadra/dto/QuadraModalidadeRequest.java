package com.example.agendamentoarena.quadra.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuadraModalidadeRequest {
    
    @NotNull
    private Long quadraId;
    
    @NotNull
    private Long modalidadeId;
}
