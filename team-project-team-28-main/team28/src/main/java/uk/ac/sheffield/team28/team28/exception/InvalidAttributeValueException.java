package uk.ac.sheffield.team28.team28.exception;

public class InvalidAttributeValueException extends Exception {

  public String message;

  public InvalidAttributeValueException(String message) {
        super(message);
        this.message = message;
  }
}
