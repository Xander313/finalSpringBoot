package com.example.demo.model;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Entity
@Table(name = "seccion_plan_ambiental")
public class SeccionPlanAmbientalModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "codigo_seccion", nullable = false)
    private Long codigoSeccion;

    @Column(name = "numero_seccion")
    private Integer numeroSeccion;

    @Column(name = "titulo_seccion", length = 255)
    private String tituloSeccion;

    @Lob
    @Column(name = "objetivo_seccion")
    private String objetivoSeccion;

    @Column(name = "color_seccion", length = 25)
    private String colorSeccion;

    @Column(name = "fk_cod_etapa")
    private Long fkCodEtapa;

    @Column(name = "fecha_creado_seccion", nullable = false, insertable = false, updatable = false)
    private LocalDateTime fechaCreadoSeccion;

    @Column(name = "fecha_editado_seccion", nullable = false, insertable = false, updatable = false)
    private LocalDateTime fechaEditadoSeccion;

    @OneToMany(mappedBy = "seccionPlanAmbiental", fetch = FetchType.LAZY)
    private List<AccionPlanAmbientalModel> acciones;

    public Long getCodigoSeccion() {
        return codigoSeccion;
    }

    public void setCodigoSeccion(Long codigoSeccion) {
        this.codigoSeccion = codigoSeccion;
    }

    public Integer getNumeroSeccion() {
        return numeroSeccion;
    }

    public void setNumeroSeccion(Integer numeroSeccion) {
        this.numeroSeccion = numeroSeccion;
    }

    public String getTituloSeccion() {
        return tituloSeccion;
    }

    public void setTituloSeccion(String tituloSeccion) {
        this.tituloSeccion = tituloSeccion;
    }

    public String getObjetivoSeccion() {
        return objetivoSeccion;
    }

    public void setObjetivoSeccion(String objetivoSeccion) {
        this.objetivoSeccion = objetivoSeccion;
    }

    public String getColorSeccion() {
        return colorSeccion;
    }

    public void setColorSeccion(String colorSeccion) {
        this.colorSeccion = colorSeccion;
    }

    public Long getFkCodEtapa() {
        return fkCodEtapa;
    }

    public void setFkCodEtapa(Long fkCodEtapa) {
        this.fkCodEtapa = fkCodEtapa;
    }

    public LocalDateTime getFechaCreadoSeccion() {
        return fechaCreadoSeccion;
    }

    public void setFechaCreadoSeccion(LocalDateTime fechaCreadoSeccion) {
        this.fechaCreadoSeccion = fechaCreadoSeccion;
    }

    public LocalDateTime getFechaEditadoSeccion() {
        return fechaEditadoSeccion;
    }

    public void setFechaEditadoSeccion(LocalDateTime fechaEditadoSeccion) {
        this.fechaEditadoSeccion = fechaEditadoSeccion;
    }

    public List<AccionPlanAmbientalModel> getAcciones() {
        return acciones;
    }

    public void setAcciones(List<AccionPlanAmbientalModel> acciones) {
        this.acciones = acciones;
    }
}
