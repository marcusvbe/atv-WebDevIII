// src/main/java/com/autobots/automanager/controles/CredencialUsuarioSenhaControle.java
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

import com.autobots.automanager.DTOs.CredencialUsuarioSenhaDTO;
import com.autobots.automanager.entitades.CredencialUsuarioSenha;
import com.autobots.automanager.repositorios.RepositorioCredencialUsuarioSenha;
import org.modelmapper.ModelMapper;

@RestController
@RequestMapping("/")
public class CredencialUsuarioSenhaControle {

    @Autowired
    private RepositorioCredencialUsuarioSenha repositorioCredencialUsuarioSenha;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/credenciais-usuario-senha")
    public ResponseEntity<CollectionModel<EntityModel<CredencialUsuarioSenhaDTO>>> obterCredenciaisUsuarioSenha() {
        List<CredencialUsuarioSenha> credenciais = repositorioCredencialUsuarioSenha.findAll();
        List<EntityModel<CredencialUsuarioSenhaDTO>> credenciaisDTO = credenciais.stream()
                .map(credencial -> {
                    CredencialUsuarioSenhaDTO credencialDTO = modelMapper.map(credencial, CredencialUsuarioSenhaDTO.class);
                    return EntityModel.of(credencialDTO,
                            WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CredencialUsuarioSenhaControle.class).obterCredencialUsuarioSenha(credencial.getId())).withSelfRel(),
                            WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CredencialUsuarioSenhaControle.class).obterCredenciaisUsuarioSenha()).withRel("credenciais-usuario-senha"));
                })
                .collect(Collectors.toList());
        return new ResponseEntity<>(CollectionModel.of(credenciaisDTO), HttpStatus.OK);
    }

    @GetMapping("credencial-usuario-senha/{id}")
    public ResponseEntity<EntityModel<CredencialUsuarioSenhaDTO>> obterCredencialUsuarioSenha(@PathVariable Long id) {
        Optional<CredencialUsuarioSenha> credencial = repositorioCredencialUsuarioSenha.findById(id);
        if (credencial.isPresent()) {
            CredencialUsuarioSenhaDTO credencialDTO = modelMapper.map(credencial.get(), CredencialUsuarioSenhaDTO.class);
            EntityModel<CredencialUsuarioSenhaDTO> credencialModel = EntityModel.of(credencialDTO,
                    WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CredencialUsuarioSenhaControle.class).obterCredencialUsuarioSenha(id)).withSelfRel(),
                    WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CredencialUsuarioSenhaControle.class).obterCredenciaisUsuarioSenha()).withRel("credenciais-usuario-senha"));
            return new ResponseEntity<>(credencialModel, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/credencial-usuario-senha/cadastro")
    public ResponseEntity<EntityModel<CredencialUsuarioSenhaDTO>> cadastrarCredencialUsuarioSenha(@RequestBody CredencialUsuarioSenhaDTO credencialDTO) {
        CredencialUsuarioSenha credencial = modelMapper.map(credencialDTO, CredencialUsuarioSenha.class);
        CredencialUsuarioSenha novaCredencial = repositorioCredencialUsuarioSenha.save(credencial);
        CredencialUsuarioSenhaDTO novaCredencialDTO = modelMapper.map(novaCredencial, CredencialUsuarioSenhaDTO.class);
        EntityModel<CredencialUsuarioSenhaDTO> credencialModel = EntityModel.of(novaCredencialDTO,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CredencialUsuarioSenhaControle.class).obterCredencialUsuarioSenha(novaCredencial.getId())).withSelfRel(),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CredencialUsuarioSenhaControle.class).obterCredenciaisUsuarioSenha()).withRel("credenciais-usuario-senha"));
        return new ResponseEntity<>(credencialModel, HttpStatus.CREATED);
    }

    @PutMapping("credencial-usuario-senha/atualizar/{id}")
    public ResponseEntity<EntityModel<CredencialUsuarioSenhaDTO>> atualizarCredencialUsuarioSenha(@PathVariable Long id, @RequestBody CredencialUsuarioSenhaDTO credencialAtualizadaDTO) {
        Optional<CredencialUsuarioSenha> credencialExistente = repositorioCredencialUsuarioSenha.findById(id);
        if (credencialExistente.isPresent()) {
            CredencialUsuarioSenha credencial = modelMapper.map(credencialAtualizadaDTO, CredencialUsuarioSenha.class);
            credencial.setId(id);
            CredencialUsuarioSenha credencialAtualizada = repositorioCredencialUsuarioSenha.save(credencial);
            CredencialUsuarioSenhaDTO credencialAtualizadaDTOResponse = modelMapper.map(credencialAtualizada, CredencialUsuarioSenhaDTO.class);
            EntityModel<CredencialUsuarioSenhaDTO> credencialModel = EntityModel.of(credencialAtualizadaDTOResponse,
                    WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CredencialUsuarioSenhaControle.class).obterCredencialUsuarioSenha(id)).withSelfRel(),
                    WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CredencialUsuarioSenhaControle.class).obterCredenciaisUsuarioSenha()).withRel("credenciais-usuario-senha"));
            return new ResponseEntity<>(credencialModel, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/credencial-usuario-senha/excluir/{id}")
    public ResponseEntity<Void> excluirCredencialUsuarioSenha(@PathVariable Long id) {
        Optional<CredencialUsuarioSenha> credencial = repositorioCredencialUsuarioSenha.findById(id);
        if (credencial.isPresent()) {
            repositorioCredencialUsuarioSenha.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
