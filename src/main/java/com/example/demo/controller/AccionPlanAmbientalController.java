package com.example.demo.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.entity.AccionPlanAmbientalEntity;
import com.example.demo.entity.SeccionPlanAmbientalEntity;
import com.example.demo.repository.AccionPlanAmbientalRepository;
import com.example.demo.repository.SeccionPlanAmbientalRepository;

@Controller
@RequestMapping("/acciones-plan-ambiental")
public class AccionPlanAmbientalController {

    private final AccionPlanAmbientalRepository accionPlanAmbientalRepository;
    private final SeccionPlanAmbientalRepository seccionPlanAmbientalRepository;

    public AccionPlanAmbientalController(
            AccionPlanAmbientalRepository accionPlanAmbientalRepository,
            SeccionPlanAmbientalRepository seccionPlanAmbientalRepository
    ) {
        this.accionPlanAmbientalRepository = accionPlanAmbientalRepository;
        this.seccionPlanAmbientalRepository = seccionPlanAmbientalRepository;
    }

    @GetMapping
    public String listar(
            @RequestParam(value = "seccionId", required = false) Long seccionId,
            Model model
    ) {
        List<AccionPlanAmbientalEntity> acciones = seccionId != null
                ? accionPlanAmbientalRepository.findBySeccionPlanAmbientalCodigoSeccion(seccionId)
                : accionPlanAmbientalRepository.findAll();

        model.addAttribute("acciones", acciones);
        model.addAttribute("secciones", seccionPlanAmbientalRepository.findAll());
        model.addAttribute("seccionIdSeleccionada", seccionId);
        return "accion_plan_ambiental/listaAccion";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("accion", new AccionPlanAmbientalEntity());
        model.addAttribute("secciones", seccionPlanAmbientalRepository.findAll());
        return "accion_plan_ambiental/formularioAccion";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        AccionPlanAmbientalEntity accion = accionPlanAmbientalRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID de acción inválido: " + id));

        model.addAttribute("accion", accion);
        model.addAttribute("secciones", seccionPlanAmbientalRepository.findAll());
        return "accion_plan_ambiental/formularioAccion";
    }

    @PostMapping("/guardar")
    public String guardar(
            @ModelAttribute("accion") AccionPlanAmbientalEntity accion,
            @RequestParam(value = "seccionId", required = false) Long seccionId,
            RedirectAttributes redirectAttributes
    ) {
        boolean editando = accion.getCodigoAccion() != null
                && accionPlanAmbientalRepository.existsById(accion.getCodigoAccion());

        if (seccionId != null) {
            SeccionPlanAmbientalEntity seccion = seccionPlanAmbientalRepository.findById(seccionId)
                    .orElseThrow(() -> new IllegalArgumentException("Sección no encontrada"));
            accion.setSeccionPlanAmbiental(seccion);
        } else {
            accion.setSeccionPlanAmbiental(null);
        }

        if (accion.getEstadoAplica() == null) {
            accion.setEstadoAplica(1);
        }

        accion.setColorAccion(normalizarColorAccionRgba(accion.getColorAccion()));

        accionPlanAmbientalRepository.save(accion);

        redirectAttributes.addFlashAttribute(
                "mensaje",
                editando ? "Acción editada exitosamente" : "Acción creada exitosamente"
        );
        redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        return "redirect:/acciones-plan-ambiental";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        if (!accionPlanAmbientalRepository.existsById(id)) {
            redirectAttributes.addFlashAttribute("mensaje", "Acción no encontrada");
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
            return "redirect:/acciones-plan-ambiental";
        }

        accionPlanAmbientalRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("mensaje", "Acción eliminada exitosamente");
        redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        return "redirect:/acciones-plan-ambiental";
    }

    private String normalizarColorAccionRgba(String color) {
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
            return "rgba(" + r + "," + g + "," + b + ",0.2)";
        } catch (NumberFormatException ex) {
            return valor;
        }
    }
}
