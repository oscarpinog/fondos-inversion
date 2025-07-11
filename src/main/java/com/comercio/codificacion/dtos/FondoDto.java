package com.comercio.codificacion.dtos;

import lombok.Data;

@Data
public class FondoDto {
    private Long id;
    private String nombre;
    private Double montoMinimo;
    private String categoria;
}
