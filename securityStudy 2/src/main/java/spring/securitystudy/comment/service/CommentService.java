package spring.securitystudy.comment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import spring.securitystudy.comment.entity.Comment;
import spring.securitystudy.comment.repository.CommentRepository;
import spring.securitystudy.member.entity.Member;
import spring.securitystudy.member.repository.MemberRepository;
import spring.securitystudy.post.entity.Post;
import spring.securitystudy.post.repository.PostRepository;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    public void create(Long postId, String content, String username) {
        Post findPost = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글은 존재하지 않습니다."));

        Member findMember = memberRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("해당 멤버는 존재하지 않습니다."));

        Comment comment = new Comment();
        comment.create(findPost, content, findMember);

        findPost.addComment(comment);
        findMember.addComment(comment);

        commentRepository.save(comment);
    }
}
