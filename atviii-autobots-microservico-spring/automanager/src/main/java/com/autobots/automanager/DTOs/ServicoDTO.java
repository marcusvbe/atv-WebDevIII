// src/main/java/com/autobots/automanager/dtos/ServicoDTO.java
package com.autobots.automanager.DTOs;

import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

@Data
public class ServicoDTO extends RepresentationModel<ServicoDTO> {
    private Long id;
    private String nome;
    private double valor;
    private String descricao;
    // Getters and Setters
}
