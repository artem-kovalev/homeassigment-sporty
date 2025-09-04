package com.sportygroup.providerapi.controller;

import com.sportygroup.providerapi.dto.EventMessageDto;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/provider-beta")
public class ProviderBetaController {

    @PostMapping("/feed")
    public EventMessageDto feed(@RequestBody EventMessageDto dto) {
        return dto;
    }
}
