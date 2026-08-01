package uk.ac.sheffield.team28.team28.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.Objects;

public class InstrumentDto {

    private Long instrumentId;

    @NotBlank(message = "Instrument cannot be empty.")
    private String instrumentInput;
    @NotBlank(message = "Make cannot be blank.")
    private String make;
    @NotBlank(message = "Serial number cannot be blank.")
    private String serialNumber;
    private boolean inStorage;
    private String note;

    //Constructors
    public InstrumentDto(){}

    public InstrumentDto(Long instrumentId, String instrumentInput, String make, String serialNumber, Boolean inStorage,
                         String note){
        this.instrumentId = instrumentId;
        this.instrumentInput = instrumentInput;
        this.make = make;
        this.serialNumber = serialNumber;
        this.inStorage = inStorage;
        this.note = note;

    }

    //Getters and setters


    public Long getInstrumentId() {
        return instrumentId;
    }

    public void setInstrumentId(Long instrumentId) {
        this.instrumentId = instrumentId;
    }

    public String getInstrumentInput() {
        return instrumentInput;
    }

    public void setInstrumentInput(String instrumentInput) {
        this.instrumentInput = instrumentInput;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public boolean getInStorage() {
        return inStorage;
    }

    public void setInStorage(Boolean inStorage) {
        this.inStorage = inStorage;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InstrumentDto that = (InstrumentDto) o;
        return getInStorage() == that.getInStorage() && Objects.equals(getInstrumentId(), that.getInstrumentId()) && Objects.equals(getInstrumentInput(), that.getInstrumentInput()) && Objects.equals(getMake(), that.getMake()) && Objects.equals(getSerialNumber(), that.getSerialNumber()) && Objects.equals(getNote(), that.getNote());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getInstrumentId(), getInstrumentInput(), getMake(), getSerialNumber(), getInStorage(), getNote());
    }
}
