package uk.ac.sheffield.team28.team28.model;

public enum MemberType {
    ADULT,
    NON_ADULT,
    COMMITTEE,
    DIRECTOR;
    
     @Override
     public String toString() {
         return switch (this) {
             case NON_ADULT -> "Non-adult";
             case ADULT -> "Adult";
             case COMMITTEE -> "Committee";
             case DIRECTOR -> "Director";
             default -> throw new IllegalArgumentException("Unexpected value: " + this);
         };
     }
    
    // static MemberType fromString(String s) {
    //     return switch (s) {
    //         case "Non-Adult" -> Non_Adult;
    //         case "Adult" -> Adult;
    //         case "Committee" ->  Committee;
    //         case "Director" -> Director;
    //         default -> throw new IllegalStateException("Unexpected value: " + s);
    //     };
    // }
}
