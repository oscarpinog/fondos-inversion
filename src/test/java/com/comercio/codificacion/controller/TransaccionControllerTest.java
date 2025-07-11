package com.comercio.codificacion.controller;

import com.comercio.codificacion.dtos.SuscripcionDto;
import com.comercio.codificacion.dtos.CancelacionDto;
import com.comercio.codificacion.dtos.TransaccionResponseDto;
import com.comercio.codificacion.services.TransaccionService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import static org.hamcrest.Matchers.hasSize;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@WebMvcTest(TransaccionController.class)
@AutoConfigureMockMvc(addFilters = false)
class TransaccionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransaccionService transaccionService;

    private ObjectMapper objectMapper;
    private SuscripcionDto suscripcionDto;
    private CancelacionDto cancelacionDto;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();

        suscripcionDto = new SuscripcionDto();
        suscripcionDto.setClienteId(1L);
        suscripcionDto.setFondoId(2L);


        cancelacionDto = new CancelacionDto();
        cancelacionDto.setClienteId(1L);
        cancelacionDto.setFondoId(2L);
    }

    @Test
    void suscribirseAFondo_DeberiaRetornar200() throws Exception {
        doNothing().when(transaccionService).suscribirseAFondo(suscripcionDto);

        mockMvc.perform(post("/api/transacciones/suscribirse")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(suscripcionDto)))
                .andExpect(status().isOk())
                .andExpect(content().string("Suscripción realizada con éxito"));
    }

    @Test
    void suscribirseAFondo_DeberiaRetornar400CuandoFalla() throws Exception {
        doThrow(new RuntimeException("No tiene saldo disponible")).when(transaccionService).suscribirseAFondo(suscripcionDto);

        mockMvc.perform(post("/api/transacciones/suscribirse")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(suscripcionDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("No tiene saldo disponible"));
    }

    @Test
    void cancelarSuscripcion_DeberiaRetornar200() throws Exception {
        doNothing().when(transaccionService).cancelarSuscripcion(cancelacionDto);

        mockMvc.perform(post("/api/transacciones/cancelar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cancelacionDto)))
                .andExpect(status().isOk())
                .andExpect(content().string("Cancelación realizada con éxito"));
    }

    @Test
    void cancelarSuscripcion_DeberiaRetornar400CuandoFalla() throws Exception {
        doThrow(new RuntimeException("No se encontró suscripción")).when(transaccionService).cancelarSuscripcion(cancelacionDto);

        mockMvc.perform(post("/api/transacciones/cancelar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cancelacionDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("No se encontró suscripción"));
    }

    @Test
    void obtenerHistorial_DeberiaRetornar200YListaTransacciones() throws Exception {
        TransaccionResponseDto t1 = new TransaccionResponseDto();
        t1.setTransaccionId(1L);
        t1.setFondoId(1L);
        t1.setFondoNombre("Fondo1");
        t1.setTipo("Vinculación");
        t1.setFecha(LocalDateTime.of(2025, 7, 11, 10, 0));
        t1.setMonto(200000.0);

        TransaccionResponseDto t2 = new TransaccionResponseDto();
        t2.setTransaccionId(2L);
        t2.setFondoId(2L);
        t2.setFondoNombre("Fondo2");
        t2.setTipo("Cancelación");
        t2.setFecha(LocalDateTime.of(2025, 7, 11, 10, 30));
        t2.setMonto(300000.0);

        List<TransaccionResponseDto> historial = Arrays.asList(t1, t2);

        when(transaccionService.obtenerHistorialPorCliente(1L)).thenReturn(historial);

        mockMvc.perform(get("/api/transacciones/historial/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].transaccionId", is(1)))
                .andExpect(jsonPath("$[0].tipo", is("Vinculación")))
                .andExpect(jsonPath("$[0].monto", is(200000.0)))
                .andExpect(jsonPath("$[1].transaccionId", is(2)))
                .andExpect(jsonPath("$[1].tipo", is("Cancelación")))
                .andExpect(jsonPath("$[1].monto", is(300000.0)));
    }

}
