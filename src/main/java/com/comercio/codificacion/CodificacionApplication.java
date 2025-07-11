package com.comercio.codificacion;

import javax.sql.DataSource;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.comercio.codificacion.dtos.PreferenciaNotificacion;
import com.comercio.codificacion.entities.ClienteEntity;
import com.comercio.codificacion.entities.FondoEntity;
import com.comercio.codificacion.entities.UsuarioEntity;
import com.comercio.codificacion.repositories.ClienteRepository;
import com.comercio.codificacion.repositories.FondoRepository;
import com.comercio.codificacion.security.UsuarioRepository;


@SpringBootApplication
public class CodificacionApplication {

	public static void main(String[] args) {
		SpringApplication.run(CodificacionApplication.class, args);
	}
   

}
