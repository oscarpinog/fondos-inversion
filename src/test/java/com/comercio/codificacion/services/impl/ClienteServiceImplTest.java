package com.comercio.codificacion.services.impl;

import com.comercio.codificacion.dtos.ClienteDto;
import com.comercio.codificacion.dtos.PreferenciaNotificacion;
import com.comercio.codificacion.entities.ClienteEntity;
import com.comercio.codificacion.repositories.ClienteRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClienteServiceImplTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteServiceImpl clienteService;

    private ClienteDto clienteDto;
    private ClienteEntity clienteEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        clienteDto = new ClienteDto();
        clienteDto.setId(1L);
        clienteDto.setNombre("Juan");

        clienteEntity = new ClienteEntity();
        clienteEntity.setId(1L);
        clienteEntity.setNombre("Juan");
        clienteEntity.setSaldoDisponible(500000.0);
        clienteEntity.setPreferenciaNotificacion(PreferenciaNotificacion.EMAIL);
    }

    @Test
    void obtenerClientePorId_DeberiaRetornarClienteDto() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(clienteEntity));

        Optional<ClienteDto> resultado = clienteService.obtenerClientePorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals(clienteEntity.getNombre(), resultado.get().getNombre());
        assertEquals(clienteEntity.getSaldoDisponible(), resultado.get().getSaldoDisponible());
    }

    @Test
    void crearCliente_DeberiaAsignarSaldoYPreferenciaYGuardar() {
        clienteDto.setPreferenciaNotificacion(null); // Para validar la asignación por defecto

        clienteService.crearCliente(clienteDto);

        ArgumentCaptor<ClienteEntity> captor = ArgumentCaptor.forClass(ClienteEntity.class);
        verify(clienteRepository).save(captor.capture());

        ClienteEntity guardado = captor.getValue();
        assertEquals(500000.0, guardado.getSaldoDisponible());
        assertEquals(PreferenciaNotificacion.EMAIL, guardado.getPreferenciaNotificacion());
        assertEquals(clienteDto.getNombre(), guardado.getNombre());
    }

    @Test
    void actualizarCliente_DeberiaLlamarSaveConEntidad() {
        clienteService.actualizarCliente(clienteDto);

        ArgumentCaptor<ClienteEntity> captor = ArgumentCaptor.forClass(ClienteEntity.class);
        verify(clienteRepository).save(captor.capture());

        ClienteEntity actualizado = captor.getValue();
        assertEquals(clienteDto.getId(), actualizado.getId());
        assertEquals(clienteDto.getNombre(), actualizado.getNombre());
    }
}
