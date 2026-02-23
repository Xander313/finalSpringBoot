package com.example.demo.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class CorreoService {

    private final JavaMailSender javaMailSender;

    public CorreoService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void enviarCodigo(String destino, String asunto, String contexto, String codigo) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(destino);
        message.setSubject(asunto);
        message.setText(contexto + "\n\nCodigo: " + codigo + "\n\nEste codigo expira en 10 minutos.");
        javaMailSender.send(message);
    }
}
