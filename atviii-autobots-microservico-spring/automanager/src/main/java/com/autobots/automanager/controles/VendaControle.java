package com.autobots.automanager.controles;

import com.autobots.automanager.DTOs.VendaDTO;
import com.autobots.automanager.entitades.Venda;
import com.autobots.automanager.repositorios.RepositorioVenda;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/")
public class VendaControle {

    @Autowired
    private RepositorioVenda repositorioVenda;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/vendas")
    public ResponseEntity<CollectionModel<EntityModel<VendaDTO>>> obterVendas() {
        List<Venda> vendas = repositorioVenda.findAll();
        List<EntityModel<VendaDTO>> vendasDTO = vendas.stream()
                .map(venda -> {
                    VendaDTO vendaDTO = modelMapper.map(venda, VendaDTO.class);
                    return EntityModel.of(vendaDTO,
                            WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(VendaControle.class).obterVenda(venda.getId())).withSelfRel(),
                            WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(VendaControle.class).obterVendas()).withRel("vendas"));
                })
                .collect(Collectors.toList());
        return new ResponseEntity<>(CollectionModel.of(vendasDTO), HttpStatus.OK);
    }

    @GetMapping("venda/{id}")
    public ResponseEntity<EntityModel<VendaDTO>> obterVenda(@PathVariable Long id) {
        Optional<Venda> venda = repositorioVenda.findById(id);
        if (venda.isPresent()) {
            VendaDTO vendaDTO = modelMapper.map(venda.get(), VendaDTO.class);
            EntityModel<VendaDTO> vendaModel = EntityModel.of(vendaDTO,
                    WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(VendaControle.class).obterVenda(id)).withSelfRel(),
                    WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(VendaControle.class).obterVendas()).withRel("vendas"));
            return new ResponseEntity<>(vendaModel, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/venda/cadastro")
    public ResponseEntity<EntityModel<VendaDTO>> cadastrarVenda(@RequestBody VendaDTO vendaDTO) {
        Venda venda = modelMapper.map(vendaDTO, Venda.class);
        Venda novaVenda = repositorioVenda.save(venda);
        VendaDTO novaVendaDTO = modelMapper.map(novaVenda, VendaDTO.class);
        EntityModel<VendaDTO> vendaModel = EntityModel.of(novaVendaDTO,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(VendaControle.class).obterVenda(novaVenda.getId())).withSelfRel(),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(VendaControle.class).obterVendas()).withRel("vendas"));
        return new ResponseEntity<>(vendaModel, HttpStatus.CREATED);
    }

    @PutMapping("venda/atualizar/{id}")
    public ResponseEntity<EntityModel<VendaDTO>> atualizarVenda(@PathVariable Long id, @RequestBody VendaDTO vendaAtualizadaDTO) {
        Optional<Venda> vendaExistente = repositorioVenda.findById(id);
        if (vendaExistente.isPresent()) {
            Venda venda = modelMapper.map(vendaAtualizadaDTO, Venda.class);
            venda.setId(id);
            Venda vendaAtualizada = repositorioVenda.save(venda);
            VendaDTO vendaAtualizadaDTOResponse = modelMapper.map(vendaAtualizada, VendaDTO.class);
            EntityModel<VendaDTO> vendaModel = EntityModel.of(vendaAtualizadaDTOResponse,
                    WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(VendaControle.class).obterVenda(id)).withSelfRel(),
                    WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(VendaControle.class).obterVendas()).withRel("vendas"));
            return new ResponseEntity<>(vendaModel, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/venda/excluir/{id}")
    public ResponseEntity<Void> excluirVenda(@PathVariable Long id) {
        Optional<Venda> venda = repositorioVenda.findById(id);
        if (venda.isPresent()) {
            repositorioVenda.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
