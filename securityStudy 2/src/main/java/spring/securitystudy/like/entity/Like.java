package spring.securitystudy.like.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import spring.securitystudy.post.entity.Post;
import spring.securitystudy.user.entity.User;

@Entity
@Table(name = "LikeEntity")
@Getter
@NoArgsConstructor
public class Like {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "postId")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;

    public Like(Post post, User user) {
        this.post = post;
        this.user = user;
    }

    public static Like addNewLike(Post findPost, User loginUser) {
        Like like = new Like(findPost, loginUser);

        findPost.addLike(like);
        loginUser.addLike(like);

        return like;
    }
}
