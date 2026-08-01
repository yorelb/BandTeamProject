package uk.ac.sheffield.team28.team28.model;

import jakarta.persistence.*;


@Entity
@Table(name = "Misc")
public class Misc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;
    @Column(name = "quantity")
    private Integer miscQuantity;

    //Getters and setters
    public Misc(){}

    public Misc(Item item, Integer miscQuantity) {
        this.item = item;
        this.miscQuantity = miscQuantity;
    }


    public Long getId() {
        return id;
    }

    public Item getMiscItem(){
        return item;
    }
    public void setMiscItem(Item item){
        this.item = item;

    }

    public String getMiscSerialNumber(String miscSerialNumber){
        return miscSerialNumber;
    }

    public Integer getMiscQuantity() {
        return miscQuantity;
    }

    public void setMiscQuantity(Integer miscQuantity){
        this.miscQuantity = miscQuantity;
    }

    }



