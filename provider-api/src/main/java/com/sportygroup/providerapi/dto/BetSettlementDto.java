package com.sportygroup.providerapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class BetSettlementDto extends EventMessageDto {
    MatchOutcome result;
}
