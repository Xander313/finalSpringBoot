package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.AccionPlanAmbientalEntity;

public interface AccionPlanAmbientalRepository extends JpaRepository<AccionPlanAmbientalEntity, Long> {

    List<AccionPlanAmbientalEntity> findBySeccionPlanAmbientalCodigoSeccion(Long codigoSeccion);

    long countBySeccionPlanAmbientalCodigoSeccion(Long codigoSeccion);
}
