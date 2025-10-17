package com.example.foundation.service;

import com.example.foundation.model.Bin;
import com.example.foundation.model.Tag;
import com.example.foundation.repository.BinRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class BinServiceTest {

    private BinRepository binRepository;
    private TagService tagService;
    private BinService binService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        binRepository = mock(BinRepository.class);
        tagService = mock(TagService.class);
        binService = new BinService(binRepository, tagService);
    }

    @Test
    @DisplayName("getUnassignedBins returns IN_STOCK bins")
    void getUnassignedBins() {
        Bin b1 = new Bin();
        b1.setId("bin-1");
        b1.setStatus("IN_STOCK");

        given(binRepository.findBinsByStatus("IN_STOCK")).willReturn(List.of(b1));

        var result = binService.getUnassignedBins();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo("bin-1");
    }

    @Test
    @DisplayName("getAssignedBins returns ASSIGNED bins")
    void getAssignedBins() {
        Bin b = new Bin();
        b.setId("bin-a");
        b.setStatus("ASSIGNED");

        given(binRepository.findBinsByStatus("ASSIGNED")).willReturn(List.of(b));

        var result = binService.getAssignedBins();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getStatus()).isEqualTo("ASSIGNED");
    }

    @Test
    @DisplayName("scanBin delegates to TagService")
    void scanBin_delegates() {
        Tag t = new Tag();
        t.setId("tag-9");
        t.setTagId("TAG-9");

        given(tagService.scanTag("TAG-9")).willReturn(t);

        var result = binService.scanBin("TAG-9");

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("tag-9");
    }
}
