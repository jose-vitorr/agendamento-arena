package com.example.agendamentoarena.arena.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArenaResponse {
    
    private Long id;
    private String nome;
    private String endereco;
    private String telefoneWhatsapp;
    private String linkMaps;
    private Integer percentualSinalPadrao;
    private String politicaCancelamento;
    private Boolean ativa;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime dataCriacao;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime dataAtualizacao;
}
