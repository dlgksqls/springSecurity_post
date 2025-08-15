package spring.securitystudy.friendship.exception;

public class ReceiveUserNotFoundException extends RuntimeException {
    public ReceiveUserNotFoundException(String message) {
        super(message);
    }
}
