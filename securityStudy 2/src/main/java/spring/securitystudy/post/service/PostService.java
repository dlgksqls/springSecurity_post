package spring.securitystudy.post.service;

import org.springframework.data.domain.Page;
import spring.securitystudy.image.dto.ImageUploadDto;
import spring.securitystudy.post.dto.PostCommentDto;
import spring.securitystudy.post.dto.PostCreateDto;
import spring.securitystudy.post.dto.PostUpdateDto;
import spring.securitystudy.post.dto.PostViewDto;
import spring.securitystudy.post.entity.Post;

import java.util.List;

public interface PostService {
    void create(PostCreateDto postDto, ImageUploadDto imageDto, String username);
    Page<PostViewDto> findAllByPage(int page, int size);
    Post findById(Long id);
    void update(Long id, PostUpdateDto dto);
    List<Post> findByUsername(String username);
    PostCommentDto findCommentPost(Long id);
}
