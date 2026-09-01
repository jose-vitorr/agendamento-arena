package com.example.agendamentoarena.administrador.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdministradorArenaRequest {
    
    @NotNull
    private Long administradorId;
    
    @NotNull
    private Long arenaId;
    
    private Boolean principal = false;
}
