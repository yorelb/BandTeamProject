package uk.ac.sheffield.team28.team28.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Music")
public class Music {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @Column(name = "arranger")
    private String arranger;

    @Column(name = "band_in_practice")
    private BandInPractice bandInPractice;

    //Getters and setters
    public Music(){}

    public Music(Item item, String arranger, BandInPractice bandInPractice){
        this.item = item;
        this.arranger = arranger;
        this.bandInPractice =bandInPractice;
    }

    public Long getId() {
        return id;
    }

    public String getArranger(){
        return arranger;
    }

    public BandInPractice getBandInPractice(){
        return bandInPractice;
    }

    public void setBandInPractice(BandInPractice bandInPractice) {
        this.bandInPractice = bandInPractice;
    }

    public void setArranger(String arranger) {
        this.arranger = arranger;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Item getItem() {
        return item;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
