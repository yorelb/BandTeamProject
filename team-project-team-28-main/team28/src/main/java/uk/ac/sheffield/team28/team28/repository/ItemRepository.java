package uk.ac.sheffield.team28.team28.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.ac.sheffield.team28.team28.model.Item;

public interface ItemRepository extends JpaRepository<Item, Long> {
}
