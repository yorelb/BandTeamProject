package uk.ac.sheffield.team28.team28.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uk.ac.sheffield.team28.team28.model.ItemType;
import uk.ac.sheffield.team28.team28.model.Order;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Modifying
    @Query("DELETE FROM Order o WHERE o.member.id = :memberId")
    void deleteOrdersByMemberId(@Param("memberId") Long memberId);

    @Query("SELECT o FROM Order o WHERE o.item.itemType = :itemType AND o.isFulfilled = false")
    List<Order> findByItemTypeAndNotFulfilled(@Param("itemType") ItemType itemType);



}
