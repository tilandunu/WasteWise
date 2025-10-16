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
