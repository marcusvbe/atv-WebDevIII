package com.autobots.automanager.DTOs;

import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

import java.util.Set;

@Data
public class UsuarioDTO extends RepresentationModel<UsuarioDTO> {
    private Long id;
    private String nome;
    private String nomeSocial;
    private Set<VendaDTO> vendas;
}
