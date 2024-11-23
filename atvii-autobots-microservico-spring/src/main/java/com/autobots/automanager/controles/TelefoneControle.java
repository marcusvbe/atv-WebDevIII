package com.autobots.automanager.controles;

import com.autobots.automanager.entidades.Telefone;
import com.autobots.automanager.repositorios.TelefoneRepositorio;
import com.autobots.automanager.modelo.TelefoneAtualizador;
import com.autobots.automanager.modelo.AdicionadorLinkTelefone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/")
public class TelefoneControle {

    @Autowired
    private TelefoneRepositorio TelefoneRepositorio;

    @Autowired
    private TelefoneAtualizador atualizador;

    @Autowired
    private AdicionadorLinkTelefone adicionadorLink;

    @GetMapping("/telefone/{id}")
    public ResponseEntity<Telefone> obterTelefone(@PathVariable long id) {
        Telefone telefone = TelefoneRepositorio.findById(id).orElse(null);
        if (telefone == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            adicionadorLink.adicionarLink(telefone);
            return new ResponseEntity<>(telefone, HttpStatus.FOUND);
        }
    }

    @GetMapping("/telefones")
    public ResponseEntity<List<Telefone>> obterTelefones() {
        List<Telefone> telefones = TelefoneRepositorio.findAll();
        if (telefones.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            adicionadorLink.adicionarLink(telefones);
            return new ResponseEntity<>(telefones, HttpStatus.FOUND);
        }
    }

    @PostMapping("/telefone/cadastro")
    public Telefone cadastrarTelefone(@RequestBody Telefone telefone) {
        return TelefoneRepositorio.save(telefone);
    }

    @PutMapping("/telefone/atualizar/{id}")
    public Telefone atualizarTelefone(@RequestBody Telefone atualizacao) {
        Telefone telefone = TelefoneRepositorio.findById(atualizacao.getId()).orElse(null);
        if (telefone != null) {
            atualizador.atualizar(telefone, atualizacao);
            return TelefoneRepositorio.save(telefone);
        }
        return null;
    }

    @DeleteMapping("/telefone/excluir/{id}")
    public void excluirTelefone(@PathVariable Long id) {
        TelefoneRepositorio.deleteById(id);
    }
}