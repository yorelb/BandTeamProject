package uk.ac.sheffield.team28.team28.model;

import jakarta.persistence.*;

@Entity
@Table(name="Item")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "item_type", nullable = false)
    private ItemType itemType;

    @Column(name = "name_type_or_title", nullable = false)
    private String nameTypeOrTitle;

    @Column(name = "make_or_Composer", nullable = true)
    private String makeOrComposer;

    @Column(name = "in_storage", nullable = true)
    private Boolean inStorage;

    @Column(name = "note", nullable = true)
    private String note;

    public Item(){}

    public Item(ItemType itemType, String nameTypeOrTitle, String makeOrComposer, String note){
        this.itemType = itemType;
        this.nameTypeOrTitle = nameTypeOrTitle;
        this.makeOrComposer = makeOrComposer;
        inStorage = true;
        this.note = note;
    }

    //Getters and setters
    public Long getId() {
        return id;
    }

    public ItemType getItemType(){
        return itemType;
    }

    public String getNameTypeOrTitle(){
        return nameTypeOrTitle;
    }

    public String getMakeOrComposer(){
        return makeOrComposer;
    }

    public Boolean getInStorage(){
        return inStorage;
    }

    public String getNote(){
        return note;
    }

    public void setItemType(ItemType itemType) {
        this.itemType = itemType;
    }

    public void setNameTypeOrTitle(String nameTypeOrTitle) {
        this.nameTypeOrTitle = nameTypeOrTitle;
    }

    public void setMakeOrComposer(String makeOrComposer) {
        this.makeOrComposer = makeOrComposer;
    }

    public void setInStorage(Boolean inStorage) {
        this.inStorage = inStorage;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
