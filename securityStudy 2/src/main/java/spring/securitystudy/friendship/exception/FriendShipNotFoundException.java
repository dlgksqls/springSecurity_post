package spring.securitystudy.friendship.exception;

public class FriendShipNotFoundException extends RuntimeException{
    public FriendShipNotFoundException(String message) {
        super(message);
    }
}
