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
import uk.ac.sheffield.team28.team28.model.*;
import uk.ac.sheffield.team28.team28.repository.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
public class RequestServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private RequestRepository requestRepository;

    @Mock
    private PerformanceRepository performanceRepository;

    @Mock
    private MemberParticipationRepository memberParticipationRepository;
    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private RequestService requestService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void testAddRequest_WhenItsAValidRequest() {
        // Make requast and the requester
        Member requester = new Member(29L, "a@z.com", "password", "4553656352", "Jon", "Doe");
        Request request = new Request(requester, false, requester, requester);
        //Mock the response
        when(requestRepository.save(request)).thenReturn(request);
        // Get the result
        boolean result = requestService.addRequest(request);
        // Check everything went well
        assertTrue(result);
        verify(requestRepository, times(1)).save(request);
    }

    @Test
    void testAddRequest_WhenItsAnInvalidRequest() {
        // Make a request
        Request invalidRequest = new Request();
        // Get the result
        boolean result = requestService.addRequest(invalidRequest);
        // Check the result
        assertFalse(result);
        verify(requestRepository, never()).save(any(Request.class));
    }

    @Test
    void testGetRequestWithId_WhenTheIdIsValid() {
        // Make a request
        Request request = new Request();
        request.setId(1L);
        //Mock response
        when(requestRepository.findById(1L)).thenReturn(Optional.of(request));
        // Get the result
        Request result = requestService.getRequestWithId(1L);
        // Check the result
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(requestRepository, times(1)).findById(1L);
    }

    @Test
    void testGetRequestWithId_WhenTheIdIsInvalid() {
        // Mock repo response
        when(requestRepository.findById(29L)).thenReturn(Optional.empty());
        //Get the result
        Request result = requestService.getRequestWithId(29L);
        //Check the result
        assertNull(result);
        verify(requestRepository, times(1)).findById(29L);
    }

    @Test
    void testApproveRequest_WhenItsValid() {
        // Make request and requester
        Member requester = new Member(29L, "a@z.com", "password", "4553656352", "Jon", "Doe");
        Request request = new Request(requester, false, requester, requester);
        //Mock the response
        when(requestRepository.save(request)).thenReturn(request);
        // Get the result
        boolean result = requestService.approveRequest(request);
        // Check it
        assertTrue(result);
        assertTrue(request.isAccepted());
        verify(requestRepository, times(1)).save(request);
    }

    @Test
    void testApproveRequest_WhenItsInvalid() {
        // Make the request
        Request invalidRequest = new Request();
        // Get the result
        boolean result = requestService.approveRequest(invalidRequest);
        // Check the result
        assertFalse(result);
        verify(requestRepository, never()).save(any(Request.class));
    }

    @Test
    void testGetAllApprovedRequestWhereRequesterIs() {
        // Make the requester and request
        Member requester = new Member(29L, "a@z.com", "password", "4553656352", "Jon", "Doe");
        Request request = new Request(requester, true, requester, requester);
        //Mock the response
        when(requestRepository.findAllByRequesterAndAccepted(requester, true))
                .thenReturn(List.of(request));
        //Get the results
        List<Request> results = requestService.getAllApprovedRequestWhereRequesterIs(requester);
        // Check the results
        assertNotNull(results);
        assertEquals(1, results.size());
        assertTrue(results.get(0).isAccepted());
        verify(requestRepository, times(1))
                .findAllByRequesterAndAccepted(requester, true);
    }

    @Test
    void testDeleteRequest_WhenItsValid() {
        // Make the request and requester
        Member requester = new Member(29L, "a@z.com", "password", "4553656352", "Jon", "Doe");
        Request request = new Request(requester, true, requester, requester);
        //Mock the behaviour
        doNothing().when(requestRepository).delete(request); //doesnt actually delete
        // Get the result
        boolean result = requestService.deleteRequest(request);
        // Check the result
        assertTrue(result);
        verify(requestRepository, times(1)).delete(request);
    }

    @Test
    void testDeleteRequest_WhenItsInvalid() {
        // Make request
        Request invalidRequest = null;
        // Get result
        //Dont need do nothing since its invalid
        boolean result = requestService.deleteRequest(invalidRequest);
        // Check result
        assertFalse(result);
        verify(requestRepository, never()).delete(any(Request.class));
    }


}
