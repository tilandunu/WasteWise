package com.example.foundation.controller;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import com.example.foundation.model.Route;
import com.example.foundation.model.Truck;
import com.example.foundation.repository.RouteRepository;
import com.example.foundation.service.TruckService;

class TruckControllerTest {
    @Mock
    private TruckService truckService;
    @Mock
    private RouteRepository routeRepository;
    @InjectMocks
    private TruckController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllTrucks() {
        Truck truck1 = mock(Truck.class);
        Truck truck2 = mock(Truck.class);
        when(truckService.getAllTrucks()).thenReturn(List.of(truck1, truck2));
        ResponseEntity<List<Truck>> response = controller.getAllTrucks();
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(List.of(truck1, truck2), response.getBody());
    }

    @Test
    void testGetTruckById_Found() {
        Truck truck = mock(Truck.class);
        when(truckService.getTruckById("id1")).thenReturn(Optional.of(truck));
        ResponseEntity<?> response = controller.getTruckById("id1");
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(truck, response.getBody());
    }

    @Test
    void testGetTruckById_NotFound() {
        when(truckService.getTruckById("id1")).thenReturn(Optional.empty());
        ResponseEntity<?> response = controller.getTruckById("id1");
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void testCreateTruck() {
        Truck truck = mock(Truck.class);
        when(truckService.createTruck(truck)).thenReturn(truck);
        ResponseEntity<Truck> response = controller.createTruck(truck);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(truck, response.getBody());
    }

    @Test
    void testUpdateTruck_Success() {
        Truck truck = mock(Truck.class);
        when(truckService.updateTruck("id1", truck)).thenReturn(truck);
        ResponseEntity<?> response = controller.updateTruck("id1", truck);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(truck, response.getBody());
    }

    @Test
    void testUpdateTruck_NotFound() {
        Truck truck = mock(Truck.class);
        when(truckService.updateTruck("id1", truck)).thenThrow(new RuntimeException("Not found"));
        ResponseEntity<?> response = controller.updateTruck("id1", truck);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void testDeleteTruck_Success() {
        doNothing().when(truckService).deleteTruck("id1");
        ResponseEntity<?> response = controller.deleteTruck("id1");
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Truck deleted successfully", response.getBody());
    }

    @Test
    void testDeleteTruck_NotFound() {
        doThrow(new RuntimeException("Not found")).when(truckService).deleteTruck("id1");
        ResponseEntity<?> response = controller.deleteTruck("id1");
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void testAssignRoute_Success() {
        Route route = mock(Route.class);
        Truck truck = mock(Truck.class);
        when(routeRepository.findById("route1")).thenReturn(Optional.of(route));
        when(truckService.assignRoute("truck1", route)).thenReturn(truck);
        ResponseEntity<?> response = controller.assignRoute("truck1", "route1");
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(truck, response.getBody());
    }

    @Test
    void testAssignRoute_RouteNotFound() {
        when(routeRepository.findById("route1")).thenReturn(Optional.empty());
        RuntimeException ex = assertThrows(RuntimeException.class, () -> controller.assignRoute("truck1", "route1"));
        assertEquals("Route not found", ex.getMessage());
    }

    @Test
    void testUnassignRoute() {
        Truck truck = mock(Truck.class);
        when(truckService.unassignRoute("truck1")).thenReturn(truck);
        ResponseEntity<?> response = controller.unassignRoute("truck1");
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(truck, response.getBody());
    }

    @Test
    void testGetTrucksByRoute() {
        Truck truck1 = mock(Truck.class);
        Truck truck2 = mock(Truck.class);
        when(truckService.getTrucksByRoute("route1")).thenReturn(List.of(truck1, truck2));
        ResponseEntity<List<Truck>> response = controller.getTrucksByRoute("route1");
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(List.of(truck1, truck2), response.getBody());
    }
}
