package com.example.foundation.controller;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import com.example.foundation.model.Zone;
import com.example.foundation.service.ZoneService;

class ZoneControllerTest {
    @Mock
    private ZoneService zoneService;
    @InjectMocks
    private ZoneController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateZone_Success() {
        Zone zone = mock(Zone.class);
        when(zone.getName()).thenReturn("ZoneA");
        when(zoneService.createZone("ZoneA")).thenReturn(zone);
        ResponseEntity<?> response = controller.createZone(zone);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(zone, response.getBody());
    }

    @Test
    void testCreateZone_Failure() {
        Zone zone = mock(Zone.class);
        when(zone.getName()).thenReturn("ZoneA");
        when(zoneService.createZone("ZoneA")).thenThrow(new RuntimeException("Zone already exists"));
        ResponseEntity<?> response = controller.createZone(zone);
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Zone already exists", response.getBody());
    }

    @Test
    void testGetAllZones() {
        Zone zone1 = mock(Zone.class);
        Zone zone2 = mock(Zone.class);
        when(zoneService.getAllZones()).thenReturn(List.of(zone1, zone2));
        ResponseEntity<List<Zone>> response = controller.getAllZones();
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(List.of(zone1, zone2), response.getBody());
    }

    @Test
    void testGetZoneById_Success() {
        Zone zone = mock(Zone.class);
        when(zoneService.getZoneById("id1")).thenReturn(zone);
        ResponseEntity<?> response = controller.getZoneById("id1");
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(zone, response.getBody());
    }

    @Test
    void testGetZoneById_Failure() {
        when(zoneService.getZoneById("id1")).thenThrow(new RuntimeException("Zone not found"));
        ResponseEntity<?> response = controller.getZoneById("id1");
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Zone not found", response.getBody());
    }
}
