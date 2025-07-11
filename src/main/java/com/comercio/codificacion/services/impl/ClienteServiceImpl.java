package com.comercio.codificacion.services.impl;

import com.comercio.codificacion.dtos.ClienteDto;
import com.comercio.codificacion.dtos.PreferenciaNotificacion;
import com.comercio.codificacion.entities.ClienteEntity;

import com.comercio.codificacion.repositories.ClienteRepository;
import com.comercio.codificacion.services.ClienteService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClienteServiceImpl implements ClienteService {

	private final ClienteRepository clienteRepository;

	private static final double SALDO_INICIAL = 500_000.0;

	public ClienteServiceImpl(ClienteRepository clienteRepository) {
		this.clienteRepository = clienteRepository;
	}

	@Override
	public Optional<ClienteDto> obtenerClientePorId(Long id) {
		return clienteRepository.findById(id).map(entity -> {
			ClienteDto dto = new ClienteDto();
			BeanUtils.copyProperties(entity, dto);
			return dto;
		});
	}

	@Override
	public void crearCliente(ClienteDto clienteDto) {
		ClienteEntity entity = new ClienteEntity();
		BeanUtils.copyProperties(clienteDto, entity);

		// Aplicar reglas de negocio
		entity.setSaldoDisponible(SALDO_INICIAL);
		if (entity.getPreferenciaNotificacion() == null) {
			entity.setPreferenciaNotificacion(PreferenciaNotificacion.EMAIL);
		}

		clienteRepository.save(entity);
	}

	@Override
	public void actualizarCliente(ClienteDto clienteDto) {
		ClienteEntity entity = new ClienteEntity();
		BeanUtils.copyProperties(clienteDto, entity);
		clienteRepository.save(entity);
	}
}
