package com.psc.cajutest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.psc.cajutest.dto.request.TransactionRequest;
import com.psc.cajutest.dto.response.ResponseCode;
import com.psc.cajutest.dto.response.TransactionResponse;
import com.psc.cajutest.service.AccountService;
import com.psc.cajutest.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebMvcTest(TransactionController.class)
public class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionService transactionService;

    @MockBean
    private AccountService accountService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAuthorizeTransaction_Approved() throws Exception {
        // Arrange
        TransactionRequest request = new TransactionRequest("123", 50.0, "Test Merchant", "5811");
        TransactionResponse response = TransactionResponse.builder()
                .code(ResponseCode.APPROVED.getCode())
                .build();

        when(transactionService.processTransaction(any(TransactionRequest.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(ResponseCode.APPROVED.getCode()));
    }

    @Test
    void testAuthorizeTransaction_Error() throws Exception {
        // Arrange
        TransactionRequest request = new TransactionRequest("123", 50.0, "Test Merchant", "5811");
        TransactionResponse response = TransactionResponse.builder()
                .code(ResponseCode.ERROR.getCode())
                .build();

        when(transactionService.processTransaction(any(TransactionRequest.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(ResponseCode.ERROR.getCode()));
    }
}
