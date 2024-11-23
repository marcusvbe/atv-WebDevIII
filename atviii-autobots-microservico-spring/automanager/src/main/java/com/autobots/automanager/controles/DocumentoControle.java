// src/main/java/com/autobots/automanager/controles/DocumentoControle.java
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

import com.autobots.automanager.DTOs.DocumentoDTO;
import com.autobots.automanager.entitades.Documento;
import com.autobots.automanager.repositorios.RepositorioDocumento;
import org.modelmapper.ModelMapper;

@RestController
@RequestMapping("/")
public class DocumentoControle {

    @Autowired
    private RepositorioDocumento repositorioDocumento;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/documentos")
    public ResponseEntity<CollectionModel<EntityModel<DocumentoDTO>>> obterDocumentos() {
        List<Documento> documentos = repositorioDocumento.findAll();
        List<EntityModel<DocumentoDTO>> documentosDTO = documentos.stream()
                .map(documento -> {
                    DocumentoDTO documentoDTO = modelMapper.map(documento, DocumentoDTO.class);
                    return EntityModel.of(documentoDTO,
                            WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(DocumentoControle.class).obterDocumento(documento.getId())).withSelfRel(),
                            WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(DocumentoControle.class).obterDocumentos()).withRel("documentos"));
                })
                .collect(Collectors.toList());
        return new ResponseEntity<>(CollectionModel.of(documentosDTO), HttpStatus.OK);
    }

    @GetMapping("documento/{id}")
    public ResponseEntity<EntityModel<DocumentoDTO>> obterDocumento(@PathVariable Long id) {
        Optional<Documento> documento = repositorioDocumento.findById(id);
        if (documento.isPresent()) {
            DocumentoDTO documentoDTO = modelMapper.map(documento.get(), DocumentoDTO.class);
            EntityModel<DocumentoDTO> documentoModel = EntityModel.of(documentoDTO,
                    WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(DocumentoControle.class).obterDocumento(id)).withSelfRel(),
                    WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(DocumentoControle.class).obterDocumentos()).withRel("documentos"));
            return new ResponseEntity<>(documentoModel, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/documento/cadastro")
    public ResponseEntity<EntityModel<DocumentoDTO>> cadastrarDocumento(@RequestBody DocumentoDTO documentoDTO) {
        Documento documento = modelMapper.map(documentoDTO, Documento.class);
        Documento novoDocumento = repositorioDocumento.save(documento);
        DocumentoDTO novoDocumentoDTO = modelMapper.map(novoDocumento, DocumentoDTO.class);
        EntityModel<DocumentoDTO> documentoModel = EntityModel.of(novoDocumentoDTO,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(DocumentoControle.class).obterDocumento(novoDocumento.getId())).withSelfRel(),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(DocumentoControle.class).obterDocumentos()).withRel("documentos"));
        return new ResponseEntity<>(documentoModel, HttpStatus.CREATED);
    }

    @PutMapping("documento/atualizar/{id}")
    public ResponseEntity<EntityModel<DocumentoDTO>> atualizarDocumento(@PathVariable Long id, @RequestBody DocumentoDTO documentoAtualizadoDTO) {
        Optional<Documento> documentoExistente = repositorioDocumento.findById(id);
        if (documentoExistente.isPresent()) {
            Documento documento = modelMapper.map(documentoAtualizadoDTO, Documento.class);
            documento.setId(id);
            Documento documentoAtualizado = repositorioDocumento.save(documento);
            DocumentoDTO documentoAtualizadoDTOResponse = modelMapper.map(documentoAtualizado, DocumentoDTO.class);
            EntityModel<DocumentoDTO> documentoModel = EntityModel.of(documentoAtualizadoDTOResponse,
                    WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(DocumentoControle.class).obterDocumento(id)).withSelfRel(),
                    WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(DocumentoControle.class).obterDocumentos()).withRel("documentos"));
            return new ResponseEntity<>(documentoModel, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/documento/excluir/{id}")
    public ResponseEntity<Void> excluirDocumento(@PathVariable Long id) {
        Optional<Documento> documento = repositorioDocumento.findById(id);
        if (documento.isPresent()) {
            repositorioDocumento.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
