package spring.securitystudy.post.dto;

import lombok.Data;
import spring.securitystudy.post.entity.Post;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class PostViewDto {
    private Long id;
    private String title;
    private String content;
    private boolean isFriend;
    private String writer;
    private List<String> imageUrls;
    private long likeCnt;
    private boolean likeByLoginUser;
    private LocalDate createdDate;

    public PostViewDto(Long id,
                       String title,
                       String content,
                       String writer,
                       boolean isFriend,
                       LocalDate createdDate) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.writer = writer;
        this.isFriend = isFriend;
        this.createdDate = createdDate;
    }

    public PostViewDto(Post post, List<String> imageUrls, long likeCnt, boolean isFriend) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.writer = post.getUser().getUsername();
        this.isFriend = isFriend;
        this.imageUrls = imageUrls;
        this.likeCnt = likeCnt;
        this.createdDate = post.getCreatedDate();
    }
}
