package com.comercio.codificacion.repositories;

import com.comercio.codificacion.entities.ClienteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<ClienteEntity, Long> {

}
