package com.sportygroup.providerapi.controller;

import com.sportygroup.providerapi.annotation.UseDeserializer;
import com.sportygroup.providerapi.dto.EventMessageDto;
import com.sportygroup.providerapi.json.BetaEventMessageDeserializer;
import com.sportygroup.providerapi.services.EventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;


@RestController
@RequestMapping("/provider-beta")
@RequiredArgsConstructor
public class ProviderBetaController {

    private final EventPublisher publisher;

    @PostMapping("/feed")
    public ResponseEntity<Void> feed(@UseDeserializer(BetaEventMessageDeserializer.class) EventMessageDto dto) throws IOException {

        publisher.send(dto);

        return ResponseEntity.accepted().build();
    }
}
