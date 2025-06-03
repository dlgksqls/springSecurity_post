package spring.securitystudy.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.securitystudy.post.entity.Post;

public interface PostRepository extends JpaRepository<Post, Long> {
}
