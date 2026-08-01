package uk.ac.sheffield.team28.team28.service;

import org.springframework.stereotype.Service;
import uk.ac.sheffield.team28.team28.dto.MiscDto;
import uk.ac.sheffield.team28.team28.model.Item;
import uk.ac.sheffield.team28.team28.model.Misc;
import uk.ac.sheffield.team28.team28.repository.ItemRepository;
import uk.ac.sheffield.team28.team28.repository.MiscRepository;
import uk.ac.sheffield.team28.team28.model.ItemType;

@Service
public class MiscService {

    private final MiscRepository miscRepository;
    private final ItemRepository itemRepository;

    public MiscService(MiscRepository miscRepository, ItemRepository itemRepository) {
        this.miscRepository = miscRepository;
        this.itemRepository = itemRepository;
    }

    public void saveMisc(MiscDto dto) {
        Item item = new Item();
        item.setItemType(ItemType.Misc);
        item.setNameTypeOrTitle(dto.getMiscName());
        item.setMakeOrComposer(dto.getMiscMake());
        item.setInStorage(dto.InStorage());

        Item savedItem = itemRepository.save(item);

        Misc misc = new Misc();
        misc.setMiscItem(savedItem);
       // System.out.println(dto.getMiscQuantity());
        misc.setMiscQuantity(dto.getMiscQuantity());

        miscRepository.save(misc);

    }
    public void updateMisc(MiscDto dto) {
        Long miscId = dto.getMiscId();
        Misc misc = miscRepository.getReferenceById(miscId);
        Item item = misc.getMiscItem();

        item.setItemType(ItemType.Instrument);
        item.setNameTypeOrTitle(dto.getMiscName());
        item.setMakeOrComposer(dto.getMiscMake());
        item.setInStorage(dto.InStorage());
        item.setNote(dto.getMiscNote());

        Item savedItem = itemRepository.save(item);
        misc.setMiscItem(savedItem);

        miscRepository.save(misc);
    }

}
