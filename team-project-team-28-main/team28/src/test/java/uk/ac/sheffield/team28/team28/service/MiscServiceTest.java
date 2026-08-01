package uk.ac.sheffield.team28.team28.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import uk.ac.sheffield.team28.team28.dto.MiscDto;
import uk.ac.sheffield.team28.team28.model.Item;
import uk.ac.sheffield.team28.team28.model.Misc;
import uk.ac.sheffield.team28.team28.repository.ItemRepository;
import uk.ac.sheffield.team28.team28.repository.MiscRepository;
import uk.ac.sheffield.team28.team28.model.ItemType;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class MiscServiceTest {

    @Mock
    private MiscRepository miscRepository;

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private MiscService miscService;

    private MiscDto miscDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        miscDto = new MiscDto();
        miscDto.setMiscName("Guitar");
        miscDto.setMiscMake("Peugot");
        miscDto.setMiscId(29L);
        miscDto.setMiscNote("Green umbrella");
        miscDto.setQuantity(8008);
    }

    @Test
    void testSaveMisc() {
        // Make the misc item
        Item item = new Item();
        item.setItemType(ItemType.Misc);
        when(itemRepository.save(any(Item.class))).thenReturn(item);
        Misc misc = new Misc();
        when(miscRepository.save(any(Misc.class))).thenReturn(misc);
        // Save it
        miscService.saveMisc(miscDto);
        // Check its saved
        verify(itemRepository, times(1)).save(any(Item.class));
        verify(miscRepository, times(1)).save(any(Misc.class));
        assertNotNull(miscDto);
    }

    @Test
    void testUpdateMisc() {
        // Make the misc item
        Item item = new Item();
        item.setItemType(ItemType.Instrument);
        when(itemRepository.save(any(Item.class))).thenReturn(item);
        Misc misc = new Misc();
        misc.setMiscItem(item);
        //mock the response
        when(miscRepository.getReferenceById(anyLong())).thenReturn(misc);
        when(miscRepository.save(any(Misc.class))).thenReturn(misc);
        // Update the item
        miscService.updateMisc(miscDto);
        // Check it was updated
        verify(itemRepository, times(1)).save(any(Item.class));
        verify(miscRepository, times(1)).save(any(Misc.class));
        verify(miscRepository, times(1)).getReferenceById(anyLong());
        assertEquals(ItemType.Instrument, item.getItemType());
        assertEquals(miscDto.getMiscName(), item.getNameTypeOrTitle());
        assertEquals(miscDto.getMiscMake(), item.getMakeOrComposer());
    }
}
