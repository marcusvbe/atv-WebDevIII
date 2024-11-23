package com.autobots.automanager.controles;

import com.autobots.automanager.entidades.Documento;
import com.autobots.automanager.modelo.DocumentoAtualizador;
import com.autobots.automanager.repositorios.DocumentoRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/")
public class DocumentoControle {

    @Autowired
    private DocumentoRepositorio repositorio;

    @GetMapping("/documento/{id}")
    public Documento obterDocumento(@PathVariable Long id) {
        return repositorio.findById(id).orElse(null);
    }

    @GetMapping("/documentos")
    public List<Documento> obterDocumentos() {
        return repositorio.findAll();
    }

    @PostMapping("/documento")
    public Documento cadastrarDocumento(@RequestBody Documento documento) {
        return repositorio.save(documento);
    }

    @PutMapping("/documento/{id}")
    public Documento atualizarDocumento(@RequestBody Documento atualizacao) {
        Documento documento = repositorio.findById(atualizacao.getId()).orElse(null);
        if (documento != null) {
            DocumentoAtualizador atualizador = new DocumentoAtualizador();
            atualizador.atualizar(documento, atualizacao);
            return repositorio.save(documento);
        }
        return null;
    }

    @DeleteMapping("/documento/{id}")
    public void excluirDocumento(@PathVariable Long id) {
        repositorio.deleteById(id);
    }
}