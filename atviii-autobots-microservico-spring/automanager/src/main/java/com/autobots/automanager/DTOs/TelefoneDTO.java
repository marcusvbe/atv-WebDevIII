// src/main/java/com/autobots/automanager/dtos/TelefoneDTO.java
package com.autobots.automanager.DTOs;

import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

@Data
public class TelefoneDTO extends RepresentationModel<TelefoneDTO> {
    private Long id;
    private String ddd;
    private String numero;
    // Getters and Setters
}
