package spring.securitystudy.image.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import spring.securitystudy.image.entity.Image;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {

    @Query("SELECT i FROM Image i JOIN FETCH i.post p WHERE p.id IN :postIds")
    List<Image> findAllByPostId(@Param("postIds") List<Long> postIds);
}
