package com.example.foundation.service;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.example.foundation.model.Route;
import com.example.foundation.model.Truck;
import com.example.foundation.repository.TruckRepository;

class TruckServiceTest {
    @Mock
    private TruckRepository truckRepository;

    @InjectMocks
    private TruckService truckService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllTrucks() {
        Truck truck1 = mock(Truck.class);
        Truck truck2 = mock(Truck.class);
        when(truckRepository.findAll()).thenReturn(List.of(truck1, truck2));
        List<Truck> result = truckService.getAllTrucks();
        assertEquals(2, result.size());
    }

    @Test
    void testGetTruckById() {
        Truck truck = mock(Truck.class);
        when(truckRepository.findById("id1")).thenReturn(Optional.of(truck));
        Optional<Truck> result = truckService.getTruckById("id1");
        assertTrue(result.isPresent());
        assertEquals(truck, result.get());
    }

    @Test
    void testCreateTruck() {
        Truck truck = mock(Truck.class);
        when(truckRepository.save(truck)).thenReturn(truck);
        Truck result = truckService.createTruck(truck);
        assertEquals(truck, result);
    }

    @Test
    void testUpdateTruck_Success() {
        Truck existingTruck = mock(Truck.class);
        Truck updatedTruck = mock(Truck.class);
        when(truckRepository.findById("id1")).thenReturn(Optional.of(existingTruck));
        when(updatedTruck.getRegistrationNumber()).thenReturn("REG123");
        when(updatedTruck.getModel()).thenReturn("ModelX");
        when(updatedTruck.getStatus()).thenReturn("Available");
        when(updatedTruck.getAssignedRoute()).thenReturn(null);
        when(updatedTruck.getAssignedCrew()).thenReturn(null);
        when(truckRepository.save(existingTruck)).thenReturn(existingTruck);
        Truck result = truckService.updateTruck("id1", updatedTruck);
        verify(existingTruck).setRegistrationNumber("REG123");
        verify(existingTruck).setModel("ModelX");
        verify(existingTruck).setStatus("Available");
        verify(existingTruck).setAssignedRoute(null);
        verify(existingTruck).setAssignedCrew(null);
        assertEquals(existingTruck, result);
    }

    @Test
    void testUpdateTruck_TruckNotFound() {
        Truck updatedTruck = mock(Truck.class);
        when(truckRepository.findById("id1")).thenReturn(Optional.empty());
        RuntimeException ex = assertThrows(RuntimeException.class, () -> truckService.updateTruck("id1", updatedTruck));
        assertEquals("Truck not found", ex.getMessage());
    }

    @Test
    void testDeleteTruck() {
        truckService.deleteTruck("id1");
        verify(truckRepository).deleteById("id1");
    }

    @Test
    void testAssignRoute_Success() {
        Truck truck = mock(Truck.class);
        Route route = mock(Route.class);
        when(truckRepository.findById("truck1")).thenReturn(Optional.of(truck));
        when(truckRepository.save(truck)).thenReturn(truck);
        Truck result = truckService.assignRoute("truck1", route);
        verify(truck).setAssignedRoute(route);
        verify(truck).setStatus("Assigned");
        verify(truckRepository).save(truck);
        assertEquals(truck, result);
    }

    @Test
    void testAssignRoute_TruckNotFound() {
        Route route = mock(Route.class);
        when(truckRepository.findById("truck1")).thenReturn(Optional.empty());
        RuntimeException ex = assertThrows(RuntimeException.class, () -> truckService.assignRoute("truck1", route));
        assertEquals("Truck not found", ex.getMessage());
    }

    @Test
    void testUnassignRoute_Success() {
        Truck truck = mock(Truck.class);
        when(truckRepository.findById("truck1")).thenReturn(Optional.of(truck));
        when(truckRepository.save(truck)).thenReturn(truck);
        Truck result = truckService.unassignRoute("truck1");
        verify(truck).setAssignedRoute(null);
        verify(truck).setStatus("Available");
        verify(truckRepository).save(truck);
        assertEquals(truck, result);
    }

    @Test
    void testUnassignRoute_TruckNotFound() {
        when(truckRepository.findById("truck1")).thenReturn(Optional.empty());
        RuntimeException ex = assertThrows(RuntimeException.class, () -> truckService.unassignRoute("truck1"));
        assertEquals("Truck not found", ex.getMessage());
    }

    @Test
    void testGetTrucksByRoute() {
        Truck truck1 = mock(Truck.class);
        Truck truck2 = mock(Truck.class);
        when(truckRepository.findByAssignedRoute_Id("route1")).thenReturn(List.of(truck1, truck2));
        List<Truck> result = truckService.getTrucksByRoute("route1");
        assertEquals(2, result.size());
        assertTrue(result.contains(truck1));
        assertTrue(result.contains(truck2));
    }
}
