package uk.ac.sheffield.team28.team28.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.ac.sheffield.team28.team28.dto.InstrumentDto;
import uk.ac.sheffield.team28.team28.model.Instrument;
import uk.ac.sheffield.team28.team28.model.Item;
import uk.ac.sheffield.team28.team28.model.ItemType;
import uk.ac.sheffield.team28.team28.model.Loan;
import uk.ac.sheffield.team28.team28.repository.InstrumentRepository;
import uk.ac.sheffield.team28.team28.repository.ItemRepository;
import uk.ac.sheffield.team28.team28.repository.LoanRepository;

import java.util.List;

@Service
public class InstrumentService {
    @Autowired
    private final InstrumentRepository instrumentRepository;

    @Autowired
    private final ItemRepository itemRepository;

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    public LoanService loanService;


    public InstrumentService(InstrumentRepository instrumentRepository, ItemRepository itemRepository){
        this.instrumentRepository = instrumentRepository;
        this.itemRepository = itemRepository;
    }

    public void saveInstrument(InstrumentDto dto){
        //Save instrument to item
        Item item = new Item();
        item.setItemType(ItemType.Instrument);
        item.setNameTypeOrTitle(dto.getInstrumentInput());
        item.setMakeOrComposer(dto.getMake());
        item.setInStorage(dto.getInStorage());
        item.setNote(dto.getNote());

        Item savedItem = itemRepository.save(item);

        //Save instrument to instrument
        Instrument instrument = new Instrument();
        instrument.setSerialNumber(dto.getSerialNumber());
        instrument.setItem(savedItem);

        instrumentRepository.save(instrument);
    }

    public void updateInstrument(InstrumentDto dto) {
        Long instrumentId = dto.getInstrumentId();
        Instrument instrument = instrumentRepository.getReferenceById(instrumentId);
        Item item = instrument.getItem();

        item.setItemType(ItemType.Instrument);
        item.setNameTypeOrTitle(dto.getInstrumentInput());
        item.setMakeOrComposer(dto.getMake());
        item.setInStorage(dto.getInStorage());
        item.setNote(dto.getNote());

        Item savedItem = itemRepository.save(item);
        instrument.setSerialNumber(dto.getSerialNumber());
        instrument.setItem(savedItem);

        instrumentRepository.save(instrument);
    }

    // Deletes an instrument by id but only if it cant find any active loans
    public void deleteInstrument(Long id) {
        Instrument instrument = instrumentRepository.getReferenceById(id);
        Item item = instrument.getItem();

        try {
            loanService.findActiveLoanByItemId(item.getId());
            throw new IllegalArgumentException("The item is currently on loan and cannot be deleted.");
        } catch (IllegalStateException e) {
            instrumentRepository.delete(instrument);
            itemRepository.delete(item);
        }
    }

}
