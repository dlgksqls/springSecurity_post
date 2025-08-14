package spring.securitystudy.post.exception;

public class PostDeniedException extends RuntimeException{
    public PostDeniedException(String message) {
        super(message);
    }
}
