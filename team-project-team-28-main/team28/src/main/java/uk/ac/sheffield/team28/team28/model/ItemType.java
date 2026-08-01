package uk.ac.sheffield.team28.team28.model;

import jakarta.persistence.criteria.CriteriaBuilder;

public enum ItemType {
    Instrument,
    Music,
    Misc;

    @Override
    public String toString(){
        return switch (this){
            case Instrument -> "Instrument";
            case Misc -> "Misc";
            case Music -> "Music";
        };
    }

    static ItemType fromString(String s){
        return switch (s){
            case "Instrument" -> Instrument;
            case "Misc" -> Misc;
            case "Music" -> Music;
            default -> throw new IllegalStateException("Unexpected value: " + s);
        };
    }
}
