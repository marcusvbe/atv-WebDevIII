package com.autobots.automanager.controles;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.autobots.automanager.DTOs.EnderecoDTO;
import com.autobots.automanager.entitades.Endereco;
import com.autobots.automanager.repositorios.RepositorioEndereco;
import org.modelmapper.ModelMapper;

@RestController
@RequestMapping("/")
public class EnderecoControle {

    @Autowired
    private RepositorioEndereco repositorioEndereco;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/enderecos")
    public ResponseEntity<CollectionModel<EntityModel<EnderecoDTO>>> obterEnderecos() {
        List<Endereco> enderecos = repositorioEndereco.findAll();
        List<EntityModel<EnderecoDTO>> enderecosDTO = enderecos.stream()
                .map(endereco -> {
                    EnderecoDTO enderecoDTO = modelMapper.map(endereco, EnderecoDTO.class);
                    return EntityModel.of(enderecoDTO,
                            WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EnderecoControle.class).obterEndereco(endereco.getId())).withSelfRel(),
                            WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EnderecoControle.class).obterEnderecos()).withRel("enderecos"));
                })
                .collect(Collectors.toList());
        return new ResponseEntity<>(CollectionModel.of(enderecosDTO), HttpStatus.OK);
    }

    @GetMapping("endereco/{id}")
    public ResponseEntity<EntityModel<EnderecoDTO>> obterEndereco(@PathVariable Long id) {
        Optional<Endereco> endereco = repositorioEndereco.findById(id);
        if (endereco.isPresent()) {
            EnderecoDTO enderecoDTO = modelMapper.map(endereco.get(), EnderecoDTO.class);
            EntityModel<EnderecoDTO> enderecoModel = EntityModel.of(enderecoDTO,
                    WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EnderecoControle.class).obterEndereco(id)).withSelfRel(),
                    WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EnderecoControle.class).obterEnderecos()).withRel("enderecos"));
            return new ResponseEntity<>(enderecoModel, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/endereco/cadastro")
    public ResponseEntity<EntityModel<EnderecoDTO>> cadastrarEndereco(@RequestBody EnderecoDTO enderecoDTO) {
        Endereco endereco = modelMapper.map(enderecoDTO, Endereco.class);
        Endereco novoEndereco = repositorioEndereco.save(endereco);
        EnderecoDTO novoEnderecoDTO = modelMapper.map(novoEndereco, EnderecoDTO.class);
        EntityModel<EnderecoDTO> enderecoModel = EntityModel.of(novoEnderecoDTO,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EnderecoControle.class).obterEndereco(novoEndereco.getId())).withSelfRel(),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EnderecoControle.class).obterEnderecos()).withRel("enderecos"));
        return new ResponseEntity<>(enderecoModel, HttpStatus.CREATED);
    }

    @PutMapping("endereco/atualizar/{id}")
    public ResponseEntity<EntityModel<EnderecoDTO>> atualizarEndereco(@PathVariable Long id, @RequestBody EnderecoDTO enderecoAtualizadoDTO) {
        Optional<Endereco> enderecoExistente = repositorioEndereco.findById(id);
        if (enderecoExistente.isPresent()) {
            Endereco endereco = modelMapper.map(enderecoAtualizadoDTO, Endereco.class);
            endereco.setId(id);
            Endereco enderecoAtualizado = repositorioEndereco.save(endereco);
            EnderecoDTO enderecoAtualizadoDTOResponse = modelMapper.map(enderecoAtualizado, EnderecoDTO.class);
            EntityModel<EnderecoDTO> enderecoModel = EntityModel.of(enderecoAtualizadoDTOResponse,
                    WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EnderecoControle.class).obterEndereco(id)).withSelfRel(),
                    WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EnderecoControle.class).obterEnderecos()).withRel("enderecos"));
            return new ResponseEntity<>(enderecoModel, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/endereco/excluir/{id}")
    public ResponseEntity<Void> excluirEndereco(@PathVariable Long id) {
        Optional<Endereco> endereco = repositorioEndereco.findById(id);
        if (endereco.isPresent()) {
            repositorioEndereco.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
