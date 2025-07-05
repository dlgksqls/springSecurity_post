package spring.securitystudy.post.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import spring.securitystudy.comment.dto.CommentDto;
import spring.securitystudy.image.dto.ImageUrlsDto;
import spring.securitystudy.post.entity.Post;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class PostCommentImageDto {
    private Long id;
    private String title;
    private String postContent;
    private String username;
    private LocalDate createdDate;
    private LocalDate updatedDate;
    private List<ImageUrlsDto> imageUrlsList;
    private List<CommentDto> commentList;

    public PostCommentImageDto(Post post){
        this.id = post.getId();
        this.title = post.getTitle();
        this.postContent = post.getContent();
        this.username = post.getUser().getUsername();
        this.createdDate = post.getCreatedDate();
        this.updatedDate = post.getUpdatedDate();
        this.imageUrlsList = new ArrayList<>();
        this.commentList = new ArrayList<>();
    }

    public PostCommentImageDto(Post post, List<ImageUrlsDto> imageUrlsList, List<CommentDto> commentList){
        this.id = post.getId();
        this.title = post.getTitle();
        this.postContent = post.getContent();
        this.username = post.getUser().getUsername();
        this.createdDate = post.getCreatedDate();
        this.updatedDate = post.getUpdatedDate();
        this.imageUrlsList = imageUrlsList;
        this.commentList = commentList;
    }
}
