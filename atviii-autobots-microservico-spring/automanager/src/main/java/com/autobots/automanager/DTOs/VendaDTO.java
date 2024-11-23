package com.autobots.automanager.DTOs;

import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

import java.util.Date;
import java.util.Set;

@Data
public class VendaDTO extends RepresentationModel<VendaDTO> {
    private Long id;
    private Date cadastro;
    private String identificacao;
    private VeiculoDTO veiculo;
    // Getters and Setters
}
