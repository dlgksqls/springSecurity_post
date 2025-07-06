package spring.securitystudy.comment.service;

import java.nio.file.AccessDeniedException;

public interface CommentService {

    /**
     * 댓글 달기
     * @param postId
     * @param content
     * @param username
     */
    void create(Long postId, String content, String username);

    /**
     * 댓글 수정
     * @param commentId
     * @param content
     * @param username
     * @return
     * @throws AccessDeniedException
     */
    Long update(Long commentId, String content, String username) throws AccessDeniedException;
}
