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

import com.autobots.automanager.DTOs.TelefoneDTO;
import com.autobots.automanager.entitades.Telefone;
import com.autobots.automanager.repositorios.RepositorioTelefone;
import org.modelmapper.ModelMapper;

@RestController
@RequestMapping("/")
public class TelefoneControle {

    @Autowired
    private RepositorioTelefone repositorioTelefone;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/telefones")
    public ResponseEntity<CollectionModel<EntityModel<TelefoneDTO>>> obterTelefones() {
        List<Telefone> telefones = repositorioTelefone.findAll();
        List<EntityModel<TelefoneDTO>> telefonesDTO = telefones.stream()
                .map(telefone -> {
                    TelefoneDTO telefoneDTO = modelMapper.map(telefone, TelefoneDTO.class);
                    return EntityModel.of(telefoneDTO,
                            WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(TelefoneControle.class).obterTelefone(telefone.getId())).withSelfRel(),
                            WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(TelefoneControle.class).obterTelefones()).withRel("telefones"));
                })
                .collect(Collectors.toList());
        return new ResponseEntity<>(CollectionModel.of(telefonesDTO), HttpStatus.OK);
    }

    @GetMapping("telefone/{id}")
    public ResponseEntity<EntityModel<TelefoneDTO>> obterTelefone(@PathVariable Long id) {
        Optional<Telefone> telefone = repositorioTelefone.findById(id);
        if (telefone.isPresent()) {
            TelefoneDTO telefoneDTO = modelMapper.map(telefone.get(), TelefoneDTO.class);
            EntityModel<TelefoneDTO> telefoneModel = EntityModel.of(telefoneDTO,
                    WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(TelefoneControle.class).obterTelefone(id)).withSelfRel(),
                    WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(TelefoneControle.class).obterTelefones()).withRel("telefones"));
            return new ResponseEntity<>(telefoneModel, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/telefone/cadastro")
    public ResponseEntity<EntityModel<TelefoneDTO>> cadastrarTelefone(@RequestBody TelefoneDTO telefoneDTO) {
        Telefone telefone = modelMapper.map(telefoneDTO, Telefone.class);
        Telefone novoTelefone = repositorioTelefone.save(telefone);
        TelefoneDTO novoTelefoneDTO = modelMapper.map(novoTelefone, TelefoneDTO.class);
        EntityModel<TelefoneDTO> telefoneModel = EntityModel.of(novoTelefoneDTO,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(TelefoneControle.class).obterTelefone(novoTelefone.getId())).withSelfRel(),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(TelefoneControle.class).obterTelefones()).withRel("telefones"));
        return new ResponseEntity<>(telefoneModel, HttpStatus.CREATED);
    }

    @PutMapping("telefone/atualizar/{id}")
    public ResponseEntity<EntityModel<TelefoneDTO>> atualizarTelefone(@PathVariable Long id, @RequestBody TelefoneDTO telefoneAtualizadoDTO) {
        Optional<Telefone> telefoneExistente = repositorioTelefone.findById(id);
        if (telefoneExistente.isPresent()) {
            Telefone telefone = modelMapper.map(telefoneAtualizadoDTO, Telefone.class);
            telefone.setId(id);
            Telefone telefoneAtualizado = repositorioTelefone.save(telefone);
            TelefoneDTO telefoneAtualizadoDTOResponse = modelMapper.map(telefoneAtualizado, TelefoneDTO.class);
            EntityModel<TelefoneDTO> telefoneModel = EntityModel.of(telefoneAtualizadoDTOResponse,
                    WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(TelefoneControle.class).obterTelefone(id)).withSelfRel(),
                    WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(TelefoneControle.class).obterTelefones()).withRel("telefones"));
            return new ResponseEntity<>(telefoneModel, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/telefone/excluir/{id}")
    public ResponseEntity<Void> excluirTelefone(@PathVariable Long id) {
        Optional<Telefone> telefone = repositorioTelefone.findById(id);
        if (telefone.isPresent()) {
            repositorioTelefone.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
