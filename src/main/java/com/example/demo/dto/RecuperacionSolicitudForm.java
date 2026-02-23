package com.example.demo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class RecuperacionSolicitudForm {

    @NotBlank(message = "Ingrese el correo")
    @Email(message = "Ingrese un correo valido")
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
