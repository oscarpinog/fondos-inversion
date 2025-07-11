package com.comercio.codificacion.services;

import com.comercio.codificacion.dtos.FondoDto;

import java.util.List;
import java.util.Optional;

public interface FondoService {
   
	List<FondoDto> obtenerTodos();
    
    Optional<FondoDto> obtenerPorId(Long id);
}
