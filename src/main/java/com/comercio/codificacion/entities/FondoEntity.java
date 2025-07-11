package com.comercio.codificacion.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "fondos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FondoEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

    private String nombre;

    private Double montoMinimo;

    private String categoria;

    @OneToMany(mappedBy = "fondo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TransaccionEntity> transacciones;
}
