package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "usuario")
public class UsuarioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "codigo_usu", nullable = false)
    private Long codigoUsu;

    @Column(name = "nombre_usu", length = 500)
    private String nombreUsu;

    @Column(name = "apellido_usu", length = 500)
    private String apellidoUsu;

    @Column(name = "email_usu", length = 500)
    private String emailUsu;

    @Column(name = "usuario_usu", length = 500)
    private String usuarioUsu;

    @Column(name = "password_usu", length = 500)
    private String passwordUsu;

    @Column(name = "estado_usu", length = 100)
    private String estadoUsu;

    @Column(name = "codigo_per")
    private Long codigoPer;

    @Column(name = "telefono_usu", length = 15)
    private String telefonoUsu;

    @Column(name = "sexo_usu")
    private Integer sexoUsu;

    @Column(name = "identificacion_usu", length = 255)
    private String identificacionUsu;

    public Long getCodigoUsu() {
        return codigoUsu;
    }

    public void setCodigoUsu(Long codigoUsu) {
        this.codigoUsu = codigoUsu;
    }

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

    public String getPasswordUsu() {
        return passwordUsu;
    }

    public void setPasswordUsu(String passwordUsu) {
        this.passwordUsu = passwordUsu;
    }

    public String getEstadoUsu() {
        return estadoUsu;
    }

    public void setEstadoUsu(String estadoUsu) {
        this.estadoUsu = estadoUsu;
    }

    public Long getCodigoPer() {
        return codigoPer;
    }

    public void setCodigoPer(Long codigoPer) {
        this.codigoPer = codigoPer;
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
