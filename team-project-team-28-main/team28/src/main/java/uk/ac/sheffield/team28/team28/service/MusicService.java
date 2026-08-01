package uk.ac.sheffield.team28.team28.service;

import java.util.List;

import org.springframework.stereotype.Service;
import uk.ac.sheffield.team28.team28.dto.MusicDto;
import uk.ac.sheffield.team28.team28.model.Item;
import uk.ac.sheffield.team28.team28.model.ItemType;
import uk.ac.sheffield.team28.team28.model.Music;
import uk.ac.sheffield.team28.team28.repository.ItemRepository;
import uk.ac.sheffield.team28.team28.repository.MusicRepository;

@Service
public class MusicService {

    private final MusicRepository musicRepository;
    private final ItemRepository itemRepository;

    public MusicService(MusicRepository musicRepository, ItemRepository itemRepository){
        this.musicRepository = musicRepository;
        this.itemRepository = itemRepository;
    }

    public List<Music> getAllMusicWithItemDetails() {
        return musicRepository.findAll();
    }

    public void saveMusic(MusicDto dto){
        //Create and save item
        Item item = new Item();
        item.setItemType(ItemType.Music);
        item.setNameTypeOrTitle(dto.getMusicInput());
        item.setMakeOrComposer(dto.getComposer());
        item.setNote(String.valueOf(dto.getSuitableForTraining()));

        Item savedItem = itemRepository.save(item);

        //Create and save music
        Music music = new Music();
        music.setArranger(dto.getArranger());
        music.setBandInPractice(dto.getBandInPractice());
        music.setItem(savedItem);

        musicRepository.save(music);
    }
}
