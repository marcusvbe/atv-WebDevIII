// src/main/java/com/autobots/automanager/controles/EnderecoControle.java
package com.autobots.automanager.controles;

import com.autobots.automanager.entidades.Endereco;
import com.autobots.automanager.repositorios.EnderecoRepositorio;
import com.autobots.automanager.modelo.EnderecoAtualizador;
import com.autobots.automanager.modelo.AdicionadorLinkEndereco;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/")
public class EnderecoControle {

    @Autowired
    private EnderecoRepositorio EnderecoRepositorio;

    @Autowired
    private EnderecoAtualizador atualizador;
    @Autowired
    private AdicionadorLinkEndereco adicionadorLink;

    @GetMapping("/endereco/{id}")
    public ResponseEntity<Endereco> obterEndereco(@PathVariable long id) {
        Endereco endereco = EnderecoRepositorio.findById(id).orElse(null);
        if (endereco == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            adicionadorLink.adicionarLink(endereco);
            return new ResponseEntity<>(endereco, HttpStatus.FOUND);
        }
    }

    @GetMapping("/enderecos")
    public ResponseEntity<List<Endereco>> obterEnderecos() {
        List<Endereco> enderecos = EnderecoRepositorio.findAll();
        if (enderecos.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            adicionadorLink.adicionarLink(enderecos);
            return new ResponseEntity<>(enderecos, HttpStatus.FOUND);
        }
    }

    @PostMapping("/endereco/cadastro")
    public Endereco cadastrarEndereco(@RequestBody Endereco endereco) {
        return EnderecoRepositorio.save(endereco);
    }

    @PutMapping("/endereco/atualizar/{id}")
    public Endereco atualizarEndereco(@RequestBody Endereco atualizacao) {
        Endereco endereco = EnderecoRepositorio.findById(atualizacao.getId()).orElse(null);
        if (endereco != null) {
            atualizador.atualizar(endereco, atualizacao);
            return EnderecoRepositorio.save(endereco);
        }
        return null;
    }

    @DeleteMapping("/endereco/excluir/{id}")
    public void excluirEndereco(@PathVariable Long id) {
        EnderecoRepositorio.deleteById(id);
    }
}