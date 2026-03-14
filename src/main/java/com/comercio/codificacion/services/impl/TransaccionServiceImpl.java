package com.comercio.codificacion.services.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import com.comercio.codificacion.dtos.CancelacionDto;
import com.comercio.codificacion.dtos.SuscripcionDto;
import com.comercio.codificacion.dtos.TipoTransaccion;
import com.comercio.codificacion.dtos.TransaccionResponseDto;
import com.comercio.codificacion.entities.ClienteEntity;
import com.comercio.codificacion.entities.FondoEntity;
import com.comercio.codificacion.entities.TransaccionEntity;
import com.comercio.codificacion.exceptions.NotFoundResponse;
import com.comercio.codificacion.repositories.ClienteRepository;
import com.comercio.codificacion.repositories.FondoRepository;
import com.comercio.codificacion.repositories.TransaccionRepository;
import com.comercio.codificacion.services.TransaccionService;

import jakarta.transaction.Transactional;

/**
 * Implementación de la lógica de transacciones para fondos de inversión.
 * Gestiona aperturas, cancelaciones y validaciones de saldo según reglas de negocio.
 */
@Slf4j
@Service
public class TransaccionServiceImpl implements TransaccionService {

    private final TransaccionRepository transaccionRepository;
    private final ClienteRepository clienteRepository;
    private final FondoRepository fondoRepository;

    public TransaccionServiceImpl(TransaccionRepository transaccionRepository,
                                  ClienteRepository clienteRepository,
                                  FondoRepository fondoRepository) {
        this.transaccionRepository = transaccionRepository;
        this.clienteRepository = clienteRepository;
        this.fondoRepository = fondoRepository;
    }

    /**
     * Realiza la suscripción de un cliente a un fondo verificando saldo disponible.
     * @param dto Información de la suscripción solicitada.
     */
    @Transactional
    @Override
    public void suscribirseAFondo(SuscripcionDto dto) {
        log.info("Procesando solicitud de suscripción: Cliente {} -> Fondo {}", dto.getClienteId(), dto.getFondoId());

        ClienteEntity cliente = clienteRepository.findById(dto.getClienteId())
                .orElseThrow(() -> new NotFoundResponse("Cliente no encontrado"));

        FondoEntity fondo = fondoRepository.findById(dto.getFondoId())
                .orElseThrow(() -> new NotFoundResponse("Fondo no encontrado"));

        // Regla de negocio: Validación de saldo mínimo de vinculación 
        if (cliente.getSaldoDisponible() < fondo.getMontoMinimo()) {
            log.warn("Saldo insuficiente: Cliente {} no puede vincularse al fondo {}", cliente.getId(), fondo.getNombre());
            throw new NotFoundResponse("No tiene saldo disponible para vincularse al fondo " + fondo.getNombre());
        }

        // Registrar la transacción de apertura con ID único [cite: 15]
        TransaccionEntity transaccion = crearEntidadTransaccion(cliente, fondo, TipoTransaccion.APERTURA, fondo.getMontoMinimo());

        // Actualizar saldo del cliente
        cliente.setSaldoDisponible(cliente.getSaldoDisponible() - fondo.getMontoMinimo());

        clienteRepository.save(cliente);
        transaccionRepository.save(transaccion);
        
        log.info("Suscripción exitosa: Transacción {} registrada", transaccion.getId());
    }

    /**
     * Cancela una suscripción activa y retorna el valor de vinculación al saldo del cliente.
     * @param dto Información para la cancelación.
     */
    @Transactional
    @Override
    public void cancelarSuscripcion(CancelacionDto dto) {
        log.info("Procesando cancelación de suscripción: Cliente {} -> Fondo {}", dto.getClienteId(), dto.getFondoId());

        ClienteEntity cliente = clienteRepository.findById(dto.getClienteId())
                .orElseThrow(() -> new NotFoundResponse("Cliente no encontrado"));

        FondoEntity fondo = fondoRepository.findById(dto.getFondoId())
                .orElseThrow(() -> new NotFoundResponse("Fondo no encontrado"));

        // Validar existencia de suscripción previa
        boolean tieneSuscripcion = transaccionRepository.findByCliente(cliente).stream()
                .anyMatch(tx -> tx.getFondo().getId().equals(fondo.getId()) && tx.getTipo() == TipoTransaccion.APERTURA);

        if (!tieneSuscripcion) {
            log.warn("Intento de cancelación fallido: Cliente {} no tiene suscripción activa en fondo {}", cliente.getId(), fondo.getId());
            throw new NotFoundResponse("No existe una suscripción activa a ese fondo");
        }

        // Regla de negocio: Retorno de valor de vinculación al cliente 
        TransaccionEntity cancelacion = crearEntidadTransaccion(cliente, fondo, TipoTransaccion.CANCELACION, fondo.getMontoMinimo());

        cliente.setSaldoDisponible(cliente.getSaldoDisponible() + fondo.getMontoMinimo());

        clienteRepository.save(cliente);
        transaccionRepository.save(cancelacion);
        
        log.info("Cancelación exitosa: Monto retornado al cliente {}", cliente.getId());
    }

    /**
     * Obtiene el historial completo de transacciones para un cliente.
     * @param clienteId ID del cliente a consultar.
     * @return Lista de transacciones realizadas.
     */
    @Override
    public List<TransaccionResponseDto> obtenerHistorialPorCliente(Long clienteId) {
        log.info("Recuperando historial de transacciones para el cliente: {}", clienteId);
        
        ClienteEntity cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new NotFoundResponse("Cliente no encontrado"));

        return transaccionRepository.findByCliente(cliente).stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    // Métodos privados para mejorar legibilidad

    private TransaccionEntity crearEntidadTransaccion(ClienteEntity cliente, FondoEntity fondo, TipoTransaccion tipo, Double monto) {
        TransaccionEntity tx = new TransaccionEntity();
        tx.setCliente(cliente);
        tx.setFondo(fondo);
        tx.setTipo(tipo);
        tx.setFecha(LocalDateTime.now());
        tx.setMonto(monto);
        return tx;
    }

    private TransaccionResponseDto mapToResponseDto(TransaccionEntity tx) {
        TransaccionResponseDto dto = new TransaccionResponseDto();
        dto.setTransaccionId(tx.getId());
        dto.setFondoId(tx.getFondo().getId());
        dto.setFondoNombre(tx.getFondo().getNombre());
        dto.setTipo(tx.getTipo().name());
        dto.setFecha(tx.getFecha());
        dto.setMonto(tx.getMonto());
        return dto;
    }
}