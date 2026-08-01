package uk.ac.sheffield.team28.team28.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import uk.ac.sheffield.team28.team28.dto.InstrumentDto;
import uk.ac.sheffield.team28.team28.model.*;
import uk.ac.sheffield.team28.team28.repository.*;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ItemServiceTest {

    @Mock
    private InstrumentRepository instrumentRepository;
    @Mock
    private InstrumentDto instrumentDto;

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private ItemService itemService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindById_whenSuccess () {
        //Make an item
        Item item = new Item(ItemType.Instrument, "Item", "me", "Makes the best music tbh");
        //Mock the repo response
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        //Find the item
        Item result = itemService.findById(1L);
        //Check the result
        assertEquals(result,item);
        verify(itemRepository, times(1)).findById(1L);

    }

    @Test
    void testFindById_whenItemDoesntExist () {
        //Mock the repo response
        when(itemRepository.findById(1L)).thenReturn(Optional.empty());
        //Get the exception
        Exception exception = assertThrows(Exception.class, () -> {
            itemService.findById(1L);
        });
        //Compare the exception
        assertEquals("Item not found.", exception.getMessage());
        verify(itemRepository, times(1)).findById(1L);

    }



}