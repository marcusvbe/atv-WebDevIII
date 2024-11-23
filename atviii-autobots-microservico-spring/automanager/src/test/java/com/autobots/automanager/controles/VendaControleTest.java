package com.autobots.automanager.controles;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.autobots.automanager.DTOs.VendaDTO;
import com.autobots.automanager.controles.VendaControle;
import com.autobots.automanager.entitades.Venda;
import com.autobots.automanager.repositorios.RepositorioVenda;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(VendaControle.class)
public class VendaControleTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RepositorioVenda repositorioVenda;

    @MockBean
    private ModelMapper modelMapper;

    @Test
    public void testCadastrarVenda() throws Exception {
        VendaDTO vendaDTO = new VendaDTO();
        vendaDTO.setIdentificacao("Venda Teste");

        Venda venda = new Venda();
        venda.setId(1L);
        venda.setIdentificacao("Venda Teste");

        when(modelMapper.map(any(VendaDTO.class), eq(Venda.class))).thenReturn(venda);
        when(repositorioVenda.save(any(Venda.class))).thenReturn(venda);
        when(modelMapper.map(any(Venda.class), eq(VendaDTO.class))).thenReturn(vendaDTO);

        mockMvc.perform(post("/venda/cadastro")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"identificacao\": \"Venda Teste\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.identificacao").value("Venda Teste"));
    }
}
