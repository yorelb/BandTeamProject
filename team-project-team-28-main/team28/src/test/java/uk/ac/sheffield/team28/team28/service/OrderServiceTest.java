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

public class OrderServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private MusicRepository musicRepository;

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private OrderService orderService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testChangeOrderStatus_Success() throws Exception {
        // Make an order
        Order order = new Order();
        order.setIsFulfilled(false);
        //Mock the repo response
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        //Change order status
        orderService.changeOrderStatus(1L);
        //Check the order status is changed
        assertTrue(order.getIsFulfilled());
        verify(orderRepository, times(1)).findById(1L);
    }

    @Test
    public void testChangeOrderStatus_OrderDoesntExist() {
        Long orderId = 1L;
        //Mock repo response
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());
        // Get the exception
        Exception exception = assertThrows(Exception.class, () -> {
            orderService.changeOrderStatus(orderId);
        });
        //Check the exception
        assertEquals("Order not found with ID: " + orderId, exception.getMessage());
    }

    @Test
    public void testSaveOrder() {
        // Fake the user input
        //Order order = new Order();
        Member member = new Member();
        Item item = new Item();

        OrderDto orderDto = new OrderDto();
        orderDto.setMember(member);
        orderDto.setItem(item);
        orderDto.setOrderDate( LocalDate.of(2016, 9, 23));
        orderDto.setNote("Urgent");
        orderDto.setFulfilled(false);

        // Act
        orderService.save(orderDto);

        // Assert
        Order order = new Order();
        order.setMember(member);
        order.setItem(item);
        order.setOrderDate( LocalDate.of(2016, 9, 23));
        order.setNote("Urgent");
        order.setIsFulfilled(false);
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    public void testGetArrangerForOrder_TheyExist() throws Exception {
        // Make an order
        Music music = new Music(new Item(),"Bruce Wayne", BandInPractice.Senior);
        //Mock the repo response
        when(musicRepository.findByOrderId(1L)).thenReturn(Optional.of(music));
        //Get the arranger
        String result = orderService.getArrangerForOrder(1L);
        //Check the result
        assertEquals("Bruce Wayne", result);
        verify(musicRepository, times(1)).findByOrderId(1L);
    }

    @Test
    public void testGetArrangerForOrder_WhenTheyDontExist() {
        Long orderId = 1L;
        //Mock repo response
        when(musicRepository.findByOrderId(orderId)).thenReturn(Optional.empty());
        //Get the exception
        Exception exception = assertThrows(Exception.class, () -> {
            orderService.getArrangerForOrder(orderId);
        });
        //Check its right
        assertEquals("Order not found with ID: " + orderId, exception.getMessage());
    }

    @Test
    public void testMockInjection() {
        assertNotNull(orderService, "OrderService should not be null");
        assertNotNull(musicRepository, "MusicRepository should not be null");
    }
}
