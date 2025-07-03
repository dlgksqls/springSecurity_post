package spring.securitystudy.comment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.securitystudy.comment.entity.Comment;
import spring.securitystudy.comment.repository.CommentRepository;
import spring.securitystudy.member.entity.Member;
import spring.securitystudy.member.repository.MemberRepository;
import spring.securitystudy.post.entity.Post;
import spring.securitystudy.post.repository.PostRepository;

import java.nio.file.AccessDeniedException;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentService {

    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    public void create(Long postId, String content, String username) {
        log.info("member find");
        Member loginMember = memberRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저는 존재하지 않습니다."));

        log.info("post find");
        Post findPost = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글은 존재하지 않습니다."));

        Comment comment = new Comment();
        comment.create(findPost, content, loginMember);

        findPost.addComment(comment);
        loginMember.addComment(comment);

        commentRepository.save(comment);
    }
    ㄴㄴ
    @Transactional
    public Long update(Long commentId, String content, String username) throws AccessDeniedException {
        Comment findComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글은 존재하지 않습니다."));

        if (!findComment.getMember().getUsername().equals(username)) {
            throw new AccessDeniedException("댓글 수정 권한이 없습니다.");
        }

        findComment.update(content);
        return findComment.getPost().getId();
    }
}
