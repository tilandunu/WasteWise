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

import com.example.foundation.model.CrewMember;
import com.example.foundation.service.CrewMemberService;

class CrewMemberControllerTest {
    @Mock
    private CrewMemberService crewMemberService;
    @InjectMocks
    private CrewMemberController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllCrewMembers() {
        CrewMember crew1 = mock(CrewMember.class);
        CrewMember crew2 = mock(CrewMember.class);
        when(crewMemberService.getAllCrewMembers()).thenReturn(List.of(crew1, crew2));
        ResponseEntity<List<CrewMember>> response = controller.getAllCrewMembers();
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(List.of(crew1, crew2), response.getBody());
    }

    @Test
    void testGetAvailableCrew() {
        CrewMember crew1 = mock(CrewMember.class);
        when(crewMemberService.getAvailableCrewMembers()).thenReturn(List.of(crew1));
        ResponseEntity<List<CrewMember>> response = controller.getAvailableCrew();
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(List.of(crew1), response.getBody());
    }

    @Test
    void testUpdateAvailability() {
        CrewMember crew = mock(CrewMember.class);
        when(crewMemberService.updateAvailability("id1", true)).thenReturn(crew);
        ResponseEntity<CrewMember> response = controller.updateAvailability("id1", true);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(crew, response.getBody());
    }

    @Test
    void testAssignTruck() {
        CrewMember crew = mock(CrewMember.class);
        when(crewMemberService.assignTruck("id1", "truck1")).thenReturn(crew);
        ResponseEntity<CrewMember> response = controller.assignTruck("id1", "truck1");
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(crew, response.getBody());
    }

    @Test
    void testUnassignTruck() {
        CrewMember crew = mock(CrewMember.class);
        when(crewMemberService.unassignTruck("id1")).thenReturn(crew);
        ResponseEntity<CrewMember> response = controller.unassignTruck("id1");
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(crew, response.getBody());
    }
}
