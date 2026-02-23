package com.example.demo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegistroUsuarioForm {

    @NotBlank(message = "Ingrese el nombre")
    private String nombreUsu;

    @NotBlank(message = "Ingrese el apellido")
    private String apellidoUsu;

    @NotBlank(message = "Ingrese el correo")
    @Email(message = "Ingrese un correo valido")
    private String emailUsu;

    @NotBlank(message = "Ingrese el usuario")
    private String usuarioUsu;

    @NotBlank(message = "Ingrese la contrasena")
    @Size(min = 6, message = "La contrasena debe tener al menos 6 caracteres")
    private String password;

    @NotBlank(message = "Confirme la contrasena")
    private String confirmarPassword;

    private String telefonoUsu;

    private Integer sexoUsu = 1;

    private String identificacionUsu;

    public String getNombreUsu() {
        return nombreUsu;
    }

    public void setNombreUsu(String nombreUsu) {
        this.nombreUsu = nombreUsu;
    }

    public String getApellidoUsu() {
        return apellidoUsu;
    }

    public void setApellidoUsu(String apellidoUsu) {
        this.apellidoUsu = apellidoUsu;
    }

    public String getEmailUsu() {
        return emailUsu;
    }

    public void setEmailUsu(String emailUsu) {
        this.emailUsu = emailUsu;
    }

    public String getUsuarioUsu() {
        return usuarioUsu;
    }

    public void setUsuarioUsu(String usuarioUsu) {
        this.usuarioUsu = usuarioUsu;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmarPassword() {
        return confirmarPassword;
    }

    public void setConfirmarPassword(String confirmarPassword) {
        this.confirmarPassword = confirmarPassword;
    }

    public String getTelefonoUsu() {
        return telefonoUsu;
    }

    public void setTelefonoUsu(String telefonoUsu) {
        this.telefonoUsu = telefonoUsu;
    }

    public Integer getSexoUsu() {
        return sexoUsu;
    }

    public void setSexoUsu(Integer sexoUsu) {
        this.sexoUsu = sexoUsu;
    }

    public String getIdentificacionUsu() {
        return identificacionUsu;
    }

    public void setIdentificacionUsu(String identificacionUsu) {
        this.identificacionUsu = identificacionUsu;
    }
}
