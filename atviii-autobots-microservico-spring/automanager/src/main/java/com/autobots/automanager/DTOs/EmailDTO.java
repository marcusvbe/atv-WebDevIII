// src/main/java/com/autobots/automanager/dtos/EmailDTO.java
package com.autobots.automanager.DTOs;

import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

@Data
public class EmailDTO extends RepresentationModel<EmailDTO> {
    private Long id;
    private String endereco;
}
