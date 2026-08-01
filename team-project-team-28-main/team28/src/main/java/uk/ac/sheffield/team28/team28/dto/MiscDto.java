package uk.ac.sheffield.team28.team28.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class MiscDto {
    public long miscId;

    @NotBlank(message="Item name can not be left empty")
    private String miscName;
    @NotBlank(message="Item make can not be left blank")
    private String miscMake;
    @NotNull
    private Integer miscQuantity;
    @NotNull
    private boolean inStorage;

    private String miscNote;


    public MiscDto() {}
    public MiscDto(long miscId, String miscName, String miscMake, int quantity, boolean inStorage,String miscNote) {
        this.miscId = miscId;
        this.miscName = miscName;
        this.miscMake = miscMake;
        this.miscQuantity = quantity;
        this.inStorage = inStorage;
        this.miscNote = miscNote;


    }

    //Getters and Seetters
    public long getMiscId() {
        return miscId;
    }
    public void setMiscId(long miscId) {
        this.miscId = miscId;
    }
    public String getMiscName() {
        return miscName;
    }

    public String getMiscNote() {
        return miscNote;
    }

    public void setMiscName(String miscName) {
        this.miscName = miscName;
    }
    public String getMiscMake() {
        return miscMake;
    }
    public void setMiscMake(String miscMake) {
        this.miscMake = miscMake;
    }
    public int getMiscQuantity() {
        return miscQuantity;
    }
    public int setQuantity(int quantity) {
        this.miscQuantity = quantity;
        return quantity;
    }

    public boolean InStorage() {
        return inStorage;
    }

    public void setMiscNote(String miscNote) {
        this.miscNote = miscNote;
    }


}
