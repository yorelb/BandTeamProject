package uk.ac.sheffield.team28.team28.service;

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

public class InstrumentServiceTest {

    @Mock
    private InstrumentRepository instrumentRepository;
    @Mock
    private InstrumentDto instrumentDto;

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private InstrumentService instrumentService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveInstrument() {
        //Fake the user input
        InstrumentDto dto = new InstrumentDto();
        dto.setInstrumentInput("Harp");
        dto.setMake("Wayes");
        dto.setInStorage(true);
        dto.setNote("Is Green");
        dto.setSerialNumber("54243");
        Item savedItem = new Item();
        //Mock the repo response
        when(itemRepository.save(any(Item.class))).thenReturn(savedItem);
        //Save the instrument
        instrumentService.saveInstrument(dto);
        //Check it was saved
        verify(itemRepository, times(1)).save(any(Item.class));
        verify(instrumentRepository, times(1)).save(any(Instrument.class));
    }

    @Test
    void testUpdateInstrument() {
        //Fake the user input
        InstrumentDto dto = new InstrumentDto();
        dto.setInstrumentId(1L); //They wouldnt normally enter the id
        dto.setInstrumentInput("Harp");
        dto.setMake("Wayes");
        dto.setInStorage(true);
        dto.setNote("Is Green");
        dto.setSerialNumber("54243");
        //The already existing instrument
        Item existingItem = new Item(ItemType.Instrument, "Old", "Old", "Old");
        Instrument instrument = new Instrument("347364", existingItem);
        //Mock repo responses
        when(instrumentRepository.getReferenceById(1L)).thenReturn(instrument);
        when(itemRepository.save(any(Item.class))).thenReturn(existingItem);
        //Update the instrument
        instrumentService.updateInstrument(dto);
        //Check the right stuff were done
        verify(instrumentRepository, times(1)).getReferenceById(1L);
        verify(itemRepository, times(1)).save(existingItem);
        verify(instrumentRepository, times(1)).save(instrument);
        //Check the updated values
        assertEquals("Harp", instrument.getItem().getNameTypeOrTitle());
        assertEquals("Wayes", instrument.getItem().getMakeOrComposer());
        assertEquals(true, instrument.getItem().getInStorage());
        assertEquals("Is Green", instrument.getItem().getNote());
        assertEquals("54243", instrument.getSerialNumber());
    }


    @Test
    void testDeleteInstrument() {
        //The already exisitng instrument
        Instrument instrument = new Instrument();
        Item item = new Item();
        instrument.setItem(item);
        //Mock the repo response
        when(instrumentRepository.getReferenceById(1L)).thenReturn(instrument);
        //Delete the instrument
        instrumentService.deleteInstrument(1L);
        //Make sure its deleted
        verify(instrumentRepository, times(1)).getReferenceById(1L);
        verify(instrumentRepository, times(1)).delete(instrument);
        verify(itemRepository, times(1)).delete(item);
    }
}
