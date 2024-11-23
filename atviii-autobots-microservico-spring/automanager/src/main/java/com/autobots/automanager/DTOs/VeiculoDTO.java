package com.autobots.automanager.DTOs;

import com.autobots.automanager.enumeracoes.TipoVeiculo;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

@Data
public class VeiculoDTO extends RepresentationModel<VeiculoDTO> {
    private Long id;
    private String modelo;
    private String placa;
    private TipoVeiculo tipo; // Adicionando o campo tipo
    // Getters and Setters
}
