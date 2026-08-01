package uk.ac.sheffield.team28.team28.dto;

import java.time.LocalDateTime;

public class MemberParticipationDto {
    private Long performanceId;
    private String name;
    private String venue;
    private LocalDateTime dateTime;
    private boolean isParticipating;

    public MemberParticipationDto(Long performanceId, String name, String venue, LocalDateTime dateTime, boolean isParticipating) {
        this.performanceId = performanceId;
        this.name = name;
        this.venue = venue;
        this.dateTime = dateTime;
        this.isParticipating = isParticipating;
    }

    public Long getPerformanceId() {
        return performanceId;
    }

    public void setPerformanceId(Long performanceId) {
        this.performanceId = performanceId;
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

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public boolean isParticipating() {
        return isParticipating;
    }

    public void setParticipating(boolean participating) {
        isParticipating = participating;
    }
}

