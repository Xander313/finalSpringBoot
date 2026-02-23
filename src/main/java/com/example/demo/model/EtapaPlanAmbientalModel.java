package com.example.demo.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "etapa_plan_ambiental")
public class EtapaPlanAmbientalModel {

    @Id
    @Column(name = "codigo_etapa", nullable = false)
    private Long codigoEtapa;

    @Column(name = "titulo_etapa", length = 255)
    private String tituloEtapa;

    @Column(name = "fk_cod_plan_ambiental")
    private Long fkCodPlanAmbiental;

    @Column(name = "fecha_creado_etapa", nullable = false, insertable = false, updatable = false)
    private LocalDateTime fechaCreadoEtapa;

    @Column(name = "fecha_editado_etapa", nullable = false, insertable = false, updatable = false)
    private LocalDateTime fechaEditadoEtapa;

    public Long getCodigoEtapa() {
        return codigoEtapa;
    }

    public void setCodigoEtapa(Long codigoEtapa) {
        this.codigoEtapa = codigoEtapa;
    }

    public String getTituloEtapa() {
        return tituloEtapa;
    }

    public void setTituloEtapa(String tituloEtapa) {
        this.tituloEtapa = tituloEtapa;
    }

    public Long getFkCodPlanAmbiental() {
        return fkCodPlanAmbiental;
    }

    public void setFkCodPlanAmbiental(Long fkCodPlanAmbiental) {
        this.fkCodPlanAmbiental = fkCodPlanAmbiental;
    }

    public LocalDateTime getFechaCreadoEtapa() {
        return fechaCreadoEtapa;
    }

    public void setFechaCreadoEtapa(LocalDateTime fechaCreadoEtapa) {
        this.fechaCreadoEtapa = fechaCreadoEtapa;
    }

    public LocalDateTime getFechaEditadoEtapa() {
        return fechaEditadoEtapa;
    }

    public void setFechaEditadoEtapa(LocalDateTime fechaEditadoEtapa) {
        this.fechaEditadoEtapa = fechaEditadoEtapa;
    }
}
