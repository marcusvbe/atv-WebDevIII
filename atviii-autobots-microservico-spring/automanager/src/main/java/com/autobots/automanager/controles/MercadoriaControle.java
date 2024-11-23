// src/main/java/com/autobots/automanager/controles/MercadoriaControle.java
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

import com.autobots.automanager.DTOs.MercadoriaDTO;
import com.autobots.automanager.entitades.Mercadoria;
import com.autobots.automanager.repositorios.RepositorioMercadoria;
import org.modelmapper.ModelMapper;

@RestController
@RequestMapping("/")
public class MercadoriaControle {

    @Autowired
    private RepositorioMercadoria repositorioMercadoria;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/mercadorias")
    public ResponseEntity<CollectionModel<EntityModel<MercadoriaDTO>>> obterMercadorias() {
        List<Mercadoria> mercadorias = repositorioMercadoria.findAll();
        List<EntityModel<MercadoriaDTO>> mercadoriasDTO = mercadorias.stream()
                .map(mercadoria -> {
                    MercadoriaDTO mercadoriaDTO = modelMapper.map(mercadoria, MercadoriaDTO.class);
                    return EntityModel.of(mercadoriaDTO,
                            WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(MercadoriaControle.class).obterMercadoria(mercadoria.getId())).withSelfRel(),
                            WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(MercadoriaControle.class).obterMercadorias()).withRel("mercadorias"));
                })
                .collect(Collectors.toList());
        return new ResponseEntity<>(CollectionModel.of(mercadoriasDTO), HttpStatus.OK);
    }

    @GetMapping("mercadoria/{id}")
    public ResponseEntity<EntityModel<MercadoriaDTO>> obterMercadoria(@PathVariable Long id) {
        Optional<Mercadoria> mercadoria = repositorioMercadoria.findById(id);
        if (mercadoria.isPresent()) {
            MercadoriaDTO mercadoriaDTO = modelMapper.map(mercadoria.get(), MercadoriaDTO.class);
            EntityModel<MercadoriaDTO> mercadoriaModel = EntityModel.of(mercadoriaDTO,
                    WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(MercadoriaControle.class).obterMercadoria(id)).withSelfRel(),
                    WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(MercadoriaControle.class).obterMercadorias()).withRel("mercadorias"));
            return new ResponseEntity<>(mercadoriaModel, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/mercadoria/cadastro")
    public ResponseEntity<EntityModel<MercadoriaDTO>> cadastrarMercadoria(@RequestBody MercadoriaDTO mercadoriaDTO) {
        Mercadoria mercadoria = modelMapper.map(mercadoriaDTO, Mercadoria.class);
        Mercadoria novaMercadoria = repositorioMercadoria.save(mercadoria);
        MercadoriaDTO novaMercadoriaDTO = modelMapper.map(novaMercadoria, MercadoriaDTO.class);
        EntityModel<MercadoriaDTO> mercadoriaModel = EntityModel.of(novaMercadoriaDTO,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(MercadoriaControle.class).obterMercadoria(novaMercadoria.getId())).withSelfRel(),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(MercadoriaControle.class).obterMercadorias()).withRel("mercadorias"));
        return new ResponseEntity<>(mercadoriaModel, HttpStatus.CREATED);
    }

    @PutMapping("mercadoria/atualizar/{id}")
    public ResponseEntity<EntityModel<MercadoriaDTO>> atualizarMercadoria(@PathVariable Long id, @RequestBody MercadoriaDTO mercadoriaAtualizadaDTO) {
        Optional<Mercadoria> mercadoriaExistente = repositorioMercadoria.findById(id);
        if (mercadoriaExistente.isPresent()) {
            Mercadoria mercadoria = modelMapper.map(mercadoriaAtualizadaDTO, Mercadoria.class);
            mercadoria.setId(id);
            Mercadoria mercadoriaAtualizada = repositorioMercadoria.save(mercadoria);
            MercadoriaDTO mercadoriaAtualizadaDTOResponse = modelMapper.map(mercadoriaAtualizada, MercadoriaDTO.class);
            EntityModel<MercadoriaDTO> mercadoriaModel = EntityModel.of(mercadoriaAtualizadaDTOResponse,
                    WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(MercadoriaControle.class).obterMercadoria(id)).withSelfRel(),
                    WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(MercadoriaControle.class).obterMercadorias()).withRel("mercadorias"));
            return new ResponseEntity<>(mercadoriaModel, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/mercadoria/excluir/{id}")
    public ResponseEntity<Void> excluirMercadoria(@PathVariable Long id) {
        Optional<Mercadoria> mercadoria = repositorioMercadoria.findById(id);
        if (mercadoria.isPresent()) {
            repositorioMercadoria.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
