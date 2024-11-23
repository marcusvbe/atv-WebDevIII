// src/main/java/com/autobots/automanager/controles/EnderecoControle.java
package com.autobots.automanager.controles;

import com.autobots.automanager.entidades.Endereco;
import com.autobots.automanager.repositorios.EnderecoRepositorio;
import com.autobots.automanager.modelo.EnderecoAtualizador;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/")
public class EnderecoControle {

    @Autowired
    private EnderecoRepositorio repositorio;

    @Autowired
    private EnderecoAtualizador atualizador;

    @GetMapping("/endereco/{id}")
    public Endereco obterEndereco(@PathVariable long id) {
        return repositorio.findById(id).orElse(null);
    }

    @GetMapping("/enderecos")
    public List<Endereco> obterEnderecos() {
        return repositorio.findAll();
    }

    @PostMapping("/endereco")
    public Endereco cadastrarEndereco(@RequestBody Endereco endereco) {
        return repositorio.save(endereco);
    }

    @PutMapping("/endereco/{id}")
    public Endereco atualizarEndereco(@RequestBody Endereco atualizacao) {
        Endereco endereco = repositorio.findById(atualizacao.getId()).orElse(null);
        if (endereco != null) {
            atualizador.atualizar(endereco, atualizacao);
            return repositorio.save(endereco);
        }
        return null;
    }

    @DeleteMapping("/endereco/{id}")
    public void excluirEndereco(@PathVariable Long id) {
        repositorio.deleteById(id);
    }
}