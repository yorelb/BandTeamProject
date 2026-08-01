package uk.ac.sheffield.team28.team28.model;

import jakarta.persistence.*;

@Entity
@Table(name = "performance_music")
public class PerformanceMusic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "performance_id", nullable = false)
    private Performance performance;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "music_id", nullable = false)
    private Music music;

    // Constructors
    public PerformanceMusic() {}

    public PerformanceMusic(Music music, Performance performance) {
        this.music = music;
        this.performance = performance;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public Performance getPerformance() {
        return performance;
    }

    public Music getMusic() {
        return music;
    }
}
