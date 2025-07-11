package com.comercio.codificacion.dtos;


import lombok.Data;

@Data
public class ClienteDto {
    private Long id;
    private String nombre;
    private String email;
    private String telefono;
    private Double saldoDisponible;
    private PreferenciaNotificacion preferenciaNotificacion;
}
