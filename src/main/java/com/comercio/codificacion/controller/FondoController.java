package com.comercio.codificacion.controller;

import com.comercio.codificacion.dtos.FondoDto;
import com.comercio.codificacion.services.FondoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la consulta de fondos de inversión disponibles.
 * Proporciona la información necesaria para que el cliente gestione sus fondos.
 */
@Slf4j
@RestController
@RequestMapping("/api/fondos")
public class FondoController {

    private final FondoService fondoService;

    public FondoController(FondoService fondoService) {
        this.fondoService = fondoService;
    }

    /**
     * Recupera el catálogo completo de fondos de inversión.
     * Incluye nombre, monto mínimo y categoría.
     * * @return Lista de fondos disponibles en el sistema.
     */
    @GetMapping
    public List<FondoDto> listarFondos() {
        log.info("Iniciando consulta del catálogo de fondos");
        List<FondoDto> fondos = fondoService.obtenerTodos();
        log.info("Consulta exitosa: Se encontraron {} fondos disponibles", fondos.size());
        return fondos;
    }
}