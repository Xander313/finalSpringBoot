package com.example.demo.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.entity.EtapaPlanAmbientalEntity;
import com.example.demo.entity.SeccionPlanAmbientalEntity;
import com.example.demo.repository.EtapaPlanAmbientalRepository;
import com.example.demo.repository.SeccionPlanAmbientalRepository;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/secciones-plan-ambiental")
public class SeccionPlanAmbientalController {

    private final SeccionPlanAmbientalRepository seccionPlanAmbientalRepository;
    private final EtapaPlanAmbientalRepository etapaPlanAmbientalRepository;

    public SeccionPlanAmbientalController(
            SeccionPlanAmbientalRepository seccionPlanAmbientalRepository,
            EtapaPlanAmbientalRepository etapaPlanAmbientalRepository
    ) {
        this.seccionPlanAmbientalRepository = seccionPlanAmbientalRepository;
        this.etapaPlanAmbientalRepository = etapaPlanAmbientalRepository;
    }

    @GetMapping
    public String listar(Model model) {
        List<SeccionPlanAmbientalEntity> secciones = seccionPlanAmbientalRepository.findAll();
        model.addAttribute("secciones", secciones);
        return "seccion_plan_ambiental/listaSeccion";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("seccion", new SeccionPlanAmbientalEntity());
        return "seccion_plan_ambiental/formularioSeccion";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        SeccionPlanAmbientalEntity seccion = seccionPlanAmbientalRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID de sección inválido: " + id));

        model.addAttribute("seccion", seccion);
        return "seccion_plan_ambiental/formularioSeccion";
    }

    @PostMapping("/guardar")
    public String guardar(
            @Valid @ModelAttribute("seccion") SeccionPlanAmbientalEntity seccion,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            return "seccion_plan_ambiental/formularioSeccion";
        }

        boolean editando = seccion.getCodigoSeccion() != null
                && seccionPlanAmbientalRepository.existsById(seccion.getCodigoSeccion());

        seccion.setColorSeccion(normalizarColorSeccionRgba(seccion.getColorSeccion()));
        seccionPlanAmbientalRepository.save(seccion);

        redirectAttributes.addFlashAttribute(
                "mensaje",
                editando ? "Sección editada exitosamente" : "Sección creada exitosamente"
        );
        redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        return "redirect:/secciones-plan-ambiental";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        if (!seccionPlanAmbientalRepository.existsById(id)) {
            redirectAttributes.addFlashAttribute("mensaje", "Sección no encontrada");
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
            return "redirect:/secciones-plan-ambiental";
        }

        seccionPlanAmbientalRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("mensaje", "Sección eliminada exitosamente");
        redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        return "redirect:/secciones-plan-ambiental";
    }

    @ModelAttribute("etapas")
    public List<EtapaPlanAmbientalEntity> etapas() {
        return etapaPlanAmbientalRepository.findAllByOrderByTituloEtapaAsc();
    }

    private String normalizarColorSeccionRgba(String color) {
        if (color == null || color.isBlank()) {
            return null;
        }

        String valor = color.trim();

        if (valor.startsWith("rgba(") || valor.startsWith("rgb(")) {
            return valor;
        }

        if (!valor.startsWith("#")) {
            return valor;
        }

        String hex = valor.substring(1);
        if (hex.length() == 3) {
            hex = "" + hex.charAt(0) + hex.charAt(0)
                    + hex.charAt(1) + hex.charAt(1)
                    + hex.charAt(2) + hex.charAt(2);
        }

        if (hex.length() != 6) {
            return valor;
        }

        try {
            int r = Integer.parseInt(hex.substring(0, 2), 16);
            int g = Integer.parseInt(hex.substring(2, 4), 16);
            int b = Integer.parseInt(hex.substring(4, 6), 16);
            return "rgba(" + r + "," + g + "," + b + ",0.5)";
        } catch (NumberFormatException ex) {
            return valor;
        }
    }
}
