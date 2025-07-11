package com.comercio.codificacion.entities;


import jakarta.persistence.*;
import lombok.*;

import java.util.List;

import com.comercio.codificacion.dtos.PreferenciaNotificacion;

@Entity
@Table(name = "clientes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClienteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    private String email;

    private String telefono;

    private Double saldoDisponible;

    @Enumerated(EnumType.STRING)
    private PreferenciaNotificacion preferenciaNotificacion;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TransaccionEntity> transacciones;
}
