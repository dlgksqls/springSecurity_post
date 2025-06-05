package spring.securitystudy.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.securitystudy.comment.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
