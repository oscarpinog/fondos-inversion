package com.comercio.codificacion.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.comercio.codificacion.dtos.ClienteDto;
import com.comercio.codificacion.services.ClienteService;
import com.fasterxml.jackson.databind.ObjectMapper;
@ActiveProfiles("test")
@WebMvcTest(ClienteController.class)
@AutoConfigureMockMvc(addFilters = false)
class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClienteService clienteService;

    private ClienteDto clienteDto;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();

        clienteDto = new ClienteDto();
        clienteDto.setId(1L);
        clienteDto.setNombre("Juan");
        clienteDto.setSaldoDisponible(500000.0);
    }


    @Test
    void obtenerCliente_DeberiaRetornar404() throws Exception {
        Mockito.when(clienteService.obtenerClientePorId(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/clientes/1"))
                .andExpect(status().isNotFound());
    }
    
    @Test
    void registrarCliente_DeberiaRetornar201() throws Exception {
        // Arrange
        Mockito.doNothing().when(clienteService).crearCliente(Mockito.any(ClienteDto.class));

        // Act & Assert
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/api/clientes")
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(clienteDto)))
                .andExpect(status().isCreated());
    }
}
