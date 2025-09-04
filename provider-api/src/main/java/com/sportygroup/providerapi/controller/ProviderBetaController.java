package com.sportygroup.providerapi.controller;

import com.sportygroup.providerapi.annotation.UseDeserializer;
import com.sportygroup.providerapi.dto.EventMessageDto;
import com.sportygroup.providerapi.json.BetaEventMessageDeserializer;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/provider-beta")
public class ProviderBetaController {

    @PostMapping("/feed")
    public EventMessageDto feed(@UseDeserializer(BetaEventMessageDeserializer.class) EventMessageDto dto) {
        return dto;
    }
}
