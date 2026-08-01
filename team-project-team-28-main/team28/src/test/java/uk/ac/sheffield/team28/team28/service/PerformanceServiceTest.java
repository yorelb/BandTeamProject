package uk.ac.sheffield.team28.team28.service;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uk.ac.sheffield.team28.team28.dto.MemberRegistrationDto;
import uk.ac.sheffield.team28.team28.dto.MusicDto;
import uk.ac.sheffield.team28.team28.dto.OrderDto;
import uk.ac.sheffield.team28.team28.dto.PerformanceDto;
import uk.ac.sheffield.team28.team28.model.*;
import uk.ac.sheffield.team28.team28.repository.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PerformanceServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private MusicRepository musicRepository;

    @Mock
    private PerformanceRepository performanceRepository;

    @Mock
    private MemberParticipationRepository memberParticipationRepository;
    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private PerformanceDto performanceDto;


    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private PerformanceService performanceService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreatePerformance_withPlaylist() {
        // Fake the user input
        PerformanceDto performanceDto = new PerformanceDto();
        performanceDto.setName("Concert");
        performanceDto.setVenue("Stadium");
        performanceDto.setBand(BandInPractice.Senior.toString());
        performanceDto.setPlaylistIds(List.of(29L, 30L));

        Music music1 = new Music();
        music1.setId(29L);
        Music music2 = new Music();
        music2.setId(30L);

        // Mock the repo responses
        when(musicRepository.findAllById(List.of(29L, 30L))).thenReturn(List.of(music1, music2));
        when(memberRepository.findByBand(BandInPractice.Senior)).thenReturn(List.of(new Member()));
        when(memberRepository.findByBand(BandInPractice.Both)).thenReturn(List.of(new Member()));

        // Create the performance
        performanceService.createPerformance(performanceDto);

        // Verify save
        verify(performanceRepository, times(1)).save(any(Performance.class));
    }

    @Test
    void testCreatePerformance_withoutPlaylist() {
        // Fake user input without playlist
        PerformanceDto performanceDto = new PerformanceDto();
        performanceDto.setName("Concert");
        performanceDto.setVenue("Stadium");
        performanceDto.setBand("Senior");
        performanceDto.setPlaylistIds(null);
        // Mock the repo responses
        when(memberRepository.findByBand(BandInPractice.Senior)).thenReturn(List.of(new Member()));
        when(memberRepository.findByBand(BandInPractice.Both)).thenReturn(List.of(new Member()));
        // Create the performance
        performanceService.createPerformance(performanceDto);
        // Verify save
        verify(performanceRepository, times(1)).save(any(Performance.class));
    }

    @Test
    void getAllPerformances() {
        // Make performances
        Performance performance1 = new Performance();
        Performance performance2 = new Performance();
        // Mock the repo response
        when(performanceRepository.findAll()).thenReturn(List.of(performance1, performance2));
        // Get the performances
        List<Performance> performances = performanceService.getAllPerformances();
        // Check the result
        assertEquals(2, performances.size());
        verify(performanceRepository, times(1)).findAll();
    }

    @Test
    void testGetPlayersForPerformance() {
        // Make participants
        Member member = new Member();
        MemberParticipation participation = new MemberParticipation();
        participation.setMember(member);
        participation.setWillParticipate(true);
        // Make a performance
        Performance performance = new Performance();
        performance.setParticipations(List.of(participation));
        // Mock the repo response
        when(performanceRepository.findById(1L)).thenReturn(Optional.of(performance));
        // Get the performance players
        List<Member> players = performanceService.getPlayersForPerformance(1L);
        // Check the result
        assertEquals(1, players.size());
        verify(performanceRepository, times(1)).findById(1L);
    }

    @Test
    void testGetPerformanceById_success() {
        // Make performance
        Performance performance = new Performance();
        // Mock the repo response
        when(performanceRepository.findById(1L)).thenReturn(Optional.of(performance));
        // Get the result
        Performance result = performanceService.getPerformanceById(1L);
        // Check the result
        assertNotNull(result);
        verify(performanceRepository, times(1)).findById(1L);
    }

    @Test
    void testGetPerformanceById_doesntExist() {
        // Mock the repo response
        when(performanceRepository.findById(1L)).thenReturn(Optional.empty());
        // Get exception
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            performanceService.getPerformanceById(1L);
        });
        // Check it's right
        assertEquals("Performance not found with id: " + 1L, exception.getMessage());
    }

    @Test
    void testUpdatePerformance() {
        // Make a performance
        Performance performance = new Performance();
        performance.setVenue("Batcave");
        // Update it
        Performance updatedPerformance = new Performance();
        updatedPerformance.setVenue("Fortress of Solitude");
        // Mock the repo responses
        when(performanceRepository.findById(1L)).thenReturn(Optional.of(performance));
        when(performanceRepository.save(performance)).thenReturn(updatedPerformance);
        // Update the performance
        Performance result = performanceService.updatePerformance(1L, updatedPerformance);
        // Check result
        assertEquals("Fortress of Solitude", result.getVenue());
        verify(performanceRepository, times(1)).save(performance);
    }

    @Test
    void testDeletePerformance() {
        // Delete the performance
        performanceService.deletePerformance(1L);
        // Check it's deleted
        verify(performanceRepository, times(1)).deleteById(1L);
    }

    @Test
    void testOptOutOfPerformance() {
        // Make participation object
        MemberParticipation participation = new MemberParticipation();
        participation.setWillParticipate(true);

        // Mock the repo response
        when(memberParticipationRepository.findByPerformanceIdAndMemberId(1L, 1L))
                .thenReturn(Optional.of(participation));

        // Opt out of the performance
        performanceService.optOutOfPerformance(1L, 1L);

        // Verify the changes
        assertFalse(participation.isWillParticipate());
        verify(memberParticipationRepository, times(1)).save(participation);
    }

    @Test
    void testOptInToPerformance() {
        // Make participation object
        MemberParticipation participation = new MemberParticipation();
        participation.setWillParticipate(false);

        // Mock the repo response
        when(memberParticipationRepository.findByPerformanceIdAndMemberId(1L, 1L))
                .thenReturn(Optional.of(participation));

        // Opt into it performance
        performanceService.optInToPerformance(1L, 1L);

        // Verify changes
        assertTrue(participation.isWillParticipate());
        verify(memberParticipationRepository, times(1)).save(participation);
    }

}