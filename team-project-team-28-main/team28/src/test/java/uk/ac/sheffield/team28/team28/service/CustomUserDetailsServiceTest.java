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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uk.ac.sheffield.team28.team28.dto.MemberRegistrationDto;
import uk.ac.sheffield.team28.team28.model.BandInPractice;
import uk.ac.sheffield.team28.team28.model.ChildMember;
import uk.ac.sheffield.team28.team28.model.Member;
import uk.ac.sheffield.team28.team28.model.MemberType;
import uk.ac.sheffield.team28.team28.repository.ChildMemberRepository;
import uk.ac.sheffield.team28.team28.repository.LoanRepository;
import uk.ac.sheffield.team28.team28.repository.MemberRepository;
import uk.ac.sheffield.team28.team28.repository.OrderRepository;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Service
public class CustomUserDetailsServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private ChildMemberRepository childMemberRepository;

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;



    @BeforeEach
    public void setUp() {

        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLoadUserByUsername_Success() {
        //Create member
        Member member = new Member(29L, "a@z.com", "password", MemberType.ADULT, "090378734", "John", "Doe");
        //Mock the repo response
        when(memberRepository.findByEmail("a@z.com")).thenReturn(Optional.of(member));
        //Get the result of the megthod
        UserDetails userDetails = customUserDetailsService.loadUserByUsername("a@z.com");
        //Check the result against the member
        assertNotNull(userDetails);
        assertEquals("a@z.com", userDetails.getUsername());
        assertEquals("password", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_" + MemberType.ADULT.toString())));
    }

    @Test
    void testLoadUserByUsername_UserDoesntExist() {
        String email = "a@r.com";
        //Mock the repo response
        when(memberRepository.findByEmail(email)).thenReturn(Optional.empty());
        //Should throw an exeption
        assertThrows(UsernameNotFoundException.class, () -> {
            customUserDetailsService.loadUserByUsername(email);
        });
    }

}

