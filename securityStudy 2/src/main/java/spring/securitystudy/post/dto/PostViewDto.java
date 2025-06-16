package spring.securitystudy.post.dto;

import lombok.Data;
import org.springframework.cglib.core.Local;
import spring.securitystudy.post.entity.Post;

import java.time.LocalDate;

@Data
public class PostViewDto {
    private Long id;
    private String title;
    private String content;
    private boolean isFriend;
    private String writer;
    private LocalDate createdDate;

    public PostViewDto(Post post, boolean isFriend) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.writer = post.getMember().getUsername();
        this.isFriend = isFriend;
        this.createdDate = post.getCreatedDate();
    }
}
