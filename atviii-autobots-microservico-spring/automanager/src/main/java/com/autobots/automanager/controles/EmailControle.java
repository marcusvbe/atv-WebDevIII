// src/main/java/com/autobots/automanager/controles/EmailControle.java
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

import com.autobots.automanager.DTOs.EmailDTO;
import com.autobots.automanager.entitades.Email;
import com.autobots.automanager.repositorios.RepositorioEmail;
import org.modelmapper.ModelMapper;

@RestController
@RequestMapping("/")
public class EmailControle {

    @Autowired
    private RepositorioEmail repositorioEmail;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/emails")
    public ResponseEntity<CollectionModel<EntityModel<EmailDTO>>> obterEmails() {
        List<Email> emails = repositorioEmail.findAll();
        List<EntityModel<EmailDTO>> emailsDTO = emails.stream()
                .map(email -> {
                    EmailDTO emailDTO = modelMapper.map(email, EmailDTO.class);
                    return EntityModel.of(emailDTO,
                            WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EmailControle.class).obterEmail(email.getId())).withSelfRel(),
                            WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EmailControle.class).obterEmails()).withRel("emails"));
                })
                .collect(Collectors.toList());
        return new ResponseEntity<>(CollectionModel.of(emailsDTO), HttpStatus.OK);
    }

    @GetMapping("email/{id}")
    public ResponseEntity<EntityModel<EmailDTO>> obterEmail(@PathVariable Long id) {
        Optional<Email> email = repositorioEmail.findById(id);
        if (email.isPresent()) {
            EmailDTO emailDTO = modelMapper.map(email.get(), EmailDTO.class);
            EntityModel<EmailDTO> emailModel = EntityModel.of(emailDTO,
                    WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EmailControle.class).obterEmail(id)).withSelfRel(),
                    WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EmailControle.class).obterEmails()).withRel("emails"));
            return new ResponseEntity<>(emailModel, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/email/cadastro")
    public ResponseEntity<EntityModel<EmailDTO>> cadastrarEmail(@RequestBody EmailDTO emailDTO) {
        Email email = modelMapper.map(emailDTO, Email.class);
        Email novoEmail = repositorioEmail.save(email);
        EmailDTO novoEmailDTO = modelMapper.map(novoEmail, EmailDTO.class);
        EntityModel<EmailDTO> emailModel = EntityModel.of(novoEmailDTO,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EmailControle.class).obterEmail(novoEmail.getId())).withSelfRel(),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EmailControle.class).obterEmails()).withRel("emails"));
        return new ResponseEntity<>(emailModel, HttpStatus.CREATED);
    }

    @PutMapping("email/atualizar/{id}")
    public ResponseEntity<EntityModel<EmailDTO>> atualizarEmail(@PathVariable Long id, @RequestBody EmailDTO emailAtualizadoDTO) {
        Optional<Email> emailExistente = repositorioEmail.findById(id);
        if (emailExistente.isPresent()) {
            Email email = modelMapper.map(emailAtualizadoDTO, Email.class);
            email.setId(id);
            Email emailAtualizado = repositorioEmail.save(email);
            EmailDTO emailAtualizadoDTOResponse = modelMapper.map(emailAtualizado, EmailDTO.class);
            EntityModel<EmailDTO> emailModel = EntityModel.of(emailAtualizadoDTOResponse,
                    WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EmailControle.class).obterEmail(id)).withSelfRel(),
                    WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EmailControle.class).obterEmails()).withRel("emails"));
            return new ResponseEntity<>(emailModel, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/email/excluir/{id}")
    public ResponseEntity<Void> excluirEmail(@PathVariable Long id) {
        Optional<Email> email = repositorioEmail.findById(id);
        if (email.isPresent()) {
            repositorioEmail.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
