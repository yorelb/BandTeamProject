package uk.ac.sheffield.team28.team28.model;

public enum BandInPractice {
    None,
    Training,
    Senior,
    Both;

    @Override
    public String toString(){
        return switch (this){
            case None -> "None";
            case Training ->  "Training";
            case Senior -> "Senior";
            case Both -> "Both";
        };
    }

    static BandInPractice fromString(String s){
        return switch (s){
            case "None" -> None;
            case "Training" -> Training;
            case "Senior" -> Senior;
            case "Both" -> Both;
            default -> throw new IllegalStateException("Unexpected value: " + s);
        };
    }
}
