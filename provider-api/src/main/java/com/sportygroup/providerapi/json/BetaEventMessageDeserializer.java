package com.sportygroup.providerapi.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.sportygroup.providerapi.dto.BetSettlementDto;
import com.sportygroup.providerapi.dto.EventMessageDto;
import com.sportygroup.providerapi.dto.OddsChangeDto;

import java.io.IOException;

public class BetaEventMessageDeserializer extends JsonDeserializer<EventMessageDto> {

    private static final String MESSAGE_TYPE_FIELD ="type";
    private static final String MESSAGE_TYPE_ODDS_VALUE ="odds";
    private static final String MESSAGE_TYPE_SETTLEMENT_VALUE ="settlement";

    private static final String EVENT_ID_FIELD ="event_id";

    private static final String VALUES_FIELD ="odds";

    private static final String HOME_FIELD ="home";
    private static final String DRAW_FIELD ="draw";
    private static final String AWAY_FIELD ="away";
    private static final String OUTCOME_FIELD ="result";

    @Override
    public EventMessageDto deserialize(JsonParser p, DeserializationContext context) throws java.io.IOException {
        JsonNode root = p.readValueAsTree();

        var type = DeserializerHelper.reqText(root, MESSAGE_TYPE_FIELD, context).toLowerCase();
        return switch (type) {
            case MESSAGE_TYPE_ODDS_VALUE -> createOddsChange(root, context);
            case MESSAGE_TYPE_SETTLEMENT_VALUE -> createSettlement(root, context);
            default -> context.reportPropertyInputMismatch(EventMessageDto.class,
                    MESSAGE_TYPE_FIELD,
                    "Message type '%s' is not support",
                    type);
        };

    }

    private static OddsChangeDto createOddsChange(JsonNode root, DeserializationContext context) throws IOException {

        var eventId = DeserializerHelper.reqText(root, EVENT_ID_FIELD, context);
        var values = DeserializerHelper.get(root, VALUES_FIELD, context);
        var home = DeserializerHelper.reqDouble(values, HOME_FIELD, context);
        var draw = DeserializerHelper.reqDouble(values, DRAW_FIELD, context);
        var away = DeserializerHelper.reqDouble(values, AWAY_FIELD, context);

        OddsChangeDto result = new OddsChangeDto();
        result.setEventId(eventId);
        result.setHome(home);
        result.setDraw(draw);
        result.setAway(away);

        return result;
    }
    private static BetSettlementDto createSettlement(JsonNode root, DeserializationContext context) throws IOException {
        var eventId = DeserializerHelper.reqText(root, EVENT_ID_FIELD, context);
        var outcome = DeserializerHelper.redOutcome(root, OUTCOME_FIELD, context);

        BetSettlementDto result = new BetSettlementDto();
        result.setEventId(eventId);
        result.setResult(outcome);
        return result;
    }
}
