package uk.ac.sheffield.team28.team28.repository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import uk.ac.sheffield.team28.team28.Team28Application;
import uk.ac.sheffield.team28.team28.model.*;


import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertTrue;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = Team28Application.class)

public class MusicRepositoryTest {

    @Autowired
    private MusicRepository musicRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ItemRepository itemRepository;

    private Music music1;
    private Music music2;
    private Order order;

    @BeforeEach
    void setUp() {
        music1 = new Music();
        music1.setId(1L);
        music1.setBandInPractice(BandInPractice.Senior);
        music1.setArranger("Me");
        musicRepository.save(music1);

        music2 = new Music();
        music2.setId(2L);
        music2.setBandInPractice(BandInPractice.Training);
        music2.setArranger("Me");
        musicRepository.save(music2);

        Item item = new Item();
        item.setId(1L);
        itemRepository.save(item);
        order = new Order();
        order.setItem(item);
        orderRepository.save(order);

        music1.setItem(item);
        musicRepository.save(music1);
    }

    @Test
    void testFindByBandInPracticeOrBandInPractice() {
        // When finding Music by BandInPractice
        List<Music> result = musicRepository.findByBandInPracticeOrBandInPractice(BandInPractice.Senior, BandInPractice.Training);

        // Then the result should contain the expected Music objects
        assertEquals(2, result.size());
        assertTrue(result.contains(music1));
        assertTrue(result.contains(music2));
    }

    @Test
    void testFindByOrderId_Found() {
        // When finding Music by OrderId
        Optional<Music> result = musicRepository.findByOrderId(order.getId());

        // Then the result should be present
        assertTrue(result.isPresent());
        assertEquals(music1.getId(), result.get().getId());
    }

    @Test
    void testFindByOrderId_NotFound() {
        // When searching for a non-existing OrderId
        Optional<Music> result = musicRepository.findByOrderId(999L);

        // Then the result should be empty
        assertFalse(result.isPresent());
    }
}
