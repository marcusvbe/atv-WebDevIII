package com.autobots.automanager.controles;

import com.autobots.automanager.entidades.Telefone;
import com.autobots.automanager.repositorios.TelefoneRepositorio;
import com.autobots.automanager.modelo.TelefoneAtualizador;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/")
public class TelefoneControle {

    @Autowired
    private TelefoneRepositorio repositorio;

    @Autowired
    private TelefoneAtualizador atualizador;

    @GetMapping("/telefone/{id}")
    public Telefone obterTelefone(@PathVariable long id) {
        return repositorio.findById(id).orElse(null);
    }

    @GetMapping("/telefones")
    public List<Telefone> obterTelefones() {
        return repositorio.findAll();
    }

    @PostMapping("/telefone")
    public Telefone cadastrarTelefone(@RequestBody Telefone telefone) {
        return repositorio.save(telefone);
    }

    @PutMapping("/telefone/{id}")
    public Telefone atualizarTelefone(@RequestBody Telefone atualizacao) {
        Telefone telefone = repositorio.findById(atualizacao.getId()).orElse(null);
        if (telefone != null) {
            atualizador.atualizar(telefone, atualizacao);
            return repositorio.save(telefone);
        }
        return null;
    }

    @DeleteMapping("/telefone/{id}")
    public void excluirTelefone(@PathVariable Long id) {
        repositorio.deleteById(id);
    }
}