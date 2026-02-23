package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.UsuarioEntity;

public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Long> {

    Optional<UsuarioEntity> findByUsuarioUsu(String usuarioUsu);

    Optional<UsuarioEntity> findByEmailUsu(String emailUsu);

    boolean existsByUsuarioUsu(String usuarioUsu);

    boolean existsByEmailUsu(String emailUsu);
}
