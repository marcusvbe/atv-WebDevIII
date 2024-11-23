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

import com.autobots.automanager.DTOs.VeiculoDTO;
import com.autobots.automanager.entitades.Veiculo;
import com.autobots.automanager.repositorios.RepositorioVeiculo;
import org.modelmapper.ModelMapper;

@RestController
@RequestMapping("/")
public class VeiculoControle {

    @Autowired
    private RepositorioVeiculo repositorioVeiculo;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/veiculos")
    public ResponseEntity<CollectionModel<EntityModel<VeiculoDTO>>> obterVeiculos() {
        List<Veiculo> veiculos = repositorioVeiculo.findAll();
        List<EntityModel<VeiculoDTO>> veiculosDTO = veiculos.stream()
                .map(veiculo -> {
                    VeiculoDTO veiculoDTO = modelMapper.map(veiculo, VeiculoDTO.class);
                    return EntityModel.of(veiculoDTO,
                            WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(VeiculoControle.class).obterVeiculo(veiculo.getId())).withSelfRel(),
                            WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(VeiculoControle.class).obterVeiculos()).withRel("veiculos"));
                })
                .collect(Collectors.toList());
        return new ResponseEntity<>(CollectionModel.of(veiculosDTO), HttpStatus.OK);
    }

    @GetMapping("veiculo/{id}")
    public ResponseEntity<EntityModel<VeiculoDTO>> obterVeiculo(@PathVariable Long id) {
        Optional<Veiculo> veiculo = repositorioVeiculo.findById(id);
        if (veiculo.isPresent()) {
            VeiculoDTO veiculoDTO = modelMapper.map(veiculo.get(), VeiculoDTO.class);
            EntityModel<VeiculoDTO> veiculoModel = EntityModel.of(veiculoDTO,
                    WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(VeiculoControle.class).obterVeiculo(id)).withSelfRel(),
                    WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(VeiculoControle.class).obterVeiculos()).withRel("veiculos"));
            return new ResponseEntity<>(veiculoModel, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/veiculo/cadastro")
    public ResponseEntity<EntityModel<VeiculoDTO>> cadastrarVeiculo(@RequestBody VeiculoDTO veiculoDTO) {
        Veiculo veiculo = modelMapper.map(veiculoDTO, Veiculo.class);
        Veiculo novoVeiculo = repositorioVeiculo.save(veiculo);
        VeiculoDTO novoVeiculoDTO = modelMapper.map(novoVeiculo, VeiculoDTO.class);
        EntityModel<VeiculoDTO> veiculoModel = EntityModel.of(novoVeiculoDTO,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(VeiculoControle.class).obterVeiculo(novoVeiculo.getId())).withSelfRel(),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(VeiculoControle.class).obterVeiculos()).withRel("veiculos"));
        return new ResponseEntity<>(veiculoModel, HttpStatus.CREATED);
    }

    @PutMapping("veiculo/atualizar/{id}")
    public ResponseEntity<EntityModel<VeiculoDTO>> atualizarVeiculo(@PathVariable Long id, @RequestBody VeiculoDTO veiculoAtualizadoDTO) {
        Optional<Veiculo> veiculoExistente = repositorioVeiculo.findById(id);
        if (veiculoExistente.isPresent()) {
            Veiculo veiculo = modelMapper.map(veiculoAtualizadoDTO, Veiculo.class);
            veiculo.setId(id);
            Veiculo veiculoAtualizado = repositorioVeiculo.save(veiculo);
            VeiculoDTO veiculoAtualizadoDTOResponse = modelMapper.map(veiculoAtualizado, VeiculoDTO.class);
            EntityModel<VeiculoDTO> veiculoModel = EntityModel.of(veiculoAtualizadoDTOResponse,
                    WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(VeiculoControle.class).obterVeiculo(id)).withSelfRel(),
                    WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(VeiculoControle.class).obterVeiculos()).withRel("veiculos"));
            return new ResponseEntity<>(veiculoModel, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/veiculo/excluir/{id}")
    public ResponseEntity<Void> excluirVeiculo(@PathVariable Long id) {
        Optional<Veiculo> veiculo = repositorioVeiculo.findById(id);
        if (veiculo.isPresent()) {
            repositorioVeiculo.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
