package com.example.demo.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.entity.SeccionPlanAmbientalEntity;
import com.example.demo.repository.SeccionPlanAmbientalRepository;

@Controller
@RequestMapping("/secciones-plan-ambiental")
public class SeccionPlanAmbientalController {

    private final SeccionPlanAmbientalRepository seccionPlanAmbientalRepository;

    public SeccionPlanAmbientalController(SeccionPlanAmbientalRepository seccionPlanAmbientalRepository) {
        this.seccionPlanAmbientalRepository = seccionPlanAmbientalRepository;
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
        return "seccion_plan_ambiental/form_seccion";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        SeccionPlanAmbientalEntity seccion = seccionPlanAmbientalRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID de sección inválido: " + id));

        model.addAttribute("seccion", seccion);
        return "seccion_plan_ambiental/form_seccion";
    }

    @PostMapping("/guardar")
    public String guardar(
            @ModelAttribute("seccion") SeccionPlanAmbientalEntity seccion,
            RedirectAttributes redirectAttributes
    ) {
        boolean editando = seccion.getCodigoSeccion() != null
                && seccionPlanAmbientalRepository.existsById(seccion.getCodigoSeccion());

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
}
