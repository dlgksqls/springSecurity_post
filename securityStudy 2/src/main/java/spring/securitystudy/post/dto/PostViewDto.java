package spring.securitystudy.post.dto;

import lombok.Data;
import org.springframework.cglib.core.Local;
import spring.securitystudy.image.entity.Image;
import spring.securitystudy.post.entity.Post;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class PostViewDto {
    private Long id;
    private String title;
    private String content;
    private boolean isFriend;
    private String writer;
    private List<String> imageUrls;
    private LocalDate createdDate;

    public PostViewDto(Post post, boolean isFriend) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.writer = post.getMember().getUsername();
        this.isFriend = isFriend;
        this.imageUrls = post.getImageList().stream()
                .map(img -> img.getUrl())
                .collect(Collectors.toList());
        this.createdDate = post.getCreatedDate();
    }
}
