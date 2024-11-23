// src/main/java/com/autobots/automanager/dtos/CredencialDTO.java
package com.autobots.automanager.DTOs;

import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

import java.util.Date;

@Data
public class CredencialDTO extends RepresentationModel<CredencialDTO> {
    private Long id;
    private Date criacao;
    private Date ultimoAcesso;
    private boolean inativo;
}
