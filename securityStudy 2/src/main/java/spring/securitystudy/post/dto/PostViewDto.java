package spring.securitystudy.post.dto;

import lombok.Data;
import org.springframework.cglib.core.Local;
import spring.securitystudy.post.entity.Post;

import java.time.LocalDate;

@Data
public class PostViewDto {
    private String title;
    private String content;
    private String writer;
    private LocalDate createdDate;

    public PostViewDto(Post post) {
        this.title = post.getTitle();
        this.content = post.getContent();
        this.writer = post.getMember().getUsername();
        this.createdDate = post.getCreatedDate();
    }
}
