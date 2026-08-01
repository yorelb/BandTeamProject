package uk.ac.sheffield.team28.team28.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Instrument")
public class Instrument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name= "Item_Id", nullable = false)
    private Item item;

    @Column(name = "Serial_number")
    private String serialNumber;

    //Getters and setters
    public Instrument() {}

    public Instrument(String serialNumber, Item item){
        this.serialNumber = serialNumber;
        this.item = item;
    }
    public Long getId() {
        return id;
    }

    public String getSerialNumber(){
        return serialNumber;
    }

    public Item getItem(){
        return item;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public void setItem(Item item) {
        this.item = item;
    }
}
