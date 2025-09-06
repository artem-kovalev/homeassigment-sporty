package com.sportygroup.providerapi.controller;

import com.sportygroup.providerapi.dto.BetSettlementDto;
import com.sportygroup.providerapi.dto.EventMessageDto;
import com.sportygroup.providerapi.dto.OddsChangeDto;
import com.sportygroup.providerapi.services.EventPublisher;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ProviderBetaController.class)
@AutoConfigureMockMvc
public class ProviderBetaControllerTests {

    private static final String URL = "/provider-beta/feed";

    @Autowired
    MockMvc mvc;

    @MockitoBean
    EventPublisher publisher;

    @Test
    public void postFeed_returns202_andConvertedToBetSettlement() throws Exception {

        // Arrange
        String json = """
                {
                    "type": "SETTLEMENT",
                    "event_id": "ev321",
                    "result": "away"
                }
                """;

        // Act
        var result = mvc.perform(post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));

        // Assert
        result.andExpect(status().isAccepted());

        ArgumentCaptor<BetSettlementDto> captor = ArgumentCaptor.forClass(BetSettlementDto.class);
        verify(publisher, times(1)).send(captor.capture());
    }

    @Test
    public void postFeed_returns202_andConvertedToOddsChange() throws Exception {
        // Arrange
        String json = """ 
                {
                    "type": "ODDS",
                    "event_id": "ev321",
                    "odds": {
                        "home": 2,
                        "draw": 1.1,
                        "away": 4.0
                    }
                }
                """;

        // Act
        var result = mvc.perform(post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));

        // Assert
        result.andExpect(status().isAccepted());

        ArgumentCaptor<OddsChangeDto> captor = ArgumentCaptor.forClass(OddsChangeDto.class);
        verify(publisher, times(1)).send(captor.capture());
    }

    @Test
    public void postFeed_returns400_whenBodyIsInvalid() throws Exception {
        // Arrange
        String json = """ 
                {
                }
                """;

        // Act
        var result = mvc.perform(post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));

        // Assert
        result.andExpect(status().isBadRequest());
    }

    @Test
    public void postFeed_returns500_whenPublisherThrowsException() throws Exception {
        // Arrange
        String json = """
                {
                    "type": "SETTLEMENT",
                    "event_id": "ev321",
                    "result": "away"
                }
                """;

        doThrow(new RuntimeException("Publisher failed"))
                .when(publisher).send(any(EventMessageDto.class));

        // Act
        var result = mvc.perform(post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));

        // Assert
        result.andExpect(status().is5xxServerError());
    }

    @Test
    public void postFeed_returns202_whenModelFieldAndValuesHasDifferentCase() throws Exception {

        // Arrange
        String json = """
                {
                    "tYpE": "SETTLEMent",
                    "evenT_ID": "ev321",
                    "Result": "awAy"
                }
                """;

        // Act
        var result = mvc.perform(post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));

        // Assert
        result.andExpect(status().isAccepted());
    }
}
