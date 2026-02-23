package com.example.demo.dto;

public class RegistroPendienteSession {

    private String nombreUsu;
    private String apellidoUsu;
    private String emailUsu;
    private String usuarioUsu;
    private String passwordHash;
    private String telefonoUsu;
    private Integer sexoUsu;
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

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
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
