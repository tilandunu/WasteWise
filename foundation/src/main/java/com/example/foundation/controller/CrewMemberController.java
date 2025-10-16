package com.example.foundation.controller;

import com.example.foundation.model.CrewMember;
import com.example.foundation.service.CrewMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/crew")
public class CrewMemberController {

    @Autowired
    private CrewMemberService crewService;

    // --- Get all crew members ---
    @GetMapping
    public List<CrewMember> getAllCrew() {
        return crewService.getAllCrew();
    }

    // --- Get crew by ID ---
    @GetMapping("/{id}")
    public CrewMember getCrewById(@PathVariable String id) {
        return crewService.getCrewById(id)
                .orElseThrow(() -> new RuntimeException("Crew member not found"));
    }

    // --- Create new crew member ---
    @PostMapping("/create")
    public CrewMember createCrew(@RequestBody CrewMember crew) {
        return crewService.createCrew(crew);
    }

    // --- Update existing crew member ---
    @PutMapping("/{id}")
    public CrewMember updateCrew(@PathVariable String id, @RequestBody CrewMember updatedCrew) {
        return crewService.updateCrew(id, updatedCrew);
    }

    // --- Delete crew member ---
    @DeleteMapping("/{id}")
    public void deleteCrew(@PathVariable String id) {
        crewService.deleteCrew(id);
    }

    // --- Assign truck to crew ---
    @PostMapping("/{crewId}/assign-truck/{truckId}")
    public CrewMember assignTruck(@PathVariable String crewId, @PathVariable String truckId) {
        return crewService.assignTruck(crewId, truckId);
    }

    // --- Unassign truck from crew ---
    @PostMapping("/{crewId}/unassign-truck")
    public CrewMember unassignTruck(@PathVariable String crewId) {
        return crewService.unassignTruck(crewId);
    }

    // --- Get available crew members ---
    @GetMapping("/available")
    public List<CrewMember> getAvailableCrew() {
        return crewService.getAvailableCrew();
    }
}
