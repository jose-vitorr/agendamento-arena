package com.example.agendamentoarena.arena.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArenaRequest {
    
    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 3, max = 100)
    private String nome;
    
    @NotBlank(message = "Endereço é obrigatório")
    private String endereco;
    
    @NotBlank(message = "Telefone WhatsApp é obrigatório")
    @Pattern(regexp = "^\\d{11,13}$", message = "Telefone inválido")
    private String telefoneWhatsapp;
    
    private String linkMaps;
    
    @NotNull(message = "Percentual de sinal é obrigatório")
    @Pattern(regexp = "30|50", message = "Percentual deve ser 30 ou 50")
    private Integer percentualSinalPadrao;
    
    private String politicaCancelamento;
}