package com.comercio.codificacion.controller;

import com.comercio.codificacion.dtos.ClienteDto;
import com.comercio.codificacion.services.ClienteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
    void registrarCliente_DeberiaRetornar200() throws Exception {
        doNothing().when(clienteService).crearCliente(any(ClienteDto.class));

        mockMvc.perform(post("/api/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(clienteDto)))
                .andExpect(status().isOk())
                .andExpect(content().string("Cliente creado exitosamente con saldo inicial"));
    }

    @Test
    void registrarCliente_DeberiaRetornar400CuandoFalla() throws Exception {
        doThrow(new RuntimeException("Error al crear cliente")).when(clienteService).crearCliente(any(ClienteDto.class));

        mockMvc.perform(post("/api/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(clienteDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Error al crear cliente"));
    }


    @Test
    void obtenerCliente_DeberiaRetornar404() throws Exception {
        Mockito.when(clienteService.obtenerClientePorId(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/clientes/1"))
                .andExpect(status().isNotFound());
    }
}
