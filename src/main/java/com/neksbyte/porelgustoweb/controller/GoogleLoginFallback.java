package com.neksbyte.porelgustoweb.controller;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Profile("!google")
public class GoogleLoginFallback {
    @GetMapping("/oauth2/authorization/google")
    public String googleLoginNotConfigured() {
        return "redirect:/login?googleUnavailable";
    }
}
