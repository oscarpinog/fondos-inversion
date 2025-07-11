package com.comercio.codificacion.services.impl;

import com.comercio.codificacion.dtos.FondoDto;
import com.comercio.codificacion.repositories.FondoRepository;
import com.comercio.codificacion.services.FondoService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FondoServiceImpl implements FondoService {

    private final FondoRepository fondoRepository;

    public FondoServiceImpl(FondoRepository fondoRepository) {
        this.fondoRepository = fondoRepository;
    }

    @Override
    public List<FondoDto> obtenerTodos() {
        return fondoRepository.findAll().stream().map(entity -> {
            FondoDto dto = new FondoDto();
            BeanUtils.copyProperties(entity, dto);
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public Optional<FondoDto> obtenerPorId(Long id) {
        return fondoRepository.findById(id).map(entity -> {
            FondoDto dto = new FondoDto();
            BeanUtils.copyProperties(entity, dto);
            return dto;
        });
    }
}
