package uk.ac.sheffield.team28.team28.dto;

import jakarta.validation.constraints.NotBlank;
import uk.ac.sheffield.team28.team28.model.BandInPractice;

public class MusicDto {

    private Long musicId;

    @NotBlank(message = "Music name cannot be empty.")
    private String musicInput;

    @NotBlank(message = "Composer cannot be empty.")
    private String composer;

    private String arranger;

    private BandInPractice bandInPractice;

    private boolean suitableForTraining;

    //Constructor
    public MusicDto(){}

    public MusicDto(String musicInput, String composer, String arranger, BandInPractice bandInPractice,
                    Boolean suitableForTraining){
        this.musicInput = musicInput;
        this.composer = composer;
        this.arranger = arranger;
        this.bandInPractice = bandInPractice;
        this.suitableForTraining = suitableForTraining;
    }

    // Getters and setters

    public String getMusicInput() {
        return musicInput;
    }

    public Long getMusicId() {
        return musicId;
    }

    public BandInPractice getBandInPractice() {
        return bandInPractice;
    }

    public String getArranger() {
        return arranger;
    }

    public String getComposer() {
        return composer;
    }
    public boolean getSuitableForTraining(){
        return suitableForTraining;
    }

    public void setBandInPractice(BandInPractice bandInPractice) {
        this.bandInPractice = bandInPractice;
    }

    public void setArranger(String arranger) {
        this.arranger = arranger;
    }

    public void setComposer(String composer) {
        this.composer = composer;
    }

    public void setMusicInput(String musicInput) {
        this.musicInput = musicInput;
    }

    public void setSuitableForTraining(boolean suitableForTraining) {
        this.suitableForTraining = suitableForTraining;
    }
}
