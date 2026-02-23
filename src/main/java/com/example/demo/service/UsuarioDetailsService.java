package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.entity.UsuarioEntity;
import com.example.demo.repository.UsuarioRepository;

@Service
public class UsuarioDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UsuarioEntity usuario = usuarioRepository.findByUsuarioUsu(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        if (usuario.getCodigoPer() != null) {
            authorities.add(new SimpleGrantedAuthority("ROLE_PER_" + usuario.getCodigoPer()));
        }

        boolean enabled = usuario.getEstadoUsu() == null
                || !"INACTIVO".equalsIgnoreCase(usuario.getEstadoUsu().trim());

        return User.withUsername(usuario.getUsuarioUsu())
                .password(usuario.getPasswordUsu())
                .authorities(authorities)
                .disabled(!enabled)
                .build();
    }
}
