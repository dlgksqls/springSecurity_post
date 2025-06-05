package spring.securitystudy.post.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PostUpdateDto {
    private String title;
    private String content;
    private LocalDate updatedDate;

    public PostUpdateDto(String title, String content, LocalDate updatedDate) {
        this.title = title;
        this.content = content;
        this.updatedDate = updatedDate;
    }
}
