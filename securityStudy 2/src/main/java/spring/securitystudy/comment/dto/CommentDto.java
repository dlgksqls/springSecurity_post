package spring.securitystudy.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import spring.securitystudy.comment.entity.Comment;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class CommentDto {
    private Long id;
    private String content;
    private String username;
    private LocalDate createdDate;
    private LocalDate updatedDate;

    public CommentDto(Comment comment){
        this.id = comment.getId();
        this.content = comment.getContent();
        this.username = comment.getMember().getUsername();
        this.createdDate = comment.getCreatedDate();
        this.updatedDate = comment.getUpdateDate();
    }
}
