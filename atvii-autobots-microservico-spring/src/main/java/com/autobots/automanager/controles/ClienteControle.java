package com.autobots.automanager.controles;

import com.autobots.automanager.entidades.Cliente;
import com.autobots.automanager.entidades.Endereco;
import com.autobots.automanager.entidades.Documento;
import com.autobots.automanager.entidades.Telefone;
import com.autobots.automanager.modelo.AdicionadorLinkCliente;
import com.autobots.automanager.modelo.ClienteAtualizador;
import com.autobots.automanager.modelo.ClienteSelecionador;
import com.autobots.automanager.repositorios.ClienteRepositorio;
import com.autobots.automanager.repositorios.DocumentoRepositorio;
import com.autobots.automanager.repositorios.EnderecoRepositorio;
import com.autobots.automanager.repositorios.TelefoneRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/")
public class ClienteControle {

	@Autowired
	private ClienteRepositorio ClienteRepositorio;

	@Autowired
	private EnderecoRepositorio enderecoRepositorio;

	@Autowired
	private DocumentoRepositorio documentoRepositorio;

	@Autowired
	private TelefoneRepositorio telefoneRepositorio;

	@Autowired
	private ClienteSelecionador selecionador;
    @Autowired
    private AdicionadorLinkCliente adicionadorLink;

	@GetMapping("/cliente/{id}")
	public ResponseEntity<Cliente> obterCliente(@PathVariable long id) {
		List<Cliente> clientes = ClienteRepositorio.findAll();
		Cliente cliente = selecionador.selecionar(clientes, id);
		if (cliente == null) {
			ResponseEntity<Cliente> resposta = new ResponseEntity<>(HttpStatus.NOT_FOUND);
			return resposta;
		} else {
			adicionadorLink.adicionarLink(cliente);
			ResponseEntity<Cliente> resposta = new ResponseEntity<Cliente>(cliente, HttpStatus.FOUND);
			return resposta;
		}
	}

	@GetMapping("/clientes")
	public ResponseEntity<List<Cliente>> obterClientes() {
		List<Cliente> clientes = ClienteRepositorio.findAll();
		if (clientes.isEmpty()) {
			ResponseEntity<List<Cliente>> resposta = new ResponseEntity<>(HttpStatus.NOT_FOUND);
			return resposta;
		} else {
			adicionadorLink.adicionarLink(clientes);
			ResponseEntity<List<Cliente>> resposta = new ResponseEntity<>(clientes, HttpStatus.FOUND);
			return resposta;
		}
	}

	@PostMapping("/cliente/cadastro")
	public Cliente cadastrarCliente(@RequestBody Cliente cliente) {
		// Handle Endereco
		if (cliente.getEndereco() != null) {
			Endereco endereco = cliente.getEndereco();
			if (endereco.getId() != null) {
				endereco = enderecoRepositorio.findById(endereco.getId()).orElse(null);
				cliente.setEndereco(endereco);
			} else {
				cliente.setEndereco(null);
			}
		}

		// Handle Documentos
		if (cliente.getDocumentos() != null) {
			List<Documento> documentos = cliente.getDocumentos();
			for (int i = 0; i < documentos.size(); i++) {
				Documento documento = documentos.get(i);
				if (documento.getId() != null) {
					Documento existingDocumento = documentoRepositorio.findById(documento.getId()).orElse(null);
					if (existingDocumento != null) {
						documentos.set(i, existingDocumento);
					}
				}
			}
		}

		// Handle Telefones
		if (cliente.getTelefones() != null) {
			List<Telefone> telefones = new ArrayList<>();
			for (Telefone telefone : cliente.getTelefones()) {
				if (telefone.getId() != null) {
					telefone = telefoneRepositorio.findById(telefone.getId()).orElse(null);
					if (telefone != null) {
						telefones.add(telefone);
					}
				}
			}
			cliente.getTelefones().clear();
			cliente.getTelefones().addAll(telefones);
		}

		return ClienteRepositorio.save(cliente);
	}

	@PutMapping("/cliente/atualizar/{id}")
	public Cliente atualizarCliente(@RequestBody Cliente atualizacao) {
		Cliente cliente = ClienteRepositorio.findById(atualizacao.getId()).orElse(null);
		if (cliente != null) {
			ClienteAtualizador atualizador = new ClienteAtualizador();
			atualizador.atualizar(cliente, atualizacao);
			return ClienteRepositorio.save(cliente);
		}
		return null;
	}

	@DeleteMapping("/cliente/excluir/{id}")
	public void excluirCliente(@PathVariable Long id) {
		ClienteRepositorio.deleteById(id);
	}
}