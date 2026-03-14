package com.comercio.codificacion.controller;

import com.comercio.codificacion.dtos.SuscripcionDto;
import com.comercio.codificacion.dtos.CancelacionDto;
import com.comercio.codificacion.dtos.TransaccionResponseDto;
import com.comercio.codificacion.services.TransaccionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador para la gestión de transacciones de fondos (Aperturas y Cancelaciones).
 * Proporciona el historial de movimientos y la gestión de suscripciones[cite: 11].
 */
@Slf4j
@RestController
@RequestMapping("/api/transacciones")
public class TransaccionController {

    private final TransaccionService transaccionService;

    public TransaccionController(TransaccionService transaccionService) {
        this.transaccionService = transaccionService;
    }

    /**
     * Procesa la suscripción de un cliente a un nuevo fondo[cite: 9].
     * @param dto Datos de la suscripción y preferencia de notificación[cite: 12].
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/suscribirse")
    @ResponseStatus(HttpStatus.CREATED)
    public void suscribirseAFondo(@RequestBody SuscripcionDto dto) {
        log.info("Iniciando suscripción para el cliente {} al fondo {}", dto.getClienteId(), dto.getFondoId());
        transaccionService.suscribirseAFondo(dto);
        log.info("Suscripción exitosa para el cliente {}", dto.getClienteId());
    }

    /**
     * Procesa la cancelación de una suscripción activa[cite: 10].
     * @param dto Datos para identificar la suscripción a cancelar.
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/cancelar")
    @ResponseStatus(HttpStatus.OK)
    public void cancelarSuscripcion(@RequestBody CancelacionDto dto) {
        log.info("Iniciando cancelación de suscripción para el cliente {}", dto.getClienteId());
        transaccionService.cancelarSuscripcion(dto);
        log.info("Cancelación completada para el cliente {}", dto.getClienteId());
    }

    /**
     * Recupera el historial de todas las transacciones de un cliente[cite: 11].
     * @param clienteId Identificador único del cliente.
     * @return Lista de aperturas y cancelaciones realizadas[cite: 11].
     */
    @GetMapping("/historial/{clienteId}")
    public List<TransaccionResponseDto> obtenerHistorial(@PathVariable Long clienteId) {
        log.info("Consultando historial de transacciones para el cliente ID: {}", clienteId);
        List<TransaccionResponseDto> historial = transaccionService.obtenerHistorialPorCliente(clienteId);
        log.info("Se recuperaron {} registros para el cliente {}", historial.size(), clienteId);
        return historial;
    }
}