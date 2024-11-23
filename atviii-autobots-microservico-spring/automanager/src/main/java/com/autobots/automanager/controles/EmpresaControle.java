// src/main/java/com/autobots/automanager/controles/EmpresaControle.java
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

import com.autobots.automanager.DTOs.EmpresaDTO;
import com.autobots.automanager.entitades.Empresa;
import com.autobots.automanager.repositorios.RepositorioEmpresa;
import org.modelmapper.ModelMapper;

@RestController
@RequestMapping("/")
public class EmpresaControle {

    @Autowired
    private RepositorioEmpresa repositorioEmpresa;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/empresas")
    public ResponseEntity<CollectionModel<EntityModel<EmpresaDTO>>> obterEmpresas() {
        List<Empresa> empresas = repositorioEmpresa.findAll();
        List<EntityModel<EmpresaDTO>> empresasDTO = empresas.stream()
                .map(empresa -> {
                    EmpresaDTO empresaDTO = modelMapper.map(empresa, EmpresaDTO.class);
                    return EntityModel.of(empresaDTO,
                            WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EmpresaControle.class).obterEmpresa(empresa.getId())).withSelfRel(),
                            WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EmpresaControle.class).obterEmpresas()).withRel("empresas"));

                })
                .collect(Collectors.toList());
        return new ResponseEntity<>(CollectionModel.of(empresasDTO), HttpStatus.OK);
    }

    @GetMapping("empresa/{id}")
    public ResponseEntity<EntityModel<EmpresaDTO>> obterEmpresa(@PathVariable Long id) {
        Optional<Empresa> empresa = repositorioEmpresa.findById(id);
        if (empresa.isPresent()) {
            EmpresaDTO empresaDTO = modelMapper.map(empresa.get(), EmpresaDTO.class);
            EntityModel<EmpresaDTO> empresaModel = EntityModel.of(empresaDTO,
                    WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EmpresaControle.class).obterEmpresa(id)).withSelfRel(),
                    WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EmpresaControle.class).obterEmpresas()).withRel("empresas"));
            return new ResponseEntity<>(empresaModel, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/empresa/cadastro")
    public ResponseEntity<EntityModel<EmpresaDTO>> cadastrarEmpresa(@RequestBody EmpresaDTO empresaDTO) {
        Empresa empresa = modelMapper.map(empresaDTO, Empresa.class);
        Empresa novaEmpresa = repositorioEmpresa.save(empresa);
        EmpresaDTO novaEmpresaDTO = modelMapper.map(novaEmpresa, EmpresaDTO.class);
        EntityModel<EmpresaDTO> empresaModel = EntityModel.of(novaEmpresaDTO,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EmpresaControle.class).obterEmpresa(novaEmpresa.getId())).withSelfRel(),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EmpresaControle.class).obterEmpresas()).withRel("empresas"));
        return new ResponseEntity<>(empresaModel, HttpStatus.CREATED);
    }

    @PutMapping("empresa/atualizar/{id}")
    public ResponseEntity<EntityModel<EmpresaDTO>> atualizarEmpresa(@PathVariable Long id, @RequestBody EmpresaDTO empresaAtualizadaDTO) {
        Optional<Empresa> empresaExistente = repositorioEmpresa.findById(id);
        if (empresaExistente.isPresent()) {
            Empresa empresa = modelMapper.map(empresaAtualizadaDTO, Empresa.class);
            empresa.setId(id);
            Empresa empresaAtualizada = repositorioEmpresa.save(empresa);
            EmpresaDTO empresaAtualizadaDTOResponse = modelMapper.map(empresaAtualizada, EmpresaDTO.class);
            EntityModel<EmpresaDTO> empresaModel = EntityModel.of(empresaAtualizadaDTOResponse,
                    WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EmpresaControle.class).obterEmpresa(id)).withSelfRel(),
                    WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EmpresaControle.class).obterEmpresas()).withRel("empresas"));
            return new ResponseEntity<>(empresaModel, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/empresa/excluir/{id}")
    public ResponseEntity<Void> excluirEmpresa(@PathVariable Long id) {
        Optional<Empresa> empresa = repositorioEmpresa.findById(id);
        if (empresa.isPresent()) {
            repositorioEmpresa.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
