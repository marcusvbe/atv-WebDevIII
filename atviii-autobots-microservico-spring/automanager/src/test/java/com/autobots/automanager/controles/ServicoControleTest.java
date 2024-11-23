// src/test/java/com/autobots/automanager/controles/ServicoControleTest.java
package com.autobots.automanager.controles;

import com.autobots.automanager.DTOs.ServicoDTO;
import com.autobots.automanager.entitades.Servico;
import com.autobots.automanager.repositorios.RepositorioServico;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ServicoControleTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RepositorioServico repositorioServico;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testObterServicos() throws Exception {
        Servico servico = new Servico();
        servico.setId(1L);
        servico.setNome("Servico Teste");
        servico.setValor(100.0);
        servico.setDescricao("Descricao Teste");

        when(repositorioServico.findAll()).thenReturn(Collections.singletonList(servico));

        mockMvc.perform(get("/servicos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.servicoDTOList[0].id").value(servico.getId()))
                .andExpect(jsonPath("$._embedded.servicoDTOList[0].nome").value(servico.getNome()));
    }

    @Test
    public void testObterServico() throws Exception {
        Servico servico = new Servico();
        servico.setId(1L);
        servico.setNome("Servico Teste");
        servico.setValor(100.0);
        servico.setDescricao("Descricao Teste");

        when(repositorioServico.findById(1L)).thenReturn(Optional.of(servico));

        mockMvc.perform(get("/servico/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(servico.getId()))
                .andExpect(jsonPath("$.nome").value(servico.getNome()));
    }

    @Test
    public void testCadastrarServico() throws Exception {
        ServicoDTO servicoDTO = new ServicoDTO();
        servicoDTO.setNome("Servico Teste");
        servicoDTO.setValor(100.0);
        servicoDTO.setDescricao("Descricao Teste");

        Servico servico = new Servico();
        servico.setId(1L);
        servico.setNome(servicoDTO.getNome());
        servico.setValor(servicoDTO.getValor());
        servico.setDescricao(servicoDTO.getDescricao());

        when(repositorioServico.save(servico)).thenReturn(servico);

        mockMvc.perform(post("/servico/cadastro")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(servicoDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(servico.getId()))
                .andExpect(jsonPath("$.nome").value(servico.getNome()));
    }

    @Test
    public void testAtualizarServico() throws Exception {
        ServicoDTO servicoDTO = new ServicoDTO();
        servicoDTO.setNome("Servico Atualizado");
        servicoDTO.setValor(150.0);
        servicoDTO.setDescricao("Descricao Atualizada");

        Servico servico = new Servico();
        servico.setId(1L);
        servico.setNome("Servico Teste");
        servico.setValor(100.0);
        servico.setDescricao("Descricao Teste");

        when(repositorioServico.findById(1L)).thenReturn(Optional.of(servico));
        when(repositorioServico.save(servico)).thenReturn(servico);

        mockMvc.perform(put("/servico/atualizar/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(servicoDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(servico.getId()))
                .andExpect(jsonPath("$.nome").value(servicoDTO.getNome()));
    }

    @Test
    public void testExcluirServico() throws Exception {
        Servico servico = new Servico();
        servico.setId(1L);
        servico.setNome("Servico Teste");
        servico.setValor(100.0);
        servico.setDescricao("Descricao Teste");

        when(repositorioServico.findById(1L)).thenReturn(Optional.of(servico));

        mockMvc.perform(delete("/servico/excluir/1"))
                .andExpect(status().isNoContent());
    }
}
