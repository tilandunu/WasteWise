package com.example.foundation.controller;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
import com.example.foundation.service.RouteService;

class RouteControllerTest {
    @Mock
    private RouteService routeService;
    @InjectMocks
    private RouteController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllRoutes() {
        Route route1 = mock(Route.class);
        Route route2 = mock(Route.class);
        when(routeService.getAllRoutes()).thenReturn(List.of(route1, route2));
        ResponseEntity<List<Route>> response = controller.getAllRoutes();
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(List.of(route1, route2), response.getBody());
    }

    @Test
    void testGetRouteById_Found() {
        Route route = mock(Route.class);
        when(routeService.getRouteById("id1")).thenReturn(Optional.of(route));
        ResponseEntity<?> response = controller.getRouteById("id1");
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(route, response.getBody());
    }

    @Test
    void testGetRouteById_NotFound() {
        when(routeService.getRouteById("id1")).thenReturn(Optional.empty());
        ResponseEntity<?> response = controller.getRouteById("id1");
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void testCreateRoute() {
        Route route = mock(Route.class);
        when(routeService.createRoute(route)).thenReturn(route);
        ResponseEntity<Route> response = controller.createRoute(route);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(route, response.getBody());
    }

    @Test
    void testUpdateRoute_Success() {
        Route route = mock(Route.class);
        when(routeService.updateRoute("id1", route)).thenReturn(route);
        ResponseEntity<?> response = controller.updateRoute("id1", route);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(route, response.getBody());
    }

    @Test
    void testUpdateRoute_NotFound() {
        Route route = mock(Route.class);
        when(routeService.updateRoute("id1", route)).thenThrow(new RuntimeException("Not found"));
        ResponseEntity<?> response = controller.updateRoute("id1", route);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void testDeleteRoute_Success() {
        doNothing().when(routeService).deleteRoute("id1");
        ResponseEntity<?> response = controller.deleteRoute("id1");
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Route deleted successfully", response.getBody());
    }

    @Test
    void testDeleteRoute_NotFound() {
        doThrow(new RuntimeException("Not found")).when(routeService).deleteRoute("id1");
        ResponseEntity<?> response = controller.deleteRoute("id1");
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void testAssignTruck() {
        Route route = mock(Route.class);
        when(routeService.assignTruck("route1", "truck1")).thenReturn(route);
        ResponseEntity<?> response = controller.assignTruck("route1", "truck1");
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(route, response.getBody());
    }

    @Test
    void testUnassignTruck() {
        Route route = mock(Route.class);
        when(routeService.unassignTruck("route1", "truck1")).thenReturn(route);
        ResponseEntity<?> response = controller.unassignTruck("route1", "truck1");
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(route, response.getBody());
    }

    @Test
    void testGetActiveRoutes() {
        Route route1 = mock(Route.class);
        Route route2 = mock(Route.class);
        when(routeService.getActiveRoutes()).thenReturn(List.of(route1, route2));
        ResponseEntity<List<Route>> response = controller.getActiveRoutes();
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(List.of(route1, route2), response.getBody());
    }
}
