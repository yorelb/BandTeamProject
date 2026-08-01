package uk.ac.sheffield.team28.team28.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "performance")
public class Performance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "Name", nullable = false)
    private String name;

    @Column(name = "Venue", nullable = false)
    private String venue;

    @Enumerated(EnumType.STRING)
    @Column(name = "Band", nullable = false)
    private BandInPractice band;

    @Column(name = "DateTime", nullable = false)
    private LocalDateTime dateTime;

    @OneToMany(mappedBy = "performance", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<MemberParticipation> participations;

    @ManyToMany
    @JoinTable(
    name = "Performance_Playlist",
    joinColumns = @JoinColumn(name = "performance_id"),
    inverseJoinColumns = @JoinColumn(name = "music_id")
    )
    @JsonManagedReference
    private List<Music> playlist = new ArrayList<>();


    public Performance() {}

    public Performance(String name, String venue, BandInPractice band, LocalDateTime dateTime) {
        this.name = name;
        this.venue = venue;
        this.band = band;
        this.dateTime = dateTime;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public BandInPractice getBand() {
        return band;
    }

    public void setBand(BandInPractice band) {
        this.band = band;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public List<MemberParticipation> getParticipations() {
        return participations;
    }

    public void setParticipations(List<MemberParticipation> participations) {
        this.participations = participations;
    }

    public List<Music> getPlaylist() {
        return playlist;
    }

    public void setPlaylist(List<Music> playlist) {
        this.playlist = playlist;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
