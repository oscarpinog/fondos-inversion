package com.comercio.codificacion.controller;

import com.comercio.codificacion.dtos.SuscripcionDto;
import com.comercio.codificacion.dtos.CancelacionDto;
import com.comercio.codificacion.dtos.TransaccionResponseDto;
import com.comercio.codificacion.services.TransaccionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transacciones")
public class TransaccionController {

    private final TransaccionService transaccionService;

    public TransaccionController(TransaccionService transaccionService) {
        this.transaccionService = transaccionService;
    }

	@PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/suscribirse")
    public ResponseEntity<String> suscribirseAFondo(@RequestBody SuscripcionDto dto) {
        try {
            transaccionService.suscribirseAFondo(dto);
            return ResponseEntity.ok("Suscripción realizada con éxito");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

	@PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/cancelar")
    public ResponseEntity<String> cancelarSuscripcion(@RequestBody CancelacionDto dto) {
        try {
            transaccionService.cancelarSuscripcion(dto);
            return ResponseEntity.ok("Cancelación realizada con éxito");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/historial/{clienteId}")
    public ResponseEntity<List<TransaccionResponseDto>> obtenerHistorial(@PathVariable Long clienteId) {
        List<TransaccionResponseDto> historial = transaccionService.obtenerHistorialPorCliente(clienteId);
        return ResponseEntity.ok(historial);
    }
}
