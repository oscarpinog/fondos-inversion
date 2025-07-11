package com.comercio.codificacion.controller;

import com.comercio.codificacion.dtos.FondoDto;
import com.comercio.codificacion.services.FondoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fondos")
public class FondoController {

    private final FondoService fondoService;

    public FondoController(FondoService fondoService) {
        this.fondoService = fondoService;
    }

    @GetMapping
    public ResponseEntity<List<FondoDto>> listarFondos() {
        List<FondoDto> fondos = fondoService.obtenerTodos();
        return ResponseEntity.ok(fondos);
    }
}
