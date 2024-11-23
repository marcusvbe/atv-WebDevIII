// src/main/java/com/autobots/automanager/dtos/EnderecoDTO.java
package com.autobots.automanager.DTOs;

import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

@Data
public class EnderecoDTO extends RepresentationModel<EnderecoDTO> {
    private Long id;
    private String estado;
    private String cidade;
    private String bairro;
    private String rua;
    private String numero;
    private String codigoPostal;
    private String informacoesAdicionais;
    // Getters and Setters
}
