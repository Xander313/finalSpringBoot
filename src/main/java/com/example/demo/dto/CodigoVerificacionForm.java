package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class CodigoVerificacionForm {

    @NotBlank(message = "Ingrese el codigo de verificacion")
    @Pattern(regexp = "\\d{6}", message = "El codigo debe tener 6 digitos")
    private String codigo;

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
}
