package com.example.foundation.controller;

import com.example.foundation.model.Tag;
import com.example.foundation.service.TagService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TagController.class)
@AutoConfigureMockMvc(addFilters = false)
public class TagControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TagService tagService;

    @Test
    @DisplayName("GET /api/tags/all - returns all tags")
    void getAllTags_returnsList() throws Exception {
        Tag t1 = new Tag();
        t1.setId("t1");
        t1.setTagId("TAG-1");

        Tag t2 = new Tag();
        t2.setId("t2");
        t2.setTagId("TAG-2");

        given(tagService.getAllTags()).willReturn(List.of(t1, t2));

        mockMvc.perform(get("/api/tags/all").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].tagId", is("TAG-1")));
    }

    @Test
    @DisplayName("GET /api/tags/scan/{tagId} - returns a tag")
    void scanTag_returnsTag() throws Exception {
        Tag t = new Tag();
        t.setId("tag-123");
        t.setTagId("TAG-100");

        given(tagService.scanTag(anyString())).willReturn(t);

        mockMvc.perform(get("/api/tags/scan/TAG-100").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is("tag-123")))
                .andExpect(jsonPath("$.tagId", is("TAG-100")));
    }
}
