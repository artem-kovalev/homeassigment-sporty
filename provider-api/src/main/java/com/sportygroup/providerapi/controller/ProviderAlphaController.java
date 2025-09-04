package com.sportygroup.providerapi.controller;

import com.sportygroup.providerapi.dto.EventMessageDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/provider-alpha")
public class ProviderAlphaController {

    @PostMapping("/feed")
    public EventMessageDto feed(@RequestBody EventMessageDto dto) {
        return dto;
    }
}


