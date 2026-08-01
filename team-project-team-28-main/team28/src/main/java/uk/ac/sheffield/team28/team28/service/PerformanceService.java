package uk.ac.sheffield.team28.team28.service;

import org.springframework.stereotype.Service;

import uk.ac.sheffield.team28.team28.dto.MemberDto;
import uk.ac.sheffield.team28.team28.dto.MemberParticipationDto;
import uk.ac.sheffield.team28.team28.dto.PerformanceDto;
import uk.ac.sheffield.team28.team28.model.BandInPractice;
import uk.ac.sheffield.team28.team28.model.Member;
import uk.ac.sheffield.team28.team28.model.MemberParticipation;
import uk.ac.sheffield.team28.team28.model.Music;
import uk.ac.sheffield.team28.team28.model.Performance;
import uk.ac.sheffield.team28.team28.repository.MemberParticipationRepository;
import uk.ac.sheffield.team28.team28.repository.MemberRepository;
import uk.ac.sheffield.team28.team28.repository.MusicRepository;
import uk.ac.sheffield.team28.team28.repository.PerformanceRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class PerformanceService {

    private final PerformanceRepository performanceRepository;
    private final MemberParticipationRepository memberParticipationRepository;
    private final MemberRepository memberRepository;
     private final MusicRepository musicRepository;

    public PerformanceService(PerformanceRepository performanceRepository, MemberParticipationRepository memberParticipationRepository,  MemberRepository memberRepository, MusicRepository musicRepository) {
        this.performanceRepository = performanceRepository;
        this.memberParticipationRepository = memberParticipationRepository;
        this.memberRepository = memberRepository;
        this.musicRepository = musicRepository;
    }

    // Create a new performance
   public void createPerformance(PerformanceDto performanceDTO) {
    try {
        Performance performance = new Performance();
        performance.setName(performanceDTO.getName());
        performance.setVenue(performanceDTO.getVenue());
        performance.setBand(BandInPractice.valueOf(performanceDTO.getBand()));
        performance.setDateTime(performanceDTO.getDateTime());

        if (performanceDTO.getPlaylistIds() != null && !performanceDTO.getPlaylistIds().isEmpty()) {
            List<Music> playlist = musicRepository.findAllById(performanceDTO.getPlaylistIds());
            if (playlist.size() != performanceDTO.getPlaylistIds().size()) {
                throw new IllegalArgumentException("Invalid playlist IDs provided");
            }
            performance.setPlaylist(playlist);
        }

        List<Member> bandMembers;
        if (performance.getBand() == BandInPractice.Training) {
            //bandMembers = memberRepository.findByBand(BandInPractice.Training);
            bandMembers = new ArrayList<>(memberRepository.findByBand(BandInPractice.Training));

            bandMembers.addAll(memberRepository.findByBand(BandInPractice.Both));
        } else if (performance.getBand() == BandInPractice.Senior) {
            //bandMembers = memberRepository.findByBand(BandInPractice.Senior);

            bandMembers = new ArrayList<>(memberRepository.findByBand(BandInPractice.Senior));

            bandMembers.addAll(memberRepository.findByBand(BandInPractice.Both));

        } else if (performance.getBand() == BandInPractice.Both) {
           // bandMembers = memberRepository.findByBand(BandInPractice.Both);
            bandMembers = new ArrayList<>(memberRepository.findByBand(BandInPractice.Both));

            bandMembers.addAll(memberRepository.findByBand(BandInPractice.Senior));
            bandMembers.addAll(memberRepository.findByBand(BandInPractice.Training));
        } else {
            bandMembers = new ArrayList<>();
        }



        List<MemberParticipation> participations = new ArrayList<>();
        for (Member member : bandMembers) {
            participations.add(new MemberParticipation(performance, member, true)); // Default willParticipate = true
        }
        performance.setParticipations(participations);

        performanceRepository.save(performance);
    } catch (Exception e) {
        System.err.println("Error in createPerformance: " + e.getMessage());
        throw e;
    }
}
    // Get all performances
    public List<Performance> getAllPerformances() {
        return performanceRepository.findAll();
    }

    public List<Member> getPlayersForPerformance(Long performanceId) {
        return performanceRepository.findById(performanceId)
                .map(Performance::getParticipations)
                .orElseThrow(() -> new RuntimeException("Performance not found"))
                .stream()
                .filter(MemberParticipation::isWillParticipate)
                .map(MemberParticipation::getMember)
                .toList();
    }

    // Get a specific performance by ID
    public Performance getPerformanceById(Long id) {
        return performanceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Performance not found with id: " + id));
    }

    // Update a performance
    public Performance updatePerformance(Long id, Performance updatedPerformance) {
        Performance performance = getPerformanceById(id);

        performance.setVenue(updatedPerformance.getVenue());
        performance.setBand(updatedPerformance.getBand());
        performance.setDateTime(updatedPerformance.getDateTime());
        performance.setPlaylist(updatedPerformance.getPlaylist());

        return performanceRepository.save(performance);
    }

    // Delete a performance
    public void deletePerformance(Long id) {
        performanceRepository.deleteById(id);
    }

    public List<MemberDto> getMembersForPerformance(Long performanceId) {
        Performance performance = performanceRepository.findById(performanceId)
                .orElseThrow(() -> new NoSuchElementException("Performance not found"));

        return performance.getParticipations().stream()
                .filter(MemberParticipation::isWillParticipate)
                .map(participation -> {
                    Member member = participation.getMember();
                    return new MemberDto(member.getFirstName(), member.getLastName(), member.getBand(), member.getId());
                })
                .toList();
    }

    public List<MemberParticipationDto> getParticipationsForMember(Long memberId) {
        List<MemberParticipation> participations = memberParticipationRepository.findByMemberId(memberId);
        return participations.stream().map(p -> new MemberParticipationDto(
            p.getPerformance().getId(),
            p.getPerformance().getName(),
            p.getPerformance().getVenue(),
            p.getPerformance().getDateTime(),
            p.isWillParticipate()
        )).toList();
    }

    public void optOutOfPerformance(Long performanceId, Long memberId) {
        MemberParticipation participation = memberParticipationRepository
            .findByPerformanceIdAndMemberId(performanceId, memberId)
            .orElseThrow(() -> new IllegalArgumentException("Participation not found for the given performance and member."));

        participation.setWillParticipate(false);
        memberParticipationRepository.save(participation);
    }

    public void optInToPerformance(Long performanceId, Long memberId) {
        MemberParticipation participation = memberParticipationRepository
            .findByPerformanceIdAndMemberId(performanceId, memberId)
            .orElseThrow(() -> new IllegalArgumentException("Participation not found for the given performance and member."));
        
        participation.setWillParticipate(true);
        memberParticipationRepository.save(participation);
    }

    //Get performance based on band
    public List<Performance> getPerformanceByBand (BandInPractice band1, BandInPractice band2){
        return performanceRepository.getPerformancesByBandOrBand(band1, band2);
    }
    
}
