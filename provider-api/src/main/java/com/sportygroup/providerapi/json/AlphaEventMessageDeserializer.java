package com.sportygroup.providerapi.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.sportygroup.providerapi.dto.BetSettlementDto;
import com.sportygroup.providerapi.dto.EventMessageDto;
import com.sportygroup.providerapi.dto.MatchOutcome;

public class AlphaEventMessageDeserializer extends JsonDeserializer<EventMessageDto> {

    @Override
    public EventMessageDto deserialize(JsonParser p, DeserializationContext context) throws java.io.IOException {
        //TODO need implement

        JsonNode n = p.readValueAsTree();
        var result = new BetSettlementDto(MatchOutcome.AWAY_WIN);
        result.setEventId("TEST1");
        return result;
    }
}

