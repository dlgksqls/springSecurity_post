package spring.securitystudy.user.exception;

public class FindUserNotExistException extends RuntimeException{
    public FindUserNotExistException(String message) {
        super("해당 유저를 찾을 수 없습니다.");
    }
}
