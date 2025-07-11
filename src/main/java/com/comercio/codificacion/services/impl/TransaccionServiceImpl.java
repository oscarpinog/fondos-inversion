package com.comercio.codificacion.services.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.comercio.codificacion.dtos.CancelacionDto;
import com.comercio.codificacion.dtos.SuscripcionDto;
import com.comercio.codificacion.dtos.TipoTransaccion;
import com.comercio.codificacion.dtos.TransaccionResponseDto;
import com.comercio.codificacion.entities.ClienteEntity;
import com.comercio.codificacion.entities.FondoEntity;
import com.comercio.codificacion.entities.TransaccionEntity;
import com.comercio.codificacion.repositories.ClienteRepository;
import com.comercio.codificacion.repositories.FondoRepository;
import com.comercio.codificacion.repositories.TransaccionRepository;
import com.comercio.codificacion.services.TransaccionService;

import jakarta.transaction.Transactional;

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

    @Transactional
    @Override
    public void suscribirseAFondo(SuscripcionDto dto) {
        ClienteEntity cliente = clienteRepository.findById(dto.getClienteId())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        FondoEntity fondo = fondoRepository.findById(dto.getFondoId())
                .orElseThrow(() -> new RuntimeException("Fondo no encontrado"));

        if (cliente.getSaldoDisponible() < fondo.getMontoMinimo()) {
            throw new RuntimeException("No tiene saldo disponible para vincularse al fondo " + fondo.getNombre());
        }

        TransaccionEntity transaccion = new TransaccionEntity();
        transaccion.setCliente(cliente);
        transaccion.setFondo(fondo);
        transaccion.setTipo(TipoTransaccion.APERTURA);
        transaccion.setFecha(LocalDateTime.now());
        transaccion.setMonto(fondo.getMontoMinimo());

        cliente.setSaldoDisponible(cliente.getSaldoDisponible() - fondo.getMontoMinimo());

        clienteRepository.save(cliente);
        transaccionRepository.save(transaccion);
    }

    @Transactional
    @Override
    public void cancelarSuscripcion(CancelacionDto dto) {
        ClienteEntity cliente = clienteRepository.findById(dto.getClienteId())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        FondoEntity fondo = fondoRepository.findById(dto.getFondoId())
                .orElseThrow(() -> new RuntimeException("Fondo no encontrado"));

        boolean tieneSuscripcion = transaccionRepository.findByCliente(cliente).stream().anyMatch(tx ->
                tx.getFondo().getId().equals(fondo.getId()) && tx.getTipo() == TipoTransaccion.APERTURA
        );

        if (!tieneSuscripcion) {
            throw new RuntimeException("No existe una suscripción activa a ese fondo");
        }

        TransaccionEntity cancelacion = new TransaccionEntity();
        cancelacion.setCliente(cliente);
        cancelacion.setFondo(fondo);
        cancelacion.setTipo(TipoTransaccion.CANCELACION);
        cancelacion.setFecha(LocalDateTime.now());
        cancelacion.setMonto(fondo.getMontoMinimo());

        cliente.setSaldoDisponible(cliente.getSaldoDisponible() + fondo.getMontoMinimo());

        clienteRepository.save(cliente);
        transaccionRepository.save(cancelacion);
    }

    @Override
    public List<TransaccionResponseDto> obtenerHistorialPorCliente(Long clienteId) {
        ClienteEntity cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        return transaccionRepository.findByCliente(cliente).stream().map(tx -> {
            TransaccionResponseDto dto = new TransaccionResponseDto();
            dto.setTransaccionId(tx.getId());
            dto.setFondoId(tx.getFondo().getId());
            dto.setFondoNombre(tx.getFondo().getNombre());
            dto.setTipo(tx.getTipo().name());
            dto.setFecha(tx.getFecha());
            dto.setMonto(tx.getMonto());
            return dto;
        }).collect(Collectors.toList());
    }
}
