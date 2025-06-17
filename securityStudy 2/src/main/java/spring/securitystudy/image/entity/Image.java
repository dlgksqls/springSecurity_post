package spring.securitystudy.image.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import spring.securitystudy.post.entity.Post;

@Entity
@Getter
public class Image {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String url;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "postId")
    private Post post;

    public void createNewImage(String imageFileName, Post newPost) {
        this.url = "/image/" + imageFileName;
        this.post = newPost;
    }
}
