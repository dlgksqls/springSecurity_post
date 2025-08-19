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
    Optional<Like> findByPostAndUser(Post post, User user);
    void deleteByPostAndUser(Post post, User user);

    @Query("SELECT l.post.id FROM Like l WHERE l.user = :loginUser")
    List<Long> findByUser(@Param("loginUser") User loginUser);
}
