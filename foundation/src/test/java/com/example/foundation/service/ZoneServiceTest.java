package com.example.foundation.service;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.example.foundation.model.Zone;
import com.example.foundation.repository.ZoneRepository;

class ZoneServiceTest {
    @Mock
    private ZoneRepository zoneRepository;

    @InjectMocks
    private ZoneService zoneService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateZone_Success() {
        when(zoneRepository.existsByName("ZoneA")).thenReturn(false);
        Zone zone = mock(Zone.class);
        when(zoneRepository.save(any(Zone.class))).thenReturn(zone);
        Zone result = zoneService.createZone("ZoneA");
        verify(zoneRepository).save(any(Zone.class));
        assertEquals(zone, result);
    }

    @Test
    void testCreateZone_AlreadyExists() {
        when(zoneRepository.existsByName("ZoneA")).thenReturn(true);
        RuntimeException ex = assertThrows(RuntimeException.class, () -> zoneService.createZone("ZoneA"));
        assertEquals("Zone already exists", ex.getMessage());
    }

    @Test
    void testGetAllZones() {
        Zone zone1 = mock(Zone.class);
        Zone zone2 = mock(Zone.class);
        when(zoneRepository.findAll()).thenReturn(List.of(zone1, zone2));
        List<Zone> result = zoneService.getAllZones();
        assertEquals(2, result.size());
        assertTrue(result.contains(zone1));
        assertTrue(result.contains(zone2));
    }

    @Test
    void testGetZoneById_Success() {
        Zone zone = mock(Zone.class);
        when(zoneRepository.findById("id1")).thenReturn(Optional.of(zone));
        Zone result = zoneService.getZoneById("id1");
        assertEquals(zone, result);
    }

    @Test
    void testGetZoneById_NotFound() {
        when(zoneRepository.findById("id1")).thenReturn(Optional.empty());
        RuntimeException ex = assertThrows(RuntimeException.class, () -> zoneService.getZoneById("id1"));
        assertEquals("Zone not found", ex.getMessage());
    }
}
