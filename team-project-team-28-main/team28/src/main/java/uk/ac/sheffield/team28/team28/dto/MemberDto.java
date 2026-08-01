package uk.ac.sheffield.team28.team28.dto;

import uk.ac.sheffield.team28.team28.model.BandInPractice;

public class MemberDto {
    private String firstName;
    private String lastName;
    private Long id;
    private BandInPractice bandInPractice;

    public MemberDto(String firstName, String lastName, BandInPractice bandInPractice, Long id) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.id = id;
        this.bandInPractice = bandInPractice;
        
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BandInPractice getBandInPractice() {
        return bandInPractice;
    }

    public void setBandInPractice(BandInPractice bandInPractice) {
        this.bandInPractice = bandInPractice;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
