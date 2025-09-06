package com.sportygroup.providerapi.services;

import com.sportygroup.providerapi.dto.EventMessageDto;
import jakarta.validation.constraints.NotNull;

import java.io.IOException;

public interface EventPublisher {
     void send(@NotNull EventMessageDto message) throws IOException;
}
