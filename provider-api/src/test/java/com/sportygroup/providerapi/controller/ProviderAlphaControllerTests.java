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

@WebMvcTest(controllers = ProviderAlphaController.class)
@AutoConfigureMockMvc
public class ProviderAlphaControllerTests {

    private static final String URL = "/provider-alpha/feed";

    @Autowired
    MockMvc mvc;

    @MockitoBean
    EventPublisher publisher;

    @Test
    public void postFeed_returns202_andConvertedToBetSettlement() throws Exception {

        // Arrange
        String json = """
                {
                    "msg_type": "settlement",
                    "event_id": "ev123",
                    "outcome": "1"
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
                    "msg_type": "odds_update",
                    "event_id": "ev123",
                    "values": {
                        "1": 2.0,
                        "X": 3.1,
                        "2": 3.8
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
                    "msg_type": "settlement",
                    "event_id": "ev123",
                    "outcome": "1"
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
                    "MSG_TYPE": "odds_UPDATE",
                    "event_ID": "ev123",
                    "ValUes": {
                        "1": 2.0,
                        "x": 3.1,
                        "2": 3.8
                    }
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


