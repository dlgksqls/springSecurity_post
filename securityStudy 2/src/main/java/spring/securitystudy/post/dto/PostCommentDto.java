package spring.securitystudy.post.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PostCommentDto {
    private String username;
    private LocalDate createdDate;
    private String content;

    public PostCommentDto(String username, LocalDate createdDate, String content) {
        this.username = username;
        this.createdDate = createdDate;
        this.content = content;
    }
}
