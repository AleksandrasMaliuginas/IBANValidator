package com.task.danskebank;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
class IbanControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private IbanValidator validatorMock;

    @Test
    void validateIban() throws Exception {
        String validIban = "LT92 3596 8868 4256 1956";
        String invalidIban = "LT92";

        when(validatorMock.isValid(validIban)).thenReturn(true);
        when(validatorMock.isValid(invalidIban)).thenReturn(false);

        String validJsonResponse = "{ \"isValid\": true }";
        String invalidJsonResponse = "{ \"iban\": \"" + invalidIban + "\", \"isValid\": false }";

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/iban/" + validIban)
                        .accept("application/json")
                        .contentType("application/json"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(validJsonResponse));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/iban/" + invalidIban)
                        .accept("application/json"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(invalidJsonResponse));
    }
}