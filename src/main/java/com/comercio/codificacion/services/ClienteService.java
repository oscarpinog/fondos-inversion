package com.comercio.codificacion.services;

import com.comercio.codificacion.dtos.ClienteDto;

import java.util.Optional;

public interface ClienteService {
    Optional<ClienteDto> obtenerClientePorId(Long id);
    void crearCliente(ClienteDto clienteDto);
    void actualizarCliente(ClienteDto clienteDto);
}
