package com.example.foundation.controller;

import com.example.foundation.model.Bin;
import com.example.foundation.model.Tag;
import com.example.foundation.service.BinService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BinController.class)
@AutoConfigureMockMvc(addFilters = false)
public class BinControllerTest {

    @Autowired
    private MockMvc mockMvc;

    
    @org.springframework.boot.test.mock.mockito.MockBean
    private BinService binService;

    @Test
    @DisplayName("GET /api/bins/unassigned - returns list of bins")
    void getUnassignedBins_returnsList() throws Exception {
        Bin b1 = new Bin();
        b1.setId("bin-1");
        b1.setBinCode("BIN-ABC123");
        b1.setType("GENERAL");
        b1.setStatus("IN_STOCK");

        Bin b2 = new Bin();
        b2.setId("bin-2");
        b2.setBinCode("BIN-XYZ789");
        b2.setType("RECYCLING");
        b2.setStatus("IN_STOCK");

        given(binService.getUnassignedBins()).willReturn(List.of(b1, b2));

        mockMvc.perform(get("/api/bins/unassigned").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is("bin-1")))
                .andExpect(jsonPath("$[0].binCode", is("BIN-ABC123")))
                .andExpect(jsonPath("$[1].id", is("bin-2")));
    }

    @Test
    @DisplayName("GET /api/bins/assigned - returns assigned bins list")
    void getAssignedBins_returnsList() throws Exception {
        Bin b1 = new Bin();
        b1.setId("a-1");
        b1.setBinCode("BIN-A1");
        b1.setType("GENERAL");
        b1.setStatus("ASSIGNED");

        given(binService.getAssignedBins()).willReturn(List.of(b1));

        mockMvc.perform(get("/api/bins/assigned").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].status", is("ASSIGNED")))
                .andExpect(jsonPath("$[0].binCode", is("BIN-A1")));
    }

    @Test
    @DisplayName("GET /api/bins/scan/{tagId} - returns scanned Tag")
    void scanBin_returnsTag() throws Exception {
        Tag t = new Tag();
        t.setId("tag-123");
        t.setTagId("TAG-100");
        t.setActive(true);
        t.setWeight(12.5f);
        t.setFillLevel(0.25f);

        given(binService.scanBin(anyString())).willReturn(t);

        mockMvc.perform(get("/api/bins/scan/TAG-100").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is("tag-123")))
                .andExpect(jsonPath("$.tagId", is("TAG-100")))
                .andExpect(jsonPath("$.active", is(true)));
    }
}
