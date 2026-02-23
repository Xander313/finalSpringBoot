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

import com.example.demo.dto.CodigoVerificacionForm;
import com.example.demo.dto.RegistroPendienteSession;
import com.example.demo.dto.RegistroUsuarioForm;
import com.example.demo.entity.UsuarioEntity;
import com.example.demo.repository.UsuarioRepository;
import com.example.demo.service.CorreoService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/registro")
public class RegistroController {

    private static final String SESSION_REGISTRO_DATA = "session_registro_data";
    private static final String SESSION_REGISTRO_CODE = "session_registro_code";
    private static final String SESSION_REGISTRO_EXPIRES = "session_registro_expires";

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final CorreoService correoService;
    private final SecureRandom secureRandom = new SecureRandom();

    public RegistroController(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder,
            CorreoService correoService
    ) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.correoService = correoService;
    }

    @GetMapping
    public String formularioRegistro(Model model) {
        if (!model.containsAttribute("registroForm")) {
            model.addAttribute("registroForm", new RegistroUsuarioForm());
        }
        return "auth/registro";
    }

    @PostMapping("/enviar-codigo")
    public String enviarCodigo(
            @Valid @ModelAttribute("registroForm") RegistroUsuarioForm form,
            BindingResult bindingResult,
            HttpSession session,
            RedirectAttributes redirectAttributes
    ) {
        if (!form.getPassword().equals(form.getConfirmarPassword())) {
            bindingResult.rejectValue("confirmarPassword", "password.mismatch", "Las contrasenas no coinciden");
        }

        if (usuarioRepository.existsByUsuarioUsu(form.getUsuarioUsu())) {
            bindingResult.rejectValue("usuarioUsu", "usuario.exists", "El usuario ya existe");
        }

        if (usuarioRepository.existsByEmailUsu(form.getEmailUsu())) {
            bindingResult.rejectValue("emailUsu", "email.exists", "El correo ya esta registrado");
        }

        if (bindingResult.hasErrors()) {
            return "auth/registro";
        }

        String codigo = generarCodigo();
        long expiracion = System.currentTimeMillis() + (10 * 60 * 1000);

        RegistroPendienteSession pendiente = new RegistroPendienteSession();
        pendiente.setNombreUsu(form.getNombreUsu());
        pendiente.setApellidoUsu(form.getApellidoUsu());
        pendiente.setEmailUsu(form.getEmailUsu());
        pendiente.setUsuarioUsu(form.getUsuarioUsu());
        pendiente.setPasswordHash(passwordEncoder.encode(form.getPassword()));
        pendiente.setTelefonoUsu(form.getTelefonoUsu());
        pendiente.setSexoUsu(form.getSexoUsu());
        pendiente.setIdentificacionUsu(form.getIdentificacionUsu());

        session.setAttribute(SESSION_REGISTRO_DATA, pendiente);
        session.setAttribute(SESSION_REGISTRO_CODE, codigo);
        session.setAttribute(SESSION_REGISTRO_EXPIRES, expiracion);

        try {
            correoService.enviarCodigo(
                    form.getEmailUsu(),
                    "Codigo de verificacion de cuenta",
                    "Usa el siguiente codigo para completar tu registro:",
                    codigo
            );
        } catch (Exception ex) {
            bindingResult.reject("mail.error", "No se pudo enviar el correo de verificacion");
            return "auth/registro";
        }

        redirectAttributes.addFlashAttribute("mensaje", "Se envio un codigo de verificacion a tu correo");
        return "redirect:/registro/verificar";
    }

    @GetMapping("/verificar")
    public String verificarRegistro(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        if (session.getAttribute(SESSION_REGISTRO_CODE) == null
                || session.getAttribute(SESSION_REGISTRO_DATA) == null) {
            redirectAttributes.addFlashAttribute("error", "La sesion de verificacion no existe o expiro");
            return "redirect:/registro";
        }

        if (!model.containsAttribute("codigoForm")) {
            model.addAttribute("codigoForm", new CodigoVerificacionForm());
        }

        RegistroPendienteSession pendiente = (RegistroPendienteSession) session.getAttribute(SESSION_REGISTRO_DATA);
        model.addAttribute("emailDestino", pendiente.getEmailUsu());
        return "auth/verificar-registro";
    }

    @PostMapping("/verificar")
    public String confirmarRegistro(
            @Valid @ModelAttribute("codigoForm") CodigoVerificacionForm codigoForm,
            BindingResult bindingResult,
            HttpSession session,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        String codigoSession = (String) session.getAttribute(SESSION_REGISTRO_CODE);
        Long expiracion = (Long) session.getAttribute(SESSION_REGISTRO_EXPIRES);
        RegistroPendienteSession pendiente = (RegistroPendienteSession) session.getAttribute(SESSION_REGISTRO_DATA);

        if (codigoSession == null || expiracion == null || pendiente == null) {
            redirectAttributes.addFlashAttribute("error", "La sesion de verificacion no existe o expiro");
            return "redirect:/registro";
        }

        if (System.currentTimeMillis() > expiracion) {
            limpiarSesionRegistro(session);
            redirectAttributes.addFlashAttribute("error", "El codigo de verificacion expiro");
            return "redirect:/registro";
        }

        if (!bindingResult.hasErrors() && !codigoSession.equals(codigoForm.getCodigo())) {
            bindingResult.rejectValue("codigo", "codigo.invalid", "Codigo incorrecto");
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("emailDestino", pendiente.getEmailUsu());
            return "auth/verificar-registro";
        }

        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setNombreUsu(pendiente.getNombreUsu());
        usuario.setApellidoUsu(pendiente.getApellidoUsu());
        usuario.setEmailUsu(pendiente.getEmailUsu());
        usuario.setUsuarioUsu(pendiente.getUsuarioUsu());
        usuario.setPasswordUsu(pendiente.getPasswordHash());
        usuario.setTelefonoUsu(pendiente.getTelefonoUsu());
        usuario.setSexoUsu(pendiente.getSexoUsu() == null ? 1 : pendiente.getSexoUsu());
        usuario.setIdentificacionUsu(pendiente.getIdentificacionUsu());
        usuario.setEstadoUsu("ACTIVO");

        usuarioRepository.save(usuario);
        limpiarSesionRegistro(session);

        return "redirect:/?registered";
    }

    private String generarCodigo() {
        return String.format("%06d", secureRandom.nextInt(1_000_000));
    }

    private void limpiarSesionRegistro(HttpSession session) {
        session.removeAttribute(SESSION_REGISTRO_DATA);
        session.removeAttribute(SESSION_REGISTRO_CODE);
        session.removeAttribute(SESSION_REGISTRO_EXPIRES);
    }
}
