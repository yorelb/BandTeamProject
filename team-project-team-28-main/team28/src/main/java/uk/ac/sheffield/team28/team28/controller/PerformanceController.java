package uk.ac.sheffield.team28.team28.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import uk.ac.sheffield.team28.team28.dto.MemberDto;
import uk.ac.sheffield.team28.team28.dto.MemberParticipationDto;
import uk.ac.sheffield.team28.team28.dto.ParticipationRequestDto;
import uk.ac.sheffield.team28.team28.dto.PerformanceDto;
import uk.ac.sheffield.team28.team28.model.Member;
import uk.ac.sheffield.team28.team28.model.MemberParticipation;
import uk.ac.sheffield.team28.team28.model.Performance;
import uk.ac.sheffield.team28.team28.service.MemberService;
import uk.ac.sheffield.team28.team28.service.PerformanceService;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/performances")
public class PerformanceController {

    private final PerformanceService performanceService;
    private final MemberService memberService;

    public PerformanceController(PerformanceService performanceService, MemberService memberService) {
        this.performanceService = performanceService;
        this.memberService = memberService;
    }

    // Create a new performance
    @PostMapping
    public ResponseEntity<String> createPerformance(@RequestBody PerformanceDto performanceDTO) {
        try {
            performanceService.createPerformance(performanceDTO);
            return ResponseEntity.ok("Performance created successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create performance.");
        }
}


    // Get all performances
    @GetMapping
    public List<Performance> getAllPerformances() {
        List<Performance> performances = performanceService.getAllPerformances();
        System.out.println("Performances: " + performances);
        return performances;
    }

    @GetMapping("/{id}/players")
    public ResponseEntity<List<Member>> getPlayersForPerformance(@PathVariable Long id) {
        List<Member> players = performanceService.getPlayersForPerformance(id);
        return ResponseEntity.ok(players);
    }

    // Get a specific performance by ID
    @GetMapping("/{id}")
    public ResponseEntity<Performance> getPerformanceById(@PathVariable Long id) {
        Performance performance = performanceService.getPerformanceById(id);
        return ResponseEntity.ok(performance);
    }

    // Update a performance
    @PutMapping("/{id}")
    public ResponseEntity<Performance> updatePerformance(@PathVariable Long id, @RequestBody Performance updatedPerformance) {
        Performance performance = performanceService.updatePerformance(id, updatedPerformance);
        return ResponseEntity.ok(performance);
    }

    // Delete a performance
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePerformance(@PathVariable Long id) {
        performanceService.deletePerformance(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{performanceId}/members")
    public ResponseEntity<List<MemberDto>> getMembersForPerformance(@PathVariable Long performanceId) {
        try {
            List<MemberDto> members = performanceService.getMembersForPerformance(performanceId);
            return ResponseEntity.ok(members);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
        }
    }

    @GetMapping("/participation")
    public ResponseEntity<List<MemberParticipationDto>> getMemberParticipations() {
        Long memberId = this.memberService.getAuthenticatedMemberId();
        System.out.println(memberId);
        List<MemberParticipationDto> participations = performanceService.getParticipationsForMember(memberId);
        return ResponseEntity.ok(participations);
    }

    @PostMapping("/participation/opt-out/{performanceId}")
    public ResponseEntity<String> optOutOfPerformance(@PathVariable Long performanceId) {
        try {
            Long memberId = this.memberService.getAuthenticatedMemberId();
            performanceService.optOutOfPerformance(performanceId, memberId);
            return ResponseEntity.ok("Opted out successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/participation/opt-in/{performanceId}")
    public ResponseEntity<String> optInToPerformance(@PathVariable Long performanceId) {
        Long memberId = this.memberService.getAuthenticatedMemberId();
        performanceService.optInToPerformance(performanceId, memberId);
        return ResponseEntity.ok("Opted in successfully.");
    }
}
