package uk.ac.sheffield.team28.team28.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uk.ac.sheffield.team28.team28.model.BandInPractice;
import uk.ac.sheffield.team28.team28.model.Music;

import java.util.List;
import java.util.Optional;

public interface MusicRepository extends JpaRepository<Music, Long> {
    List<Music> findByBandInPracticeOrBandInPractice(BandInPractice band1, BandInPractice band2);

    @Query("SELECT m FROM Music m JOIN m.item i JOIN Order o ON o.item = i WHERE o.id = :orderId")
    Optional<Music> findByOrderId(@Param("orderId") Long orderId);
}
