package uk.ac.sheffield.team28.team28.exception;

public class FieldCannotBeBlankException extends Exception {

    public String message;

    public FieldCannotBeBlankException(String firstNameCannotBeEmpty) {
        super(firstNameCannotBeEmpty);
        this.message = this.getMessage();
    }
}
