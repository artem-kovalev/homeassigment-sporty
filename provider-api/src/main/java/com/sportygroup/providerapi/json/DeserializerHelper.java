package com.sportygroup.providerapi.json;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.sportygroup.providerapi.dto.EventMessageDto;
import com.sportygroup.providerapi.dto.MatchOutcome;

import java.io.IOException;
import java.util.Map;

public final class DeserializerHelper {

    private static final Map<String, MatchOutcome> ALIASES = Map.of(
            "home", MatchOutcome.HOME_WIN, "1", MatchOutcome.HOME_WIN,
            "draw", MatchOutcome.DRAW,     "x", MatchOutcome.DRAW,
            "away", MatchOutcome.AWAY_WIN, "2", MatchOutcome.AWAY_WIN);

    public static JsonNode get(JsonNode node, String field, DeserializationContext context)  throws IOException {
        var result = node.get(field);
        if (result == null || result.isNull()) {
            context.reportInputMismatch(EventMessageDto.class, "Field '%s' is required and must be string", field);
        }
        return result;
    }

    public static String reqText(JsonNode node, String field, DeserializationContext context)  throws IOException {
        var result = node.get(field);
        if (result == null || result.isNull() || !result.isTextual()) {
            context.reportInputMismatch(EventMessageDto.class, "Field '%s' is required and must be string", field);
        }
        return result.asText();
    }

    public static double reqDouble(JsonNode node, String field, DeserializationContext context)  throws IOException {
        var result = node.get(field);
        if (result == null || result.isNull() || !result.isNumber()) {
            context.reportInputMismatch(EventMessageDto.class, "Field '%s' is required and must be double", field);
        }
        return result.asDouble();
    }

    public static MatchOutcome redOutcome(JsonNode node, String field, DeserializationContext context)  throws IOException {
        var outcome = reqText(node, field, context).toLowerCase();
        MatchOutcome out = ALIASES.get(outcome);
        if (out == null) {
            context.reportInputMismatch(MatchOutcome.class, "Unknown outcome: '%s'", outcome);
        }

        return out;
    }
}
