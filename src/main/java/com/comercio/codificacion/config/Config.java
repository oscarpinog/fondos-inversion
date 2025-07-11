package com.comercio.codificacion.config;

import javax.sql.DataSource;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.comercio.codificacion.dtos.PreferenciaNotificacion;
import com.comercio.codificacion.entities.ClienteEntity;
import com.comercio.codificacion.entities.FondoEntity;
import com.comercio.codificacion.entities.UsuarioEntity;
import com.comercio.codificacion.repositories.ClienteRepository;
import com.comercio.codificacion.repositories.FondoRepository;
import com.comercio.codificacion.security.UsuarioRepository;

@Configuration
public class Config {

	
	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			public void addCorsMapping(CorsRegistry registry) {
				registry.addMapping("/**").allowedOrigins("/**","*","/*")
				.allowedMethods("GET","POST","PUT","DELETE");
			}
		};
	}
	
	 @Bean
	    CommandLineRunner testConnection(DataSource dataSource) {
	        return args -> {
	            System.out.println("Conectando con: " + dataSource.getConnection().getMetaData().getURL());
	            System.out.println("Driver: " + dataSource.getConnection().getMetaData().getDriverName());
	        };
	    }

	    @Bean
	    CommandLineRunner crearUsuarioPorDefecto(UsuarioRepository usuarioRepository, PasswordEncoder encoder) {
	        return args -> {

	                UsuarioEntity usuario = new UsuarioEntity();
	                usuario.setNombre("Oscar Rodriguez");
	                usuario.setCorreoElectronico("oscarrodriguez@prueba.com");
	                usuario.setUsername("admin");
	                usuario.setPassword(encoder.encode("admin")); 
	                usuario.setRol("ROLE_ADMIN");

	                usuarioRepository.save(usuario);
	                
	                UsuarioEntity usuarioUser = new UsuarioEntity();
	                usuarioUser.setNombre("Gerardo Pino");
	                usuarioUser.setCorreoElectronico("gerardopino@prueba.com");
	                usuarioUser.setUsername("cli");
	                usuarioUser.setPassword(encoder.encode("cli")); 
	                usuarioUser.setRol("ROLE_CLI");

	                usuarioRepository.save(usuarioUser);

	        };
	    }

	    @Bean
	    CommandLineRunner initFondos(FondoRepository fondoRepository) {
	        return args -> {
	            if (fondoRepository.count() == 0) {
	                fondoRepository.save(FondoEntity.builder()
	                        .nombre("FPV_BTG_PACTUAL_RECAUDADORA")
	                        .montoMinimo(75000.0)
	                        .categoria("FPV")
	                        .build());

	                fondoRepository.save(FondoEntity.builder()
	                        .nombre("FPV_BTG_PACTUAL_ECOPETROL")
	                        .montoMinimo(125000.0)
	                        .categoria("FPV")
	                        .build());

	                fondoRepository.save(FondoEntity.builder()
	                        .nombre("DEUDAPRIVADA")
	                        .montoMinimo(50000.0)
	                        .categoria("FIC")
	                        .build());

	                fondoRepository.save(FondoEntity.builder()
	                        .nombre("FDO-ACCIONES")
	                        .montoMinimo(250000.0)
	                        .categoria("FIC")
	                        .build());

	                fondoRepository.save(FondoEntity.builder()
	                        .nombre("FPV_BTG_PACTUAL_DINAMICA")
	                        .montoMinimo(100000.0)
	                        .categoria("FPV")
	                        .build());

	                System.out.println("✅ Fondos iniciales cargados.");
	            }
	        };
	    }

	    //Carga de clientes
	    private static final double SALDO_INICIAL = 500_000.0;

	    @Bean
	    CommandLineRunner initData(FondoRepository fondoRepository, ClienteRepository clienteRepository) {
	        return args -> {
	            // Cargar Fondos
	            if (fondoRepository.count() == 0) {
	                fondoRepository.save(FondoEntity.builder()
	                        .nombre("FPV_BTG_PACTUAL_RECAUDADORA")
	                        .montoMinimo(75000.0)
	                        .categoria("FPV")
	                        .build());

	                fondoRepository.save(FondoEntity.builder()
	                        .nombre("FPV_BTG_PACTUAL_ECOPETROL")
	                        .montoMinimo(125000.0)
	                        .categoria("FPV")
	                        .build());

	                fondoRepository.save(FondoEntity.builder()
	                        .nombre("DEUDAPRIVADA")
	                        .montoMinimo(50000.0)
	                        .categoria("FIC")
	                        .build());

	                fondoRepository.save(FondoEntity.builder()
	                        .nombre("FDO-ACCIONES")
	                        .montoMinimo(250000.0)
	                        .categoria("FIC")
	                        .build());

	                fondoRepository.save(FondoEntity.builder()
	                        .nombre("FPV_BTG_PACTUAL_DINAMICA")
	                        .montoMinimo(100000.0)
	                        .categoria("FPV")
	                        .build());

	                System.out.println("✅ Fondos iniciales cargados.");
	            }

	            // Cargar Clientes
	            if (clienteRepository.count() == 0) {
	                clienteRepository.save(ClienteEntity.builder()
	                        .nombre("Juan Pérez")
	                        .email("juan@example.com")
	                        .telefono("3001112233")
	                        .preferenciaNotificacion(PreferenciaNotificacion.EMAIL)
	                        .saldoDisponible(SALDO_INICIAL)
	                        .build());

	                clienteRepository.save(ClienteEntity.builder()
	                        .nombre("María Gómez")
	                        .email("maria@example.com")
	                        .telefono("3004445566")
	                        .preferenciaNotificacion(PreferenciaNotificacion.SMS)
	                        .saldoDisponible(SALDO_INICIAL)
	                        .build());

	                clienteRepository.save(ClienteEntity.builder()
	                        .nombre("Carlos López")
	                        .email("carlos@example.com")
	                        .telefono("3007778899")
	                        .preferenciaNotificacion(PreferenciaNotificacion.EMAIL)
	                        .saldoDisponible(SALDO_INICIAL)
	                        .build());

	                System.out.println("✅ Clientes iniciales cargados.");
	            }
	        };
	    }
}
