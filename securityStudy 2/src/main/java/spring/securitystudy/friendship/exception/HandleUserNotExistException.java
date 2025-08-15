package spring.securitystudy.friendship.exception;

public class HandleUserNotExistException extends RuntimeException{
    public HandleUserNotExistException(String message) {
        super(message);
    }
}
