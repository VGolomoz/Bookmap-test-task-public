package app.util;

public class CustomException extends Exception {

    private String messageError;


    public CustomException(String message) {
        this.messageError = message;
    }

    @Override
    public String getMessage() {
        return messageError;
    }
}
