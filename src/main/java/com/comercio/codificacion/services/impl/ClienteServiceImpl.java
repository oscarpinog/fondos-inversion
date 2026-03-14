package com.comercio.codificacion.services.impl;

import com.comercio.codificacion.dtos.ClienteDto;
import com.comercio.codificacion.dtos.PreferenciaNotificacion;
import com.comercio.codificacion.entities.ClienteEntity;
import com.comercio.codificacion.repositories.ClienteRepository;
import com.comercio.codificacion.services.ClienteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Implementación de servicios para la gestión de clientes.
 * Garantiza el cumplimiento de las reglas de negocio iniciales de la plataforma.
 */
@Slf4j
@Service
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clienteRepository;

    /** Monto inicial obligatorio para nuevos clientes según requerimiento técnico. */
    private static final double SALDO_INICIAL = 500_000.0;

    public ClienteServiceImpl(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    /**
     * Recupera un cliente por su ID y lo transforma a DTO para la capa de presentación.
     * @param id Identificador único del cliente.
     * @return Optional con el ClienteDto si existe.
     */
    @Override
    public Optional<ClienteDto> obtenerClientePorId(Long id) {
        log.info("Buscando entidad cliente con ID: {}", id);
        return clienteRepository.findById(id).map(entity -> {
            ClienteDto dto = new ClienteDto();
            BeanUtils.copyProperties(entity, dto);
            return dto;
        });
    }

    /**
     * Crea un nuevo cliente aplicando el saldo inicial y la preferencia de notificación por defecto.
     * @param clienteDto Datos del cliente provenientes de la petición.
     */
    @Override
    public void crearCliente(ClienteDto clienteDto) {
        log.info("Iniciando persistencia de nuevo cliente: {}", clienteDto.getNombre());
        ClienteEntity entity = new ClienteEntity();
        BeanUtils.copyProperties(clienteDto, entity);

        // Aplicar reglas de negocio definidas en la prueba técnica 
        entity.setSaldoDisponible(SALDO_INICIAL);
        
        // Establecer preferencia de notificación por defecto si no se especifica 
        if (entity.getPreferenciaNotificacion() == null) {
            log.debug("Asignando EMAIL como preferencia de notificación por defecto");
            entity.setPreferenciaNotificacion(PreferenciaNotificacion.EMAIL);
        }

        clienteRepository.save(entity);
        log.info("Cliente guardado exitosamente con saldo inicial de {}", SALDO_INICIAL);
    }

    /**
     * Actualiza la información de un cliente existente.
     * @param clienteDto Datos actualizados del cliente.
     */
    @Override
    public void actualizarCliente(ClienteDto clienteDto) {
        log.info("Actualizando información para el cliente ID: {}", clienteDto.getId());
        ClienteEntity entity = new ClienteEntity();
        BeanUtils.copyProperties(clienteDto, entity);
        clienteRepository.save(entity);
        log.info("Entidad cliente ID: {} actualizada correctamente", entity.getId());
    }
}