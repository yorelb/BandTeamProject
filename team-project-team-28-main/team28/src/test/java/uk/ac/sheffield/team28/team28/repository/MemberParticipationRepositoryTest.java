package uk.ac.sheffield.team28.team28.repository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import uk.ac.sheffield.team28.team28.Team28Application;
import uk.ac.sheffield.team28.team28.model.ChildMember;
import uk.ac.sheffield.team28.team28.model.Member;
import uk.ac.sheffield.team28.team28.model.MemberType;
import uk.ac.sheffield.team28.team28.model.BandInPractice;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import uk.ac.sheffield.team28.team28.dto.InstrumentDto;
import uk.ac.sheffield.team28.team28.model.*;
import uk.ac.sheffield.team28.team28.repository.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertTrue;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = Team28Application.class)

public class MemberParticipationRepositoryTest {


    @Autowired
    private MemberParticipationRepository memberParticipationRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PerformanceRepository performanceRepository;

    private Performance performance;
    private Member member;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        performance = new Performance("First","O2",BandInPractice.Senior,LocalDateTime.now());
        performance.setId(30L);
        member = new Member(29L, "a@z.com", "password", MemberType.ADULT, "090378734", "John", "Doe");
        performance = performanceRepository.save(performance);
        member = memberRepository.save(member);
    }

    @Test
    void testFindByPerformanceIdAndMemberId_WhenFound() {
        Optional<MemberParticipation> result = memberParticipationRepository.findByPerformanceIdAndMemberId(performance.getId(), member.getId());
        System.out.println("HEEEEEEEEEEEEEEEEERE " + memberParticipationRepository.findByPerformanceIdAndMemberId(performance.getId(), member.getId()));
        // Check result
        assertTrue(result.isPresent());
        assertEquals(performance.getId(), result.get().getPerformance().getId());
        assertEquals(member.getId(), result.get().getMember().getId());
    }

    @Test
    void testFindByPerformanceIdAndMemberId_NotFound() {
        Optional<MemberParticipation> result = memberParticipationRepository.findByPerformanceIdAndMemberId(999L, 999L);
        // Cehck result
        assertFalse(result.isPresent());
    }
}
