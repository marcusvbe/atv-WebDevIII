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

import com.autobots.automanager.DTOs.ServicoDTO;
import com.autobots.automanager.entitades.Servico;
import com.autobots.automanager.repositorios.RepositorioServico;
import org.modelmapper.ModelMapper;

@RestController
@RequestMapping("/")
public class ServicoControle {

    @Autowired
    private RepositorioServico repositorioServico;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/servicos")
    public ResponseEntity<CollectionModel<EntityModel<ServicoDTO>>> obterServicos() {
        List<Servico> servicos = repositorioServico.findAll();
        List<EntityModel<ServicoDTO>> servicosDTO = servicos.stream()
                .map(servico -> {
                    ServicoDTO servicoDTO = modelMapper.map(servico, ServicoDTO.class);
                    return EntityModel.of(servicoDTO,
                            WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ServicoControle.class).obterServico(servico.getId())).withSelfRel(),
                            WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ServicoControle.class).obterServicos()).withRel("servicos"));
                })
                .collect(Collectors.toList());
        return new ResponseEntity<>(CollectionModel.of(servicosDTO), HttpStatus.OK);
    }

    @GetMapping("servico/{id}")
    public ResponseEntity<EntityModel<ServicoDTO>> obterServico(@PathVariable Long id) {
        Optional<Servico> servico = repositorioServico.findById(id);
        if (servico.isPresent()) {
            ServicoDTO servicoDTO = modelMapper.map(servico.get(), ServicoDTO.class);
            EntityModel<ServicoDTO> servicoModel = EntityModel.of(servicoDTO,
                    WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ServicoControle.class).obterServico(id)).withSelfRel(),
                    WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ServicoControle.class).obterServicos()).withRel("servicos"));
            return new ResponseEntity<>(servicoModel, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/servico/cadastro")
    public ResponseEntity<EntityModel<ServicoDTO>> cadastrarServico(@RequestBody ServicoDTO servicoDTO) {
        Servico servico = modelMapper.map(servicoDTO, Servico.class);
        Servico novoServico = repositorioServico.save(servico);
        ServicoDTO novoServicoDTO = modelMapper.map(novoServico, ServicoDTO.class);
        EntityModel<ServicoDTO> servicoModel = EntityModel.of(novoServicoDTO,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ServicoControle.class).obterServico(novoServico.getId())).withSelfRel(),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ServicoControle.class).obterServicos()).withRel("servicos"));
        return new ResponseEntity<>(servicoModel, HttpStatus.CREATED);
    }

    @PutMapping("servico/atualizar/{id}")
    public ResponseEntity<EntityModel<ServicoDTO>> atualizarServico(@PathVariable Long id, @RequestBody ServicoDTO servicoAtualizadoDTO) {
        Optional<Servico> servicoExistente = repositorioServico.findById(id);
        if (servicoExistente.isPresent()) {
            Servico servico = modelMapper.map(servicoAtualizadoDTO, Servico.class);
            servico.setId(id);
            Servico servicoAtualizado = repositorioServico.save(servico);
            ServicoDTO servicoAtualizadoDTOResponse = modelMapper.map(servicoAtualizado, ServicoDTO.class);
            EntityModel<ServicoDTO> servicoModel = EntityModel.of(servicoAtualizadoDTOResponse,
                    WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ServicoControle.class).obterServico(id)).withSelfRel(),
                    WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ServicoControle.class).obterServicos()).withRel("servicos"));
            return new ResponseEntity<>(servicoModel, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/servico/excluir/{id}")
    public ResponseEntity<Void> excluirServico(@PathVariable Long id) {
        Optional<Servico> servico = repositorioServico.findById(id);
        if (servico.isPresent()) {
            repositorioServico.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
