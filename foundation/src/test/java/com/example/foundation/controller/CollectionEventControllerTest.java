package com.example.foundation.controller;

import com.example.foundation.model.Bin;
import com.example.foundation.model.CollectionEvent;
import com.example.foundation.model.Tag;
import com.example.foundation.repository.BinRepository;
import com.example.foundation.repository.TagRepository;
import com.example.foundation.service.CollectionEventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CollectionEventControllerTest {
    @Mock
    private BinRepository binRepository;
    @Mock
    private TagRepository tagRepository;
    @Mock
    private CollectionEventService collectionEventService;
    @InjectMocks
    private CollectionEventController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCollectBin_Success() {
        Bin bin = mock(Bin.class);
        Tag tag = mock(Tag.class);
        CollectionEvent event = mock(CollectionEvent.class);
        when(binRepository.findById("bin1")).thenReturn(Optional.of(bin));
        when(tagRepository.findByTagId("tag1")).thenReturn(Optional.of(tag));
        when(tag.getWeight()).thenReturn(5.0);
        when(collectionEventService.createCollectionEvent(bin, 5.0)).thenReturn(event);
        when(bin.getBinCode()).thenReturn("BINCODE");
        ResponseEntity<?> response = controller.collectBin("bin1", "tag1", "crew1");
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Bin BINCODE marked as collected", response.getBody());
        verify(tag).setWeight(0);
        verify(tag).setFillLevel(0);
        verify(tagRepository).save(tag);
    }

    @Test
    void testCollectBin_BinNotFound() {
        when(binRepository.findById("bin1")).thenReturn(Optional.empty());
        ResponseEntity<?> response = controller.collectBin("bin1", "tag1", "crew1");
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Bin not found", response.getBody());
    }

    @Test
    void testCollectBin_TagNotFound() {
        Bin bin = mock(Bin.class);
        when(binRepository.findById("bin1")).thenReturn(Optional.of(bin));
        when(tagRepository.findByTagId("tag1")).thenReturn(Optional.empty());
        ResponseEntity<?> response = controller.collectBin("bin1", "tag1", "crew1");
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Tag not found", response.getBody());
    }

    @Test
    void testCollectBin_EventNull() {
        Bin bin = mock(Bin.class);
        Tag tag = mock(Tag.class);
        when(binRepository.findById("bin1")).thenReturn(Optional.of(bin));
        when(tagRepository.findByTagId("tag1")).thenReturn(Optional.of(tag));
        when(tag.getWeight()).thenReturn(5.0);
        when(collectionEventService.createCollectionEvent(bin, 5.0)).thenReturn(null);
        ResponseEntity<?> response = controller.collectBin("bin1", "tag1", "crew1");
        assertEquals(500, response.getStatusCodeValue());
        assertEquals("Failed to collect bin", response.getBody());
    }

    @Test
    void testCollectBin_Exception() {
        Bin bin = mock(Bin.class);
        Tag tag = mock(Tag.class);
        when(binRepository.findById("bin1")).thenReturn(Optional.of(bin));
        when(tagRepository.findByTagId("tag1")).thenReturn(Optional.of(tag));
        when(tag.getWeight()).thenThrow(new RuntimeException("error"));
        ResponseEntity<?> response = controller.collectBin("bin1", "tag1", "crew1");
        assertEquals(500, response.getStatusCodeValue());
        assertEquals("Failed to collect bin", response.getBody());
    }
}
