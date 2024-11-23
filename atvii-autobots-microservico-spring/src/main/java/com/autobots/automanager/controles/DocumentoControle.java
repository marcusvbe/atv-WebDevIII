package com.autobots.automanager.controles;

import com.autobots.automanager.entidades.Documento;
import com.autobots.automanager.modelo.DocumentoAtualizador;
import com.autobots.automanager.repositorios.DocumentoRepositorio;
import com.autobots.automanager.modelo.AdicionadorLinkDocumento;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/")
public class DocumentoControle {

    @Autowired
    private DocumentoRepositorio DocumentoRepositorio;
    @Autowired
    private AdicionadorLinkDocumento adicionadorLink;

    @GetMapping("/documento/{id}")
    public ResponseEntity<Documento> obterDocumento(@PathVariable long id) {
        Documento documento = DocumentoRepositorio.findById(id).orElse(null);
        if (documento == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            adicionadorLink.adicionarLink(documento);
            return new ResponseEntity<>(documento, HttpStatus.FOUND);
        }
    }

    @GetMapping("/documentos")
    public ResponseEntity<List<Documento>> obterDocumentos() {
        List<Documento> documentos = DocumentoRepositorio.findAll();
        if (documentos.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            adicionadorLink.adicionarLink(documentos);
            return new ResponseEntity<>(documentos, HttpStatus.FOUND);
        }
    }

    @PostMapping("/documento/cadastro")
    public Documento cadastrarDocumento(@RequestBody Documento documento) {
        return DocumentoRepositorio.save(documento);
    }

    @PutMapping("/documento/atualizar/{id}")
    public Documento atualizarDocumento(@RequestBody Documento atualizacao) {
        Documento documento = DocumentoRepositorio.findById(atualizacao.getId()).orElse(null);
        if (documento != null) {
            DocumentoAtualizador atualizador = new DocumentoAtualizador();
            atualizador.atualizar(documento, atualizacao);
            return DocumentoRepositorio.save(documento);
        }
        return null;
    }

    @DeleteMapping("/documento/excluir/{id}")
    public void excluirDocumento(@PathVariable Long id) {
        DocumentoRepositorio.deleteById(id);
    }
}