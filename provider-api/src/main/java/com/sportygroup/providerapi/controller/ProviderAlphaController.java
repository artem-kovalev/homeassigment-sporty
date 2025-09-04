package com.sportygroup.providerapi.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/provider-alpha")
public class ProviderAlphaController {

    @GetMapping("/feed")
    public String feed() {
        return "Hello, Alpha!";
    }
}


