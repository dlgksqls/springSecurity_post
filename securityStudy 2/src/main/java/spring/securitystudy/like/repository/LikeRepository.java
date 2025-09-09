package spring.securitystudy.like.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import spring.securitystudy.like.entity.Like;
import spring.securitystudy.post.entity.Post;
import spring.securitystudy.user.entity.User;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {

    @Query("SELECT l FROM Like l WHERE l.post.id = :postId AND l.user.id = :userId")
    Optional<Like> findByPostAndUser(@Param("postId") Long postId, @Param("userId") Long userId);

    void deleteByPostAndUser(Post post, User user);

    @Query("SELECT l.post.id FROM Like l WHERE l.user = :loginUser")
    List<Long> findByUser(@Param("loginUser") User loginUser);

    @Query("SELECT COUNT(*) FROM Like l WHERE l.post.id = :postId")
    long likeCount(@Param("postId") Long postId);

    @Query("SELECT COUNT(*) FROM Like l WHERE l.user.id = :userId AND l.post.id IN :postIds")
    List<Long> findLikeByLoginUserAndPostIds(@Param("userId") Long userId, @Param("postIds") List<Long> postIds);

    @Query("SELECT l.user FROM Like l INNER JOIN l.post WHERE l.post.id = :postId")
    List<User> findLikeUserPost(@Param("postId") Long postId);

    @Query("SELECT l.post FROM Like l INNER JOIN l.user WHERE l.user.id = :userId")
    List<Post> findUserLike(@Param("userId") Long userId);
}
