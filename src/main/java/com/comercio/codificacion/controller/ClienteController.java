package com.comercio.codificacion.controller;

import com.comercio.codificacion.dtos.ClienteDto;
import com.comercio.codificacion.services.ClienteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador para la gestión de clientes. 
 * Las excepciones son manejadas globalmente para mantener el código limpio.
 */
@Slf4j
@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    /**
     * Registra un cliente con el saldo inicial de COP $500.000[cite: 14].
     * @param dto Datos del cliente.
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void registrarCliente(@RequestBody ClienteDto dto) {
        log.info("Registrando cliente: {}", dto.getNombre());
        clienteService.crearCliente(dto);
        log.info("Cliente {} registrado exitosamente", dto.getNombre());
    }

    /**
     * Obtiene la información del cliente por ID.
     * @param id Identificador único.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ClienteDto> obtenerCliente(@PathVariable Long id) {
        log.info("Consultando cliente ID: {}", id);
        return clienteService.obtenerClientePorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> {
                    log.warn("Cliente ID {} no encontrado", id);
                    return ResponseEntity.notFound().build();
                });
    }
}