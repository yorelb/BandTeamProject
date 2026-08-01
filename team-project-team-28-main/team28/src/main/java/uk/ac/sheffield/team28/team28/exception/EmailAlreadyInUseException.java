package uk.ac.sheffield.team28.team28.exception;

public class EmailAlreadyInUseException extends Exception {

    public String message;

    public EmailAlreadyInUseException() {
        super("Email already in use");
        this.message = this.getMessage();

    }
}
