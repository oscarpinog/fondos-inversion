package com.comercio.codificacion.services.impl;

import com.comercio.codificacion.dtos.FondoDto;
import com.comercio.codificacion.repositories.FondoRepository;
import com.comercio.codificacion.services.FondoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementación del servicio de Fondos.
 * Gestiona el catálogo de productos de inversión disponibles (FPV y FIC)[cite: 21].
 */
@Slf4j
@Service
public class FondoServiceImpl implements FondoService {

    private final FondoRepository fondoRepository;

    public FondoServiceImpl(FondoRepository fondoRepository) {
        this.fondoRepository = fondoRepository;
    }

    /**
     * Recupera todos los fondos configurados en el sistema.
     * @return Lista de fondos con sus montos mínimos y categorías[cite: 21].
     */
    @Override
    public List<FondoDto> obtenerTodos() {
        log.info("Consultando catálogo completo de fondos en la base de datos");
        List<FondoDto> fondos = fondoRepository.findAll().stream().map(entity -> {
            FondoDto dto = new FondoDto();
            BeanUtils.copyProperties(entity, dto);
            return dto;
        }).collect(Collectors.toList());
        
        log.info("Catálogo recuperado: {} fondos encontrados", fondos.size());
        return fondos;
    }

    /**
     * Busca un fondo específico por su identificador único.
     * @param id Identificador del fondo.
     * @return Optional con el DTO del fondo si existe.
     */
    @Override
    public Optional<FondoDto> obtenerPorId(Long id) {
        log.info("Buscando fondo con ID: {}", id);
        return fondoRepository.findById(id).map(entity -> {
            FondoDto dto = new FondoDto();
            BeanUtils.copyProperties(entity, dto);
            log.debug("Fondo encontrado: {}", dto.getNombre());
            return dto;
        });
    }
}