package spring.securitystudy.post.entity;

import jakarta.persistence.*;
import lombok.Getter;
import spring.securitystudy.member.entity.Member;
import spring.securitystudy.post.dto.PostCreateDto;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
public class Post {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String content;
    private LocalDate createdDate;
    private LocalDate updatedDate;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "memberId")
    private Member member;

    public static Post create(PostCreateDto dto, Member loginUser) {
        Post post = new Post();
        post.title = dto.getTitle();
        post.content = dto.getContent();
        post.createdDate = LocalDate.now();
        post.updatedDate = null;
        post.member = loginUser;

        return post;
    }
}
