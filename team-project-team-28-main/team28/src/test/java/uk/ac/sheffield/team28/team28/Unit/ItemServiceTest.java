package uk.ac.sheffield.team28.team28.Unit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import uk.ac.sheffield.team28.team28.model.Item;
import uk.ac.sheffield.team28.team28.model.ItemType;
import uk.ac.sheffield.team28.team28.repository.ItemRepository;
import uk.ac.sheffield.team28.team28.service.ItemService;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class ItemServiceTest {

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private ItemService itemService;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void shouldReturnAllItemsWhenFindAllIsCalled(){
        Item item1 = new Item(ItemType.Misc, "Test", "Test", "Test");
        Item item2 = new Item(ItemType.Misc, "Test2", "Test2", "Test2");
        when(itemRepository.findAll()).thenReturn(Arrays.asList(item1, item2));

        List<Item> items = itemService.getAllItems();
        assertEquals(2, items.size());
        assertEquals("Test", items.get(0).getNameTypeOrTitle());
        assertEquals("Test2", items.get(1).getNameTypeOrTitle());
    }
}
