// src/main/java/com/autobots/automanager/DTOs/EmpresaDTO.java
package com.autobots.automanager.DTOs;

import com.autobots.automanager.DTOs.*;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

import java.util.Date;
import java.util.Set;

@Data
public class EmpresaDTO extends RepresentationModel<EmpresaDTO> {
    private Long id;
    private String razaoSocial;
    private String nomeFantasia;
    private Date cadastro;
    private Set<UsuarioDTO> usuarios;
    private Set<MercadoriaDTO> mercadorias;
    private Set<ServicoDTO> servicos;
    private Set<VendaDTO> vendas;
    private Set<TelefoneDTO> telefones;
    private EnderecoDTO endereco;
}
