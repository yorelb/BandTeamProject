package uk.ac.sheffield.team28.team28.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uk.ac.sheffield.team28.team28.model.PerformanceMusic;

@Repository
public interface PerformanceMusicRepository extends JpaRepository<PerformanceMusic, Long> {
}
