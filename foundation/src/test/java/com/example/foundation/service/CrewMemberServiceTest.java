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

import com.example.foundation.dto.request.RegisterUserRequest;
import com.example.foundation.model.CrewMember;
import com.example.foundation.model.Truck;
import com.example.foundation.model.Zone;
import com.example.foundation.repository.TruckRepository;
import com.example.foundation.repository.UserRepository;
import com.example.foundation.repository.ZoneRepository;

class CrewMemberServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private TruckRepository truckRepository;
    @Mock
    private ZoneRepository zoneRepository;

    @InjectMocks
    private CrewMemberService crewMemberService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllCrewMembers() {
        CrewMember crew1 = mock(CrewMember.class);
        CrewMember crew2 = mock(CrewMember.class);
        when(userRepository.findAll()).thenReturn(List.of(crew1, crew2));
        List<CrewMember> result = crewMemberService.getAllCrewMembers();
        assertEquals(2, result.size());
    }

    @Test
    void testGetAvailableCrewMembers() {
        CrewMember crew1 = mock(CrewMember.class);
        CrewMember crew2 = mock(CrewMember.class);
        when(crew1.isAvailable()).thenReturn(true);
        when(crew2.isAvailable()).thenReturn(false);
        when(userRepository.findAll()).thenReturn(List.of(crew1, crew2));
        List<CrewMember> result = crewMemberService.getAvailableCrewMembers();
        assertEquals(1, result.size());
        assertTrue(result.contains(crew1));
    }

    @Test
    void testUpdateAvailability_Success() {
        CrewMember crew = mock(CrewMember.class);
        when(userRepository.findById("id1")).thenReturn(Optional.of(crew));
        when(userRepository.save(crew)).thenReturn(crew);
        CrewMember result = crewMemberService.updateAvailability("id1", true);
        verify(crew).setAvailable(true);
        assertEquals(crew, result);
    }

    @Test
    void testUpdateAvailability_NotCrewMember() {
        // Use a mock User subclass that is not CrewMember
        class DummyUser extends com.example.foundation.model.User {
            public DummyUser() {
                super("dummy", "dummy", "dummy", "dummy");
            }
        }
        com.example.foundation.model.User user = mock(DummyUser.class);
        when(userRepository.findById("id1")).thenReturn(Optional.of(user));
        RuntimeException ex = assertThrows(RuntimeException.class, () -> crewMemberService.updateAvailability("id1", true));
        assertEquals("User is not a crew member", ex.getMessage());
    }

    @Test
    void testAssignTruck_Success() {
        CrewMember crew = mock(CrewMember.class);
        Truck truck = mock(Truck.class);
        when(userRepository.findById("crewId")).thenReturn(Optional.of(crew));
        when(truckRepository.findById("truckId")).thenReturn(Optional.of(truck));
        when(userRepository.save(crew)).thenReturn(crew);
        CrewMember result = crewMemberService.assignTruck("crewId", "truckId");
        verify(crew).setAssignedTruck(truck);
        verify(crew).setAvailable(false);
        assertEquals(crew, result);
    }

    @Test
    void testAssignTruck_NotCrewMember() {
        class DummyUser extends com.example.foundation.model.User {
            public DummyUser() {
                super("dummy", "dummy", "dummy", "dummy");
            }
        }
        com.example.foundation.model.User user = mock(DummyUser.class);
        when(userRepository.findById("crewId")).thenReturn(Optional.of(user));
        RuntimeException ex = assertThrows(RuntimeException.class, () -> crewMemberService.assignTruck("crewId", "truckId"));
        assertEquals("User is not a crew member", ex.getMessage());
    }

    @Test
    void testAssignTruck_TruckNotFound() {
        CrewMember crew = mock(CrewMember.class);
        when(userRepository.findById("crewId")).thenReturn(Optional.of(crew));
        when(truckRepository.findById("truckId")).thenReturn(Optional.empty());
        RuntimeException ex = assertThrows(RuntimeException.class, () -> crewMemberService.assignTruck("crewId", "truckId"));
        assertEquals("Truck not found", ex.getMessage());
    }

    @Test
    void testRegisterCrewMember_Success() {
        RegisterUserRequest req = mock(RegisterUserRequest.class);
        when(req.getUsername()).thenReturn("user1");
        when(req.getZoneId()).thenReturn("zone1");
        when(req.getPassword()).thenReturn("pass");
        when(req.getAddress()).thenReturn("address");
        when(req.getContactNumber()).thenReturn("123");
        when(userRepository.findByUsername("user1")).thenReturn(Optional.empty());
        Zone zone = mock(Zone.class);
        when(zoneRepository.findById("zone1")).thenReturn(Optional.of(zone));
        CrewMember savedCrew = mock(CrewMember.class);
        when(userRepository.save(any(CrewMember.class))).thenReturn(savedCrew);
        CrewMember result = crewMemberService.registerCrewMember(req);
        assertEquals(savedCrew, result);
    }

    @Test
    void testRegisterCrewMember_UsernameExists() {
        RegisterUserRequest req = mock(RegisterUserRequest.class);
        when(req.getUsername()).thenReturn("user1");
        when(userRepository.findByUsername("user1")).thenReturn(Optional.of(mock(CrewMember.class)));
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> crewMemberService.registerCrewMember(req));
        assertEquals("Username already exists", ex.getMessage());
    }

    @Test
    void testRegisterCrewMember_ZoneNotFound() {
        RegisterUserRequest req = mock(RegisterUserRequest.class);
        when(req.getUsername()).thenReturn("user1");
        when(req.getZoneId()).thenReturn("zone1");
        when(userRepository.findByUsername("user1")).thenReturn(Optional.empty());
        when(zoneRepository.findById("zone1")).thenReturn(Optional.empty());
        RuntimeException ex = assertThrows(RuntimeException.class, () -> crewMemberService.registerCrewMember(req));
        assertEquals("Zone not found", ex.getMessage());
    }

    @Test
    void testUnassignTruck_Success() {
        CrewMember crew = mock(CrewMember.class);
        when(userRepository.findById("crewId")).thenReturn(Optional.of(crew));
        when(userRepository.save(crew)).thenReturn(crew);
        CrewMember result = crewMemberService.unassignTruck("crewId");
        verify(crew).setAssignedTruck(null);
        verify(crew).setAvailable(true);
        assertEquals(crew, result);
    }

    @Test
    void testUnassignTruck_NotCrewMember() {
        class DummyUser extends com.example.foundation.model.User {
            public DummyUser() {
                super("dummy", "dummy", "dummy", "dummy");
            }
        }
        com.example.foundation.model.User user = mock(DummyUser.class);
        when(userRepository.findById("crewId")).thenReturn(Optional.of(user));
        RuntimeException ex = assertThrows(RuntimeException.class, () -> crewMemberService.unassignTruck("crewId"));
        assertEquals("User is not a crew member", ex.getMessage());
    }
}
