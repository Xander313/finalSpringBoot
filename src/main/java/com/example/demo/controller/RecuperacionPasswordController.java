package com.example.demo.controller;

import java.security.SecureRandom;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.dto.RecuperacionSolicitudForm;
import com.example.demo.dto.RestablecerContrasenaForm;
import com.example.demo.entity.UsuarioEntity;
import com.example.demo.repository.UsuarioRepository;
import com.example.demo.service.CorreoService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/password")
public class RecuperacionPasswordController {

    private static final String SESSION_RESET_EMAIL = "session_reset_email";
    private static final String SESSION_RESET_CODE = "session_reset_code";
    private static final String SESSION_RESET_EXPIRES = "session_reset_expires";

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final CorreoService correoService;
    private final SecureRandom secureRandom = new SecureRandom();

    public RecuperacionPasswordController(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder,
            CorreoService correoService
    ) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.correoService = correoService;
    }

    @GetMapping("/forgot")
    public String solicitarRecuperacion(Model model) {
        if (!model.containsAttribute("solicitudForm")) {
            model.addAttribute("solicitudForm", new RecuperacionSolicitudForm());
        }
        return "auth/recuperar-password";
    }

    @PostMapping("/forgot")
    public String enviarCodigoRecuperacion(
            @Valid @ModelAttribute("solicitudForm") RecuperacionSolicitudForm solicitudForm,
            BindingResult bindingResult,
            HttpSession session
    ) {
        UsuarioEntity usuario = usuarioRepository.findByEmailUsu(solicitudForm.getEmail()).orElse(null);
        if (usuario == null) {
            bindingResult.rejectValue("email", "email.notfound", "No existe un usuario con ese correo");
        }

        if (bindingResult.hasErrors()) {
            return "auth/recuperar-password";
        }

        String codigo = generarCodigo();
        long expiracion = System.currentTimeMillis() + (10 * 60 * 1000);

        session.setAttribute(SESSION_RESET_EMAIL, solicitudForm.getEmail());
        session.setAttribute(SESSION_RESET_CODE, codigo);
        session.setAttribute(SESSION_RESET_EXPIRES, expiracion);

        try {
            correoService.enviarCodigo(
                    solicitudForm.getEmail(),
                    "Codigo de recuperacion de contrasena",
                    "Usa el siguiente codigo para restablecer tu contrasena:",
                    codigo
            );
        } catch (Exception ex) {
            bindingResult.reject("mail.error", "No se pudo enviar el correo de recuperacion");
            return "auth/recuperar-password";
        }

        return "redirect:/password/reset?sent";
    }

    @GetMapping("/reset")
    public String formReset(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        if (session.getAttribute(SESSION_RESET_EMAIL) == null
                || session.getAttribute(SESSION_RESET_CODE) == null) {
            redirectAttributes.addFlashAttribute("error", "Primero solicita un codigo de recuperacion");
            return "redirect:/password/forgot";
        }

        if (!model.containsAttribute("resetForm")) {
            model.addAttribute("resetForm", new RestablecerContrasenaForm());
        }

        model.addAttribute("emailDestino", session.getAttribute(SESSION_RESET_EMAIL));
        return "auth/reset-password";
    }

    @PostMapping("/reset")
    public String resetPassword(
            @Valid @ModelAttribute("resetForm") RestablecerContrasenaForm resetForm,
            BindingResult bindingResult,
            HttpSession session,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        String email = (String) session.getAttribute(SESSION_RESET_EMAIL);
        String codigo = (String) session.getAttribute(SESSION_RESET_CODE);
        Long expiracion = (Long) session.getAttribute(SESSION_RESET_EXPIRES);

        if (email == null || codigo == null || expiracion == null) {
            redirectAttributes.addFlashAttribute("error", "La sesion de recuperacion no existe o expiro");
            return "redirect:/password/forgot";
        }

        if (!resetForm.getNuevaPassword().equals(resetForm.getConfirmarPassword())) {
            bindingResult.rejectValue("confirmarPassword", "password.mismatch", "Las contrasenas no coinciden");
        }

        if (!bindingResult.hasErrors() && !codigo.equals(resetForm.getCodigo())) {
            bindingResult.rejectValue("codigo", "codigo.invalid", "Codigo incorrecto");
        }

        if (System.currentTimeMillis() > expiracion) {
            limpiarSesionReset(session);
            redirectAttributes.addFlashAttribute("error", "El codigo de recuperacion expiro");
            return "redirect:/password/forgot";
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("emailDestino", email);
            return "auth/reset-password";
        }

        UsuarioEntity usuario = usuarioRepository.findByEmailUsu(email).orElse(null);
        if (usuario == null) {
            limpiarSesionReset(session);
            redirectAttributes.addFlashAttribute("error", "No existe un usuario con ese correo");
            return "redirect:/password/forgot";
        }

        usuario.setPasswordUsu(passwordEncoder.encode(resetForm.getNuevaPassword()));
        usuarioRepository.save(usuario);

        limpiarSesionReset(session);
        return "redirect:/?resetSuccess";
    }

    private String generarCodigo() {
        return String.format("%06d", secureRandom.nextInt(1_000_000));
    }

    private void limpiarSesionReset(HttpSession session) {
        session.removeAttribute(SESSION_RESET_EMAIL);
        session.removeAttribute(SESSION_RESET_CODE);
        session.removeAttribute(SESSION_RESET_EXPIRES);
    }
}
