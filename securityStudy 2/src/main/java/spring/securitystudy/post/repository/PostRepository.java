package spring.securitystudy.post.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import spring.securitystudy.post.entity.Post;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("SELECT p FROM Post p JOIN FETCH p.member WHERE p.member.username = :username")
    List<Post> findByUsername(@Param("username") String username);

//    @Query("SELECT DISTINCT p FROM Post p JOIN FETCH p.member")
//    List<Post> findAllWithMember();

    /**
     * 컬렉션은 1:N 관계라서,
     * Post 10개에 Image 20개 있으면 → 결과 row는 20개 (중복된 Post가 생김) => error
     */
    @Query(
            value = "SELECT p FROM Post p JOIN FETCH p.member",
            countQuery = "SELECT COUNT(p) FROM Post p"
    )
    Page<Post> findAllWithMember(Pageable pageable);
}