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
import uk.ac.sheffield.team28.team28.model.*;
import uk.ac.sheffield.team28.team28.repository.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
public class MusicServiceTest {

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
    private MusicService musicService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveMusic_ShouldSaveItemAndMusic() {
        // Fake user input
        MusicDto musicDto = new MusicDto();
        musicDto.setMusicInput("Moonlight Sonata");
        musicDto.setComposer("Batman");
        musicDto.setSuitableForTraining(false);
        musicDto.setArranger("Bruce Wayne");
        musicDto.setBandInPractice(BandInPractice.Senior);
        Item enteredItem = new Item();
        when(itemRepository.save(any(Item.class))).thenReturn(enteredItem);
        // Save the music
        musicService.saveMusic(musicDto);
        verify(itemRepository, times(1)).save(any(Item.class));
        verify(musicRepository, times(1)).save(any(Music.class));
    }




}
