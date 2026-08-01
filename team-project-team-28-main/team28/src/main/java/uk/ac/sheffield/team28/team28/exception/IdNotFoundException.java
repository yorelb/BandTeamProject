package uk.ac.sheffield.team28.team28.exception;

public class IdNotFoundException extends Exception {

    public String message;

    public IdNotFoundException() {
        super("Id not found");
        this.message = this.getMessage();
    }
}
