package com.comercio.codificacion.services;

import com.comercio.codificacion.dtos.SuscripcionDto;
import com.comercio.codificacion.dtos.CancelacionDto;
import com.comercio.codificacion.dtos.TransaccionResponseDto;

import java.util.List;

public interface TransaccionService {
   
	void suscribirseAFondo(SuscripcionDto dto);
   
    void cancelarSuscripcion(CancelacionDto dto);
   
    List<TransaccionResponseDto> obtenerHistorialPorCliente(Long clienteId);
}
