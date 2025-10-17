package com.example.foundation.controller;

import com.example.foundation.model.CrewMember;
import com.example.foundation.service.CrewMemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/crew")
public class CrewMemberController {

    // Dependencies
    private final CrewMemberService crewMemberService;

    // Constructor
    public CrewMemberController(CrewMemberService crewMemberService) {
        this.crewMemberService = crewMemberService;
    }

    // 🔹 Get all crew members
    @GetMapping("/crew-members")
    public ResponseEntity<List<CrewMember>> getAllCrewMembers() {
        List<CrewMember> crewList = crewMemberService.getAllCrewMembers();
        return ResponseEntity.ok(crewList);
    }

    // 🔹 Get all available crew members
    @GetMapping("/available")
    public ResponseEntity<List<CrewMember>> getAvailableCrew() {
        List<CrewMember> availableCrew = crewMemberService.getAvailableCrewMembers();
        return ResponseEntity.ok(availableCrew);
    }

    // 🔹 Update availability
    @PutMapping("/{crewId}/availability")
    public ResponseEntity<CrewMember> updateAvailability(
            @PathVariable String crewId,
            @RequestParam boolean available) {

        CrewMember updatedCrew = crewMemberService.updateAvailability(crewId, available);
        return ResponseEntity.ok(updatedCrew);
    }

    // 🔹 Assign truck to crew member
    @PutMapping("/{crewId}/assign-truck/{truckId}")
    public ResponseEntity<CrewMember> assignTruck(
            @PathVariable String crewId,
            @PathVariable String truckId) {

        CrewMember updatedCrew = crewMemberService.assignTruck(crewId, truckId);
        return ResponseEntity.ok(updatedCrew);
    }

    // 🔹 Unassign truck
    @PutMapping("/{crewId}/unassign-truck")
    public ResponseEntity<CrewMember> unassignTruck(@PathVariable String crewId) {
        CrewMember updatedCrew = crewMemberService.unassignTruck(crewId);
        return ResponseEntity.ok(updatedCrew);
    }

}
