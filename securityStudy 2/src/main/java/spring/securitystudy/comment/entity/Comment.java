package spring.securitystudy.comment.entity;

import jakarta.persistence.*;
import lombok.Getter;
import spring.securitystudy.member.entity.Member;
import spring.securitystudy.post.entity.Post;

import java.time.LocalDate;

@Entity
@Getter
public class Comment {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;
    private LocalDate createdDate;
    private LocalDate updateDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "postId")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId")
    private Member member;

    public void create(Post findPost, String content, Member findMember) {
        this.content = content;
        this.createdDate = LocalDate.now();
        this.updateDate = null;

        this.post = findPost;
        this.member = findMember;
    }
}
