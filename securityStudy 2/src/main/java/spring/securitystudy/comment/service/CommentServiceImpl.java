package spring.securitystudy.comment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.securitystudy.comment.entity.Comment;
import spring.securitystudy.comment.repository.CommentRepository;
import spring.securitystudy.user.entity.User;
import spring.securitystudy.user.repository.UserRepository;
import spring.securitystudy.post.entity.Post;
import spring.securitystudy.post.repository.PostRepository;

import java.nio.file.AccessDeniedException;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentServiceImpl implements CommentService{

    private final CommentRepository commentRepository;
    private final UserRepository memberRepository;
    private final PostRepository postRepository;

    @Override
    public void create(Long postId, String content, String username) {
        log.info("member find");
        User loginUser = memberRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저는 존재하지 않습니다."));

        log.info("post find");
        Post findPost = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글은 존재하지 않습니다."));

        Comment comment = new Comment();
        comment.create(findPost, content, loginUser);

        findPost.addComment(comment);
        loginUser.addComment(comment);

        commentRepository.save(comment);
    }

    @Override
    @Transactional
    public Long update(Long commentId, String content, String username) throws AccessDeniedException {
        Comment findComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글은 존재하지 않습니다."));

        if (!findComment.getUser().getUsername().equals(username)) {
            throw new AccessDeniedException("댓글 수정 권한이 없습니다.");
        }

        findComment.update(content);
        return findComment.getPost().getId();
    }
}
