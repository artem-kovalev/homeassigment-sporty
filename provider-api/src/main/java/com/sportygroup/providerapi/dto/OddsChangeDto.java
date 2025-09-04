package com.sportygroup.providerapi.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class OddsChangeDto extends EventMessageDto {
    double home;
    double draw;
    double away;
}

