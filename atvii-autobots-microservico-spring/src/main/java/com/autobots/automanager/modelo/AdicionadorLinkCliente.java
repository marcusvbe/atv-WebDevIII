package com.autobots.automanager.modelo;
import java.util.List;

import com.autobots.automanager.controles.DocumentoControle;
import com.autobots.automanager.controles.EnderecoControle;
import com.autobots.automanager.controles.TelefoneControle;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import com.autobots.automanager.controles.ClienteControle;
import com.autobots.automanager.entidades.Cliente;

@Component
public class AdicionadorLinkCliente implements AdicionadorLink<Cliente> {

    @Override
    public void adicionarLink(List<Cliente> lista) {
        for (Cliente cliente : lista) {
            long id = cliente.getId();
            Link linkProprio = WebMvcLinkBuilder
                    .linkTo(WebMvcLinkBuilder
                            .methodOn(ClienteControle.class)
                            .obterCliente(id))
                    .withSelfRel();
            cliente.add(linkProprio);

            // Adicionar links para documentos
            cliente.getDocumentos().forEach(documento -> {
                Link documentoLink = WebMvcLinkBuilder
                        .linkTo(WebMvcLinkBuilder
                                .methodOn(DocumentoControle.class)
                                .obterDocumento(documento.getId()))
                        .withRel("documento");
                documento.add(documentoLink);
            });

            // Adicionar link para endereço
            if (cliente.getEndereco() != null) {
                Link enderecoLink = WebMvcLinkBuilder
                        .linkTo(WebMvcLinkBuilder
                                .methodOn(EnderecoControle.class)
                                .obterEndereco(cliente.getEndereco().getId()))
                        .withRel("endereco");
                cliente.getEndereco().add(enderecoLink);
            }

            // Adicionar links para telefones
            cliente.getTelefones().forEach(telefone -> {
                Link telefoneLink = WebMvcLinkBuilder
                        .linkTo(WebMvcLinkBuilder
                                .methodOn(TelefoneControle.class)
                                .obterTelefone(telefone.getId()))
                        .withRel("telefone");
                telefone.add(telefoneLink);
            });
        }
    }

    @Override
    public void adicionarLink(Cliente objeto) {
        Link linkProprio = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder
                        .methodOn(ClienteControle.class)
                        .obterClientes())
                .withRel("clientes");
        objeto.add(linkProprio);

        // Adicionar links para documentos
        objeto.getDocumentos().forEach(documento -> {
            Link documentoLink = WebMvcLinkBuilder
                    .linkTo(WebMvcLinkBuilder
                            .methodOn(DocumentoControle.class)
                            .obterDocumento(documento.getId()))
                    .withRel("documento");
            documento.add(documentoLink);
        });

        // Adicionar link para endereço
        if (objeto.getEndereco() != null) {
            Link enderecoLink = WebMvcLinkBuilder
                    .linkTo(WebMvcLinkBuilder
                            .methodOn(EnderecoControle.class)
                            .obterEndereco(objeto.getEndereco().getId()))
                    .withRel("endereco");
            objeto.getEndereco().add(enderecoLink);
        }

        // Adicionar links para telefones
        objeto.getTelefones().forEach(telefone -> {
            Link telefoneLink = WebMvcLinkBuilder
                    .linkTo(WebMvcLinkBuilder
                            .methodOn(TelefoneControle.class)
                            .obterTelefone(telefone.getId()))
                    .withRel("telefone");
            telefone.add(telefoneLink);
        });
    }
}










