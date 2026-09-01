package com.example.agendamentoarena.usuario.dto;

import com.example.agendamentoarena.usuario.Usuario;

public record UsuarioResponseDTO(
        Long id,
        String nome,
        String telefoneWhatsapp,
        String email
) {
    public static UsuarioResponseDTO from(Usuario usuario) {
        return new UsuarioResponseDTO(
                usuario.getId(),
                usuario.getNome(),
                usuario.getTelefoneWhatsapp(),
                usuario.getEmail()
        );
    }
}