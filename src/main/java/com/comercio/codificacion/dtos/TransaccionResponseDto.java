package com.comercio.codificacion.dtos;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TransaccionResponseDto {
    private Long transaccionId;
    private Long fondoId;
    private String fondoNombre;
    private String tipo; // "APERTURA" o "CANCELACION"
    private LocalDateTime fecha;
    private Double monto;
}
