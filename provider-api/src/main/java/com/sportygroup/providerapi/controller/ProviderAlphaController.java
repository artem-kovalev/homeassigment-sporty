package com.sportygroup.providerapi.controller;

import com.sportygroup.providerapi.annotation.UseDeserializer;
import com.sportygroup.providerapi.dto.EventMessageDto;
import com.sportygroup.providerapi.json.AlphaEventMessageDeserializer;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/provider-alpha")
public class ProviderAlphaController {

    @PostMapping("/feed")
    public EventMessageDto feed(@UseDeserializer(AlphaEventMessageDeserializer.class) EventMessageDto dto) {
        return dto;
    }
}


