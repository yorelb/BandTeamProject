package uk.ac.sheffield.team28.team28.service;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import uk.ac.sheffield.team28.team28.model.Item;
import uk.ac.sheffield.team28.team28.repository.ItemRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ItemService {
    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository){
        this.itemRepository = itemRepository;
    }

    @Transactional
    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    //Add Item methods here
    public Item findById(Long id){
        Optional<Item> item = itemRepository.findById(id);
        if (item.isPresent()){
            return item.get();

        } else {
            throw new IllegalArgumentException("Item not found.");
        }
    }
}
