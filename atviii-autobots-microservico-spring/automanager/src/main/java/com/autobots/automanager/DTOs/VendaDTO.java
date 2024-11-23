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
    private UsuarioDTO cliente;
    private UsuarioDTO funcionario;
    private Set<MercadoriaDTO> mercadorias;
    private Set<ServicoDTO> servicos;
    private VeiculoDTO veiculo;
}
