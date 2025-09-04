package com.sportygroup.providerapi.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.sportygroup.providerapi.dto.BetSettlementDto;
import com.sportygroup.providerapi.dto.EventMessageDto;
import com.sportygroup.providerapi.dto.OddsChangeDto;

import java.io.IOException;

public class AlphaEventMessageDeserializer extends JsonDeserializer<EventMessageDto> {
    private static final String MESSAGE_TYPE_FIELD ="msg_type";
    private static final String MESSAGE_TYPE_ODDS_VALUE ="odds_update";
    private static final String MESSAGE_TYPE_SETTLEMENT_VALUE ="settlement";

    private static final String EVENT_ID_FIELD ="event_id";

    private static final String ODDS_FIELD ="values";

    private static final String HOME_FIELD ="1";
    private static final String DRAW_FIELD ="X";
    private static final String AWAY_FIELD ="2";
    private static final String OUTCOME_FIELD ="outcome";

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
        var odds = DeserializerHelper.get(root, ODDS_FIELD, context);
        var home = DeserializerHelper.reqDouble(odds, HOME_FIELD, context);
        var draw = DeserializerHelper.reqDouble(odds, DRAW_FIELD, context);
        var away = DeserializerHelper.reqDouble(odds, AWAY_FIELD, context);

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
