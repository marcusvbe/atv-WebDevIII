// src/main/java/com/autobots/automanager/controles/UsuarioControle.java
package com.autobots.automanager.controles;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.autobots.automanager.enumeracoes.PerfilUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.autobots.automanager.DTOs.UsuarioDTO;
import com.autobots.automanager.entitades.Usuario;
import com.autobots.automanager.repositorios.RepositorioUsuario;
import org.modelmapper.ModelMapper;

@RestController
@RequestMapping("/")
public class UsuarioControle {

    @Autowired
    private RepositorioUsuario repositorioUsuario;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/usuarios")
    public ResponseEntity<CollectionModel<EntityModel<UsuarioDTO>>> obterUsuarios() {
        List<Usuario> usuarios = repositorioUsuario.findAll();
        List<EntityModel<UsuarioDTO>> usuariosDTO = usuarios.stream()
                .map(usuario -> {
                    UsuarioDTO usuarioDTO = modelMapper.map(usuario, UsuarioDTO.class);
                    return EntityModel.of(usuarioDTO,
                            WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UsuarioControle.class).obterUsuario(usuario.getId())).withSelfRel(),
                            WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UsuarioControle.class).obterUsuarios()).withRel("usuarios"));
                })
                .collect(Collectors.toList());
        return new ResponseEntity<>(CollectionModel.of(usuariosDTO), HttpStatus.OK);
    }

    @GetMapping("usuario/{id}")
    public ResponseEntity<EntityModel<UsuarioDTO>> obterUsuario(@PathVariable Long id) {
        Optional<Usuario> usuario = repositorioUsuario.findById(id);
        if (usuario.isPresent()) {
            UsuarioDTO usuarioDTO = modelMapper.map(usuario.get(), UsuarioDTO.class);
            EntityModel<UsuarioDTO> usuarioModel = EntityModel.of(usuarioDTO,
                    WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UsuarioControle.class).obterUsuario(id)).withSelfRel(),
                    WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UsuarioControle.class).obterUsuarios()).withRel("usuarios"));
            return new ResponseEntity<>(usuarioModel, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/usuario/cadastro")
    public ResponseEntity<EntityModel<UsuarioDTO>> cadastrarUsuario(@RequestBody UsuarioDTO usuarioDTO) {
        Usuario usuario = modelMapper.map(usuarioDTO, Usuario.class);
        Usuario novoUsuario = repositorioUsuario.save(usuario);
        UsuarioDTO novoUsuarioDTO = modelMapper.map(novoUsuario, UsuarioDTO.class);
        EntityModel<UsuarioDTO> usuarioModel = EntityModel.of(novoUsuarioDTO,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UsuarioControle.class).obterUsuario(novoUsuario.getId())).withSelfRel(),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UsuarioControle.class).obterUsuarios()).withRel("usuarios"));
        return new ResponseEntity<>(usuarioModel, HttpStatus.CREATED);
    }

    @PutMapping("usuario/atualizar/{id}")
    public ResponseEntity<EntityModel<UsuarioDTO>> atualizarUsuario(@PathVariable Long id, @RequestBody UsuarioDTO usuarioAtualizadoDTO) {
        Optional<Usuario> usuarioExistente = repositorioUsuario.findById(id);
        if (usuarioExistente.isPresent()) {
            Usuario usuario = modelMapper.map(usuarioAtualizadoDTO, Usuario.class);
            usuario.setId(id);
            Usuario usuarioAtualizado = repositorioUsuario.save(usuario);
            UsuarioDTO usuarioAtualizadoDTOResponse = modelMapper.map(usuarioAtualizado, UsuarioDTO.class);
            EntityModel<UsuarioDTO> usuarioModel = EntityModel.of(usuarioAtualizadoDTOResponse,
                    WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UsuarioControle.class).obterUsuario(id)).withSelfRel(),
                    WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UsuarioControle.class).obterUsuarios()).withRel("usuarios"));
            return new ResponseEntity<>(usuarioModel, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/usuario/{id}/adicionar-perfil")
    public ResponseEntity<Usuario> adicionarPerfilUsuario(@PathVariable Long id, @RequestParam PerfilUsuario perfil) {
        Optional<Usuario> usuarioOpt = repositorioUsuario.findById(id);
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            usuario.getPerfis().add(perfil);
            repositorioUsuario.save(usuario);
            return new ResponseEntity<>(usuario, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }



    @DeleteMapping("/usuario/excluir/{id}")
    public ResponseEntity<Void> excluirUsuario(@PathVariable Long id) {
        Optional<Usuario> usuario = repositorioUsuario.findById(id);
        if (usuario.isPresent()) {
            repositorioUsuario.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
