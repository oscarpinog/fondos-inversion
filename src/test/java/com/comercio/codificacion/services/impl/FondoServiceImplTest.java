package com.comercio.codificacion.services.impl;

import com.comercio.codificacion.dtos.FondoDto;
import com.comercio.codificacion.entities.FondoEntity;
import com.comercio.codificacion.repositories.FondoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FondoServiceImplTest {

    @Mock
    private FondoRepository fondoRepository;

    @InjectMocks
    private FondoServiceImpl fondoService;

    private FondoEntity fondo1;
    private FondoEntity fondo2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        fondo1 = new FondoEntity();
        fondo1.setId(1L);
        fondo1.setNombre("Fondo A");
        fondo1.setMontoMinimo(100000.0);

        fondo2 = new FondoEntity();
        fondo2.setId(2L);
        fondo2.setNombre("Fondo B");
        fondo2.setMontoMinimo(200000.0);
    }

    @Test
    void obtenerTodos_DeberiaRetornarListaDeFondosDto() {
        when(fondoRepository.findAll()).thenReturn(Arrays.asList(fondo1, fondo2));

        List<FondoDto> resultado = fondoService.obtenerTodos();

        assertEquals(2, resultado.size());
        assertEquals("Fondo A", resultado.get(0).getNombre());
        assertEquals(100000.0, resultado.get(0).getMontoMinimo());
        assertEquals("Fondo B", resultado.get(1).getNombre());
    }

    @Test
    void obtenerPorId_DeberiaRetornarFondoDto() {
        when(fondoRepository.findById(1L)).thenReturn(Optional.of(fondo1));

        Optional<FondoDto> resultado = fondoService.obtenerPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals("Fondo A", resultado.get().getNombre());
        assertEquals(100000.0, resultado.get().getMontoMinimo());
    }

    @Test
    void obtenerPorId_DeberiaRetornarEmptyCuandoNoExiste() {
        when(fondoRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<FondoDto> resultado = fondoService.obtenerPorId(99L);

        assertFalse(resultado.isPresent());
    }
}
