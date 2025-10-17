package com.example.foundation.service;

import com.example.foundation.model.Tag;
import com.example.foundation.repository.TagRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class TagServiceTest {

    private TagRepository tagRepository;
    private TagService tagService;

    @BeforeEach
    void setup() {
        tagRepository = mock(TagRepository.class);
        tagService = new TagService(tagRepository);
    }

    @Test
    @DisplayName("getAllTags returns list from repository")
    void getAllTags() {
        Tag t = new Tag();
        t.setId("t1");
        t.setTagId("TAG-1");

        given(tagRepository.findAll()).willReturn(List.of(t));

        var result = tagService.getAllTags();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTagId()).isEqualTo("TAG-1");
    }

    @Test
    @DisplayName("scanTag returns tag when found")
    void scanTag_found() {
        Tag t = new Tag();
        t.setId("t2");
        t.setTagId("TAG-2");

        given(tagRepository.findByTagId("TAG-2")).willReturn(Optional.of(t));

        var result = tagService.scanTag("TAG-2");

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("t2");
    }

    @Test
    @DisplayName("scanTag throws when not found")
    void scanTag_notFound() {
        given(tagRepository.findByTagId("MISSING")).willReturn(Optional.empty());

        assertThatThrownBy(() -> tagService.scanTag("MISSING"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Tag not found");
    }
}
