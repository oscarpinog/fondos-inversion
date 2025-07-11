package com.comercio.codificacion.services.impl;

import com.comercio.codificacion.dtos.CancelacionDto;
import com.comercio.codificacion.dtos.SuscripcionDto;
import com.comercio.codificacion.dtos.TransaccionResponseDto;
import com.comercio.codificacion.entities.ClienteEntity;
import com.comercio.codificacion.entities.FondoEntity;
import com.comercio.codificacion.entities.TransaccionEntity;
import com.comercio.codificacion.repositories.ClienteRepository;
import com.comercio.codificacion.repositories.FondoRepository;
import com.comercio.codificacion.repositories.TransaccionRepository;
import com.comercio.codificacion.dtos.TipoTransaccion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransaccionServiceImplTest {

    private TransaccionRepository transaccionRepository;
    private ClienteRepository clienteRepository;
    private FondoRepository fondoRepository;
    private TransaccionServiceImpl transaccionService;

    @BeforeEach
    void setUp() {
        transaccionRepository = mock(TransaccionRepository.class);
        clienteRepository = mock(ClienteRepository.class);
        fondoRepository = mock(FondoRepository.class);
        transaccionService = new TransaccionServiceImpl(transaccionRepository, clienteRepository, fondoRepository);
    }

    @Test
    void suscribirseAFondo_DeberiaGuardarTransaccionYActualizarSaldo() {
        SuscripcionDto dto = new SuscripcionDto();
		dto.setClienteId(1L);
		dto.setFondoId(1L);
        ClienteEntity cliente = new ClienteEntity();
        cliente.setId(1L);
        cliente.setSaldoDisponible(1_000_000.0);

        FondoEntity fondo = new FondoEntity();
        fondo.setId(1L);
        fondo.setMontoMinimo(500_000.0);
        fondo.setNombre("Fondo X");

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(fondoRepository.findById(1L)).thenReturn(Optional.of(fondo));

        transaccionService.suscribirseAFondo(dto);

        assertEquals(500_000.0, cliente.getSaldoDisponible());
        verify(transaccionRepository, times(1)).save(any(TransaccionEntity.class));
    }

    @Test
    void suscribirseAFondo_DeberiaLanzarExcepcionPorSaldoInsuficiente() {
        SuscripcionDto dto = new SuscripcionDto();
		dto.setClienteId(1L);
		dto.setFondoId(1L);
        ClienteEntity cliente = new ClienteEntity();
        cliente.setSaldoDisponible(100_000.0);

        FondoEntity fondo = new FondoEntity();
        fondo.setMontoMinimo(500_000.0);
        fondo.setNombre("Fondo Y");

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(fondoRepository.findById(1L)).thenReturn(Optional.of(fondo));

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                transaccionService.suscribirseAFondo(dto)
        );
        assertEquals("No tiene saldo disponible para vincularse al fondo Fondo Y", ex.getMessage());
    }

    @Test
    void cancelarSuscripcion_DeberiaGuardarTransaccionYDevolverSaldo() {
        CancelacionDto dto = new CancelacionDto();
		dto.setClienteId(1L);
		dto.setFondoId(1L);
        ClienteEntity cliente = new ClienteEntity();
        cliente.setSaldoDisponible(500_000.0);

        FondoEntity fondo = new FondoEntity();
        fondo.setId(1L);
        fondo.setMontoMinimo(500_000.0);
        fondo.setNombre("Fondo A");

        TransaccionEntity apertura = new TransaccionEntity();
        apertura.setFondo(fondo);
        apertura.setTipo(TipoTransaccion.APERTURA);

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(fondoRepository.findById(1L)).thenReturn(Optional.of(fondo));
        when(transaccionRepository.findByCliente(cliente)).thenReturn(List.of(apertura));

        transaccionService.cancelarSuscripcion(dto);

        assertEquals(1_000_000.0, cliente.getSaldoDisponible());
        verify(transaccionRepository, times(1)).save(any(TransaccionEntity.class));
    }

    @Test
    void cancelarSuscripcion_DeberiaLanzarExcepcionSiNoExisteSuscripcion() {
        CancelacionDto dto = new CancelacionDto();
		dto.setClienteId(1L);
		dto.setFondoId(1L);

        ClienteEntity cliente = new ClienteEntity();
        FondoEntity fondo = new FondoEntity();
        fondo.setId(1L);

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(fondoRepository.findById(1L)).thenReturn(Optional.of(fondo));
        when(transaccionRepository.findByCliente(cliente)).thenReturn(Collections.emptyList());

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                transaccionService.cancelarSuscripcion(dto)
        );
        assertEquals("No existe una suscripción activa a ese fondo", ex.getMessage());
    }

    @Test
    void obtenerHistorialPorCliente_DeberiaRetornarListaDto() {
        ClienteEntity cliente = new ClienteEntity();
        cliente.setId(1L);

        FondoEntity fondo = new FondoEntity();
        fondo.setId(1L);
        fondo.setNombre("Fondo Z");

        TransaccionEntity tx = new TransaccionEntity();
        tx.setId(10L);
        tx.setCliente(cliente);
        tx.setFondo(fondo);
        tx.setTipo(TipoTransaccion.APERTURA);
        tx.setFecha(LocalDateTime.of(2025, 7, 11, 10, 0));
        tx.setMonto(500_000.0);

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(transaccionRepository.findByCliente(cliente)).thenReturn(List.of(tx));

        List<TransaccionResponseDto> historial = transaccionService.obtenerHistorialPorCliente(1L);

        assertEquals(1, historial.size());
        assertEquals(10L, historial.get(0).getTransaccionId());
        assertEquals("Fondo Z", historial.get(0).getFondoNombre());
    }
}
