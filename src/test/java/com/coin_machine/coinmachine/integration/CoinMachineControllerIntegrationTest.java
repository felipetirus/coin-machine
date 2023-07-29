package com.coin_machine.coinmachine.integration;


import com.coin_machine.coinmachine.model.NewCoins;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CoinMachineControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Order(1)
    public void getTotalCoins_initialprogram_Success() throws Exception {
        mockMvc.perform(get("/machine/total-coins"))
                .andExpect(status().isOk())
                .andExpect(content().string("108.00"));
    }

    @Test
    @Order(2)
    public void changeCoins_send_50_Success() throws Exception {
        mockMvc.perform(post("/machine/change-bills")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[5, 10]"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].amount").value(0.34))
                .andExpect(jsonPath("$[0].quantity").value(44))
                .andExpect(jsonPath("$[1].amount").value(0.01))
                .andExpect(jsonPath("$[1].quantity").value(4));
    }

//    @Test
//    @Order(3)
    public void changeCoins_send_5_mostCoins_Success() throws Exception {
        mockMvc.perform(post("/machine/change-bills?mostCoins=true")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[5]"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].amount").value(0.01))
                .andExpect(jsonPath("$[0].quantity").value(96))
                .andExpect(jsonPath("$[1].amount").value(0.05))
                .andExpect(jsonPath("$[1].quantity").value(80));
    }

    @Test
    public void changeCoins_invalidNote_errorMessage() throws Exception {
        mockMvc.perform(post("/machine/change-bills")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[13, 10]"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].message").value("changeNotes.listOfBills[0].<list element>: Invalid note: 13"));
    }

    @Test
    public void changeCoins_emptyList_errorMessage() throws Exception {
        mockMvc.perform(post("/machine/change-bills")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[]"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].message").value("changeNotes.listOfBills: Input bills list cannot be empty: []"));
    }

    @Test
    @Order(4)
    public void getAllTransactions_checkNewAmount_Success() throws Exception {
        mockMvc.perform(get("/machine/transaction/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].listBills[0]").value(5))
                .andExpect(jsonPath("$[0].listBills[1]").value(10));
    }

    @Test
    @Order(5)
    public void addCoins_25x100coins_success() throws Exception {
        List<NewCoins> listOfCoins = new ArrayList<>();
        listOfCoins.add(new NewCoins(25, 100));
        ObjectMapper mapper = new ObjectMapper();
        mockMvc.perform(post("/machine/add-coins")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsBytes(listOfCoins)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("New total of coins is 118.00"));
    }

    @Test
    public void addCoins_invalidCoin_success() throws Exception {
        List<NewCoins> listOfCoins = new ArrayList<>();
        listOfCoins.add(new NewCoins(3, 100));
        ObjectMapper mapper = new ObjectMapper();
        mockMvc.perform(post("/machine/add-coins")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsBytes(listOfCoins)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].message").value("addCoins.listOfCoins[0].amount: Invalid coin: 3"));
    }

}