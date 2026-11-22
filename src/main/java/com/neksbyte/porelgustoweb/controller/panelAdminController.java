package com.neksbyte.porelgustoweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class panelAdminController {
    @GetMapping("/panelAdmin")
    public String goToPanelAdmin(Model model){
        model.addAttribute("Titulo","PanelAdmin");
        return "panelAdmin";
    }
}
