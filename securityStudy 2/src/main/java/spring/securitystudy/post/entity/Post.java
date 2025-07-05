package spring.securitystudy.post.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.BatchSize;
import spring.securitystudy.comment.entity.Comment;
import spring.securitystudy.image.entity.Image;
import spring.securitystudy.user.entity.User;
import spring.securitystudy.post.dto.PostCreateDto;
import spring.securitystudy.post.dto.PostUpdateDto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Post {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String content;
    private LocalDate createdDate;
    private LocalDate updatedDate;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "memberId")
    private User user;

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    private List<Comment> commentList = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    @BatchSize(size = 10)
    private List<Image> imageList = new ArrayList<>();

    public static Post create(PostCreateDto dto, User loginUser) {
        Post post = new Post();
        post.title = dto.getTitle();
        post.content = dto.getContent();
        post.createdDate = LocalDate.now();
        post.updatedDate = null;
        post.user = loginUser;

        return post;
    }
    public void updateContent(PostUpdateDto dto) {
        this.title = dto.getTitle();
        this.content = dto.getContent();
        this.updatedDate = LocalDate.now();
    }
    public void addComment(Comment comment) {
        this.commentList.add(comment);
    }

    public void createFirstPost(User user) {
        this.title = "첫 게시글";
        this.content = "안녕하세요";
        this.createdDate = LocalDate.now();
        this.updatedDate = null;
        this.user = user;
    }

    public void createSecondPost(User user) {
        this.title = "두번째 게시글";
        this.content = "안녕하세요";
        this.createdDate = LocalDate.now();
        this.updatedDate = null;
        this.user = user;
    }

    public void addImage(Image image) {
        this.getImageList().add(image);
    }
}
