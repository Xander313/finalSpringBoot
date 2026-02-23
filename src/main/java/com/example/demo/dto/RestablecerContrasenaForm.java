package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class RestablecerContrasenaForm {

    @NotBlank(message = "Ingrese el codigo de verificacion")
    @Pattern(regexp = "\\d{6}", message = "El codigo debe tener 6 digitos")
    private String codigo;

    @NotBlank(message = "Ingrese la nueva contrasena")
    @Size(min = 6, message = "La contrasena debe tener al menos 6 caracteres")
    private String nuevaPassword;

    @NotBlank(message = "Confirme la nueva contrasena")
    private String confirmarPassword;

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNuevaPassword() {
        return nuevaPassword;
    }

    public void setNuevaPassword(String nuevaPassword) {
        this.nuevaPassword = nuevaPassword;
    }

    public String getConfirmarPassword() {
        return confirmarPassword;
    }

    public void setConfirmarPassword(String confirmarPassword) {
        this.confirmarPassword = confirmarPassword;
    }
}
