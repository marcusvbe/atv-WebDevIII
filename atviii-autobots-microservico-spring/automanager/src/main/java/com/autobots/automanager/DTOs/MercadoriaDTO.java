// src/main/java/com/autobots/automanager/dtos/MercadoriaDTO.java
package com.autobots.automanager.DTOs;

import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

import java.util.Date;

@Data
public class MercadoriaDTO extends RepresentationModel<MercadoriaDTO> {
    private Long id;
    private Date validade;
    private Date fabricao;
    private Date cadastro;
    private String nome;
    private long quantidade;
    private double valor;
    private String descricao;
    // Getters and Setters
}
