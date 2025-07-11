package com.comercio.codificacion.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

import com.comercio.codificacion.dtos.TipoTransaccion;

@Entity
@Table(name = "transacciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransaccionEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id")
    private ClienteEntity cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fondo_id")
    private FondoEntity fondo;

    @Enumerated(EnumType.STRING)
    private TipoTransaccion tipo; // APERTURA o CANCELACION

    private LocalDateTime fecha;

    private Double monto;
}
