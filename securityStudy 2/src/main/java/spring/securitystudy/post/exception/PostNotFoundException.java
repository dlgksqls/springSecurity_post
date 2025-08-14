package spring.securitystudy.post.exception;

public class PostNotFoundException extends RuntimeException{
    public PostNotFoundException(String message) {
        super(message);
    }
}
