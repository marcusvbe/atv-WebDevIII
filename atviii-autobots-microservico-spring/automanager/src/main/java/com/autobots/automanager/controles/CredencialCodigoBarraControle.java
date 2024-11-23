// src/main/java/com/autobots/automanager/controles/CredencialCodigoBarraControle.java
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

import com.autobots.automanager.DTOs.CredencialCodigoBarraDTO;
import com.autobots.automanager.entitades.CredencialCodigoBarra;
import com.autobots.automanager.repositorios.RepositorioCredencialCodigoBarra;
import org.modelmapper.ModelMapper;

@RestController
@RequestMapping("/")
public class CredencialCodigoBarraControle {

    @Autowired
    private RepositorioCredencialCodigoBarra repositorioCredencialCodigoBarra;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/credenciais-codigo-barra")
    public ResponseEntity<CollectionModel<EntityModel<CredencialCodigoBarraDTO>>> obterCredenciaisCodigoBarra() {
        List<CredencialCodigoBarra> credenciais = repositorioCredencialCodigoBarra.findAll();
        List<EntityModel<CredencialCodigoBarraDTO>> credenciaisDTO = credenciais.stream()
                .map(credencial -> {
                    CredencialCodigoBarraDTO credencialDTO = modelMapper.map(credencial, CredencialCodigoBarraDTO.class);
                    return EntityModel.of(credencialDTO,
                            WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CredencialCodigoBarraControle.class).obterCredencialCodigoBarra(credencial.getId())).withSelfRel(),
                            WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CredencialCodigoBarraControle.class).obterCredenciaisCodigoBarra()).withRel("credenciais-codigo-barra"));
                })
                .collect(Collectors.toList());
        return new ResponseEntity<>(CollectionModel.of(credenciaisDTO), HttpStatus.OK);
    }

    @GetMapping("credencial-codigo-barra/{id}")
    public ResponseEntity<EntityModel<CredencialCodigoBarraDTO>> obterCredencialCodigoBarra(@PathVariable Long id) {
        Optional<CredencialCodigoBarra> credencial = repositorioCredencialCodigoBarra.findById(id);
        if (credencial.isPresent()) {
            CredencialCodigoBarraDTO credencialDTO = modelMapper.map(credencial.get(), CredencialCodigoBarraDTO.class);
            EntityModel<CredencialCodigoBarraDTO> credencialModel = EntityModel.of(credencialDTO,
                    WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CredencialCodigoBarraControle.class).obterCredencialCodigoBarra(id)).withSelfRel(),
                    WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CredencialCodigoBarraControle.class).obterCredenciaisCodigoBarra()).withRel("credenciais-codigo-barra"));
            return new ResponseEntity<>(credencialModel, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/credencial-codigo-barra/cadastro")
    public ResponseEntity<EntityModel<CredencialCodigoBarraDTO>> cadastrarCredencialCodigoBarra(@RequestBody CredencialCodigoBarraDTO credencialDTO) {
        CredencialCodigoBarra credencial = modelMapper.map(credencialDTO, CredencialCodigoBarra.class);
        CredencialCodigoBarra novaCredencial = repositorioCredencialCodigoBarra.save(credencial);
        CredencialCodigoBarraDTO novaCredencialDTO = modelMapper.map(novaCredencial, CredencialCodigoBarraDTO.class);
        EntityModel<CredencialCodigoBarraDTO> credencialModel = EntityModel.of(novaCredencialDTO,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CredencialCodigoBarraControle.class).obterCredencialCodigoBarra(novaCredencial.getId())).withSelfRel(),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CredencialCodigoBarraControle.class).obterCredenciaisCodigoBarra()).withRel("credenciais-codigo-barra"));
        return new ResponseEntity<>(credencialModel, HttpStatus.CREATED);
    }

    @PutMapping("credencial-codigo-barra/atualizar/{id}")
    public ResponseEntity<EntityModel<CredencialCodigoBarraDTO>> atualizarCredencialCodigoBarra(@PathVariable Long id, @RequestBody CredencialCodigoBarraDTO credencialAtualizadaDTO) {
        Optional<CredencialCodigoBarra> credencialExistente = repositorioCredencialCodigoBarra.findById(id);
        if (credencialExistente.isPresent()) {
            CredencialCodigoBarra credencial = modelMapper.map(credencialAtualizadaDTO, CredencialCodigoBarra.class);
            credencial.setId(id);
            CredencialCodigoBarra credencialAtualizada = repositorioCredencialCodigoBarra.save(credencial);
            CredencialCodigoBarraDTO credencialAtualizadaDTOResponse = modelMapper.map(credencialAtualizada, CredencialCodigoBarraDTO.class);
            EntityModel<CredencialCodigoBarraDTO> credencialModel = EntityModel.of(credencialAtualizadaDTOResponse,
                    WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CredencialCodigoBarraControle.class).obterCredencialCodigoBarra(id)).withSelfRel(),
                    WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CredencialCodigoBarraControle.class).obterCredenciaisCodigoBarra()).withRel("credenciais-codigo-barra"));
            return new ResponseEntity<>(credencialModel, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/credencial-codigo-barra/excluir/{id}")
    public ResponseEntity<Void> excluirCredencialCodigoBarra(@PathVariable Long id) {
        Optional<CredencialCodigoBarra> credencial = repositorioCredencialCodigoBarra.findById(id);
        if (credencial.isPresent()) {
            repositorioCredencialCodigoBarra.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
