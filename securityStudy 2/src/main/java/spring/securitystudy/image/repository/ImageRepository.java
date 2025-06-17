package spring.securitystudy.image.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.securitystudy.image.entity.Image;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
