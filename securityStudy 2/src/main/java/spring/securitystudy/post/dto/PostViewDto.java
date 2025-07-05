package spring.securitystudy.post.dto;

import lombok.Data;
import spring.securitystudy.post.entity.Post;

import java.time.LocalDate;
import java.util.List;

@Data
public class PostViewDto {
    private Long id;
    private String title;
    private String content;
    private boolean isFriend;
    private String writer;
    private List<String> imageUrls;
    private LocalDate createdDate;

    public PostViewDto(Long id, String title, String content, String writer, boolean isFriend, LocalDate createdDate) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.writer = writer;
        this.isFriend = isFriend;
        this.createdDate = createdDate;
    }

    public PostViewDto(Post post, List<String> imageUrls, boolean isFriend) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.writer = post.getUser().getUsername();
        this.isFriend = isFriend;
        this.imageUrls = imageUrls;
        this.createdDate = post.getCreatedDate();
    }
}
