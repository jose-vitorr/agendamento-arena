package com.example.agendamentoarena.administrador.dto;

import com.example.agendamentoarena.arena.dto.ArenaResponse;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdministradorArenaResponse {
    
    private Long id;
    private AdministradorResponse administrador;
    private ArenaResponse arena;
    private Boolean principal;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime dataCriacao;
}
