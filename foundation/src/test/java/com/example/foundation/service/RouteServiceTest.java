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
import com.example.foundation.model.Zone;
import com.example.foundation.repository.RouteRepository;
import com.example.foundation.repository.TruckRepository;
import com.example.foundation.repository.ZoneRepository;

class RouteServiceTest {
    @Mock
    private RouteRepository routeRepository;
    @Mock
    private ZoneRepository zoneRepository;
    @Mock
    private TruckRepository truckRepository;

    @InjectMocks
    private RouteService routeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllRoutes() {
        Route route1 = mock(Route.class);
        Route route2 = mock(Route.class);
        when(routeRepository.findAll()).thenReturn(List.of(route1, route2));
        List<Route> result = routeService.getAllRoutes();
        assertEquals(2, result.size());
    }

    @Test
    void testGetRouteById() {
        Route route = mock(Route.class);
        when(routeRepository.findById("id1")).thenReturn(Optional.of(route));
        Optional<Route> result = routeService.getRouteById("id1");
        assertTrue(result.isPresent());
        assertEquals(route, result.get());
    }

    @Test
    void testCreateRoute_WithZone() {
        Route route = mock(Route.class);
        Zone zone = mock(Zone.class);
        when(route.getZone()).thenReturn(zone);
        when(zone.getId()).thenReturn("zone1");
        when(zoneRepository.findById("zone1")).thenReturn(Optional.of(zone));
        when(routeRepository.save(route)).thenReturn(route);
        Route result = routeService.createRoute(route);
        verify(route).setZone(zone);
        assertEquals(route, result);
    }

    @Test
    void testCreateRoute_ZoneNotFound() {
        Route route = mock(Route.class);
        Zone zone = mock(Zone.class);
        when(route.getZone()).thenReturn(zone);
        when(zone.getId()).thenReturn("zone1");
        when(zoneRepository.findById("zone1")).thenReturn(Optional.empty());
        RuntimeException ex = assertThrows(RuntimeException.class, () -> routeService.createRoute(route));
        assertEquals("Zone not found", ex.getMessage());
    }

    @Test
    void testCreateRoute_WithoutZone() {
        Route route = mock(Route.class);
        when(route.getZone()).thenReturn(null);
        when(routeRepository.save(route)).thenReturn(route);
        Route result = routeService.createRoute(route);
        assertEquals(route, result);
    }

    @Test
    void testUpdateRoute_Success() {
        Route route = mock(Route.class);
        Route updatedRoute = mock(Route.class);
        when(routeRepository.findById("id1")).thenReturn(Optional.of(route));
        when(updatedRoute.getRouteName()).thenReturn("NewName");
        when(updatedRoute.isActive()).thenReturn(true);
        when(updatedRoute.getZone()).thenReturn(null);
        when(routeRepository.save(route)).thenReturn(route);
        Route result = routeService.updateRoute("id1", updatedRoute);
        verify(route).setRouteName("NewName");
        verify(route).setActive(true);
        assertEquals(route, result);
    }

    @Test
    void testUpdateRoute_RouteNotFound() {
        Route updatedRoute = mock(Route.class);
        when(routeRepository.findById("id1")).thenReturn(Optional.empty());
        RuntimeException ex = assertThrows(RuntimeException.class, () -> routeService.updateRoute("id1", updatedRoute));
        assertEquals("Route not found", ex.getMessage());
    }

    @Test
    void testUpdateRoute_ZoneNotFound() {
        Route route = mock(Route.class);
        Route updatedRoute = mock(Route.class);
        Zone zone = mock(Zone.class);
        when(routeRepository.findById("id1")).thenReturn(Optional.of(route));
        when(updatedRoute.getRouteName()).thenReturn("NewName");
        when(updatedRoute.isActive()).thenReturn(true);
        when(updatedRoute.getZone()).thenReturn(zone);
        when(zone.getId()).thenReturn("zone1");
        when(zoneRepository.findById("zone1")).thenReturn(Optional.empty());
        RuntimeException ex = assertThrows(RuntimeException.class, () -> routeService.updateRoute("id1", updatedRoute));
        assertEquals("Zone not found", ex.getMessage());
    }

    @Test
    void testDeleteRoute_Success() {
        Route route = mock(Route.class);
        when(routeRepository.findById("id1")).thenReturn(Optional.of(route));
        routeService.deleteRoute("id1");
        verify(routeRepository).delete(route);
    }

    @Test
    void testDeleteRoute_RouteNotFound() {
        when(routeRepository.findById("id1")).thenReturn(Optional.empty());
        RuntimeException ex = assertThrows(RuntimeException.class, () -> routeService.deleteRoute("id1"));
        assertEquals("Route not found", ex.getMessage());
    }

    @Test
    void testAssignTruck_Success() {
        Route route = mock(Route.class);
        Truck truck = mock(Truck.class);
        when(routeRepository.findById("route1")).thenReturn(Optional.of(route));
        when(truckRepository.findById("truck1")).thenReturn(Optional.of(truck));
        when(routeRepository.save(route)).thenReturn(route);
        when(truckRepository.save(truck)).thenReturn(truck);
        Route result = routeService.assignTruck("route1", "truck1");
        verify(route).addTruck(truck);
        verify(truck).setAssignedRoute(route);
        verify(truckRepository).save(truck);
        verify(routeRepository).save(route);
        assertEquals(route, result);
    }

    @Test
    void testAssignTruck_RouteNotFound() {
        when(routeRepository.findById("route1")).thenReturn(Optional.empty());
        RuntimeException ex = assertThrows(RuntimeException.class, () -> routeService.assignTruck("route1", "truck1"));
        assertEquals("Route not found", ex.getMessage());
    }

    @Test
    void testAssignTruck_TruckNotFound() {
        Route route = mock(Route.class);
        when(routeRepository.findById("route1")).thenReturn(Optional.of(route));
        when(truckRepository.findById("truck1")).thenReturn(Optional.empty());
        RuntimeException ex = assertThrows(RuntimeException.class, () -> routeService.assignTruck("route1", "truck1"));
        assertEquals("Truck not found", ex.getMessage());
    }

    @Test
    void testUnassignTruck_Success() {
        Route route = mock(Route.class);
        Truck truck = mock(Truck.class);
        when(routeRepository.findById("route1")).thenReturn(Optional.of(route));
        when(truckRepository.findById("truck1")).thenReturn(Optional.of(truck));
        when(routeRepository.save(route)).thenReturn(route);
        when(truckRepository.save(truck)).thenReturn(truck);
        Route result = routeService.unassignTruck("route1", "truck1");
        verify(route).removeTruck(truck);
        verify(truck).setAssignedRoute(null);
        verify(truckRepository).save(truck);
        verify(routeRepository).save(route);
        assertEquals(route, result);
    }

    @Test
    void testUnassignTruck_RouteNotFound() {
        when(routeRepository.findById("route1")).thenReturn(Optional.empty());
        RuntimeException ex = assertThrows(RuntimeException.class, () -> routeService.unassignTruck("route1", "truck1"));
        assertEquals("Route not found", ex.getMessage());
    }

    @Test
    void testUnassignTruck_TruckNotFound() {
        Route route = mock(Route.class);
        when(routeRepository.findById("route1")).thenReturn(Optional.of(route));
        when(truckRepository.findById("truck1")).thenReturn(Optional.empty());
        RuntimeException ex = assertThrows(RuntimeException.class, () -> routeService.unassignTruck("route1", "truck1"));
        assertEquals("Truck not found", ex.getMessage());
    }

    @Test
    void testGetActiveRoutes() {
        Route activeRoute = mock(Route.class);
        Route inactiveRoute = mock(Route.class);
        when(activeRoute.isActive()).thenReturn(true);
        when(inactiveRoute.isActive()).thenReturn(false);
        when(routeRepository.findAll()).thenReturn(List.of(activeRoute, inactiveRoute));
        List<Route> result = routeService.getActiveRoutes();
        assertEquals(1, result.size());
        assertTrue(result.contains(activeRoute));
    }
}
