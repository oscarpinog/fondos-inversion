package com.comercio.codificacion.controller;

import com.comercio.codificacion.dtos.FondoDto;
import com.comercio.codificacion.services.FondoService;
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

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@WebMvcTest(FondoController.class)
@AutoConfigureMockMvc(addFilters = false)
class FondoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FondoService fondoService;

    private List<FondoDto> fondos;

    @BeforeEach
    void setUp() {
        FondoDto fondo1 = new FondoDto();
        fondo1.setId(1L);
        fondo1.setNombre("Fondo A");
        fondo1.setMontoMinimo(100000.0);

        FondoDto fondo2 = new FondoDto();
        fondo2.setId(2L);
        fondo2.setNombre("Fondo B");
        fondo2.setMontoMinimo(200000.0);

        fondos = Arrays.asList(fondo1, fondo2);
    }

    @Test
    void listarFondos_DeberiaRetornar200YListaFondos() throws Exception {
        Mockito.when(fondoService.obtenerTodos()).thenReturn(fondos);

        mockMvc.perform(get("/api/fondos")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].nombre", is("Fondo A")))
                .andExpect(jsonPath("$[0].montoMinimo", is(100000.0)))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].nombre", is("Fondo B")))
                .andExpect(jsonPath("$[1].montoMinimo", is(200000.0)));
    }
}

