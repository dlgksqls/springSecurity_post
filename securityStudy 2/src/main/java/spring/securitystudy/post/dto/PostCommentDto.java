package spring.securitystudy.post.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PostCommentDto {
    private Long id;
    private String username;
    private LocalDate createdDate;
    private String content;

    public PostCommentDto(Long id, String username, LocalDate createdDate, String content) {
        this.id = id;
        this.username = username;
        this.createdDate = createdDate;
        this.content = content;
    }
}
