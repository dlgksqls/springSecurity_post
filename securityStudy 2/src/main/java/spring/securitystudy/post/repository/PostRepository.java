package spring.securitystudy.post.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import spring.securitystudy.comment.entity.Comment;
import spring.securitystudy.image.entity.Image;
import spring.securitystudy.post.dto.PostViewDto;
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
            value = "SELECT new spring.securitystudy.post.dto.PostViewDto(p.id, p.title, p.content, p.member.username, p.member.isFriendOnly, p.createdDate) " +
                    "FROM Post p JOIN p.member m " +
                    "ORDER BY p.id DESC",
            countQuery = "SELECT COUNT(p) FROM Post p"
    )
    Page<PostViewDto> findAllWithMember(Pageable pageable);

    @Query(value = "SELECT c FROM Comment c JOIN FETCH c.member cm WHERE c.post.id = :id")
    List<Comment> findCommentByPostId (@Param(value = "id") Long id);

    @Query(value = "SELECT i FROM Image i WHERE i.post.id = :id")
    List<Image> findImageByImageId(@Param(value = "id") Long id);
}