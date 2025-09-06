package com.sportygroup.providerapi.services.impl;

import com.sportygroup.providerapi.dto.BetSettlementDto;
import com.sportygroup.providerapi.dto.EventMessageDto;
import com.sportygroup.providerapi.dto.OddsChangeDto;
import com.sportygroup.providerapi.services.EventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
public class EventPublisherImpl implements EventPublisher {

    @Override
    public void send(EventMessageDto message) throws IOException {
        if (message instanceof BetSettlementDto) {
            log.info("Put bet settlement into queue");
            return;
        } else if (message instanceof OddsChangeDto) {
            log.info("Put odds change into queue");
            return;
        }

        throw new UnsupportedOperationException("%s not supported yet.".formatted(message.getClass().getSimpleName()));
    }
}
