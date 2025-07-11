package com.comercio.codificacion.repositories;

import com.comercio.codificacion.entities.TransaccionEntity;
import com.comercio.codificacion.entities.ClienteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface TransaccionRepository extends JpaRepository<TransaccionEntity, Long> {
    List<TransaccionEntity> findByCliente(ClienteEntity cliente);
}