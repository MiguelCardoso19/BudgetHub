package com.paymentMicroservice.controller;

import com.paymentMicroservice.service.WebhookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class WebhookControllerTest {

    @Mock
    private WebhookService webhookService;

    @InjectMocks
    private WebhookController webhookController;

    private MockMvc mockMvc;

    private String sigHeader;

    @BeforeEach
    public void setUp() {
        openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(webhookController).build();
        sigHeader = "mocked-signature";
    }

    @Test
    public void testHandleStripeEvents_Success() throws Exception {
        String payload = "{\"event\":\"payment_success\"}";

        mockMvc.perform(post("/api/v1/webhook/stripe")
                        .header("Stripe-Signature", sigHeader)
                        .content(payload)
                        .contentType("application/json"))
                .andExpect(status().isOk());

        verify(webhookService, times(1)).handleWebhookEvents(payload, sigHeader);
    }

    @Test
    public void testHandleStripeEvents_InvalidPayload() throws Exception {
        mockMvc.perform(post("/api/v1/webhook/stripe")
                        .header("Stripe-Signature", sigHeader)
                        .content("")
                        .contentType("application/json"))
                .andExpect(status().isBadRequest());

        verify(webhookService, never()).handleWebhookEvents(anyString(), anyString());
    }
}