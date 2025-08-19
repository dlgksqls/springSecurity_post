package spring.securitystudy.post.service;

import org.springframework.data.domain.Page;
import spring.securitystudy.image.dto.ImageUploadDto;
import spring.securitystudy.post.dto.PostCommentImageDto;
import spring.securitystudy.post.dto.PostCreateDto;
import spring.securitystudy.post.dto.PostUpdateDto;
import spring.securitystudy.post.dto.PostViewDto;
import spring.securitystudy.post.entity.Post;
import spring.securitystudy.user.entity.User;

import java.util.List;

public interface PostService {
    /**
     * 게시글 생성
     * @param postDto (String title, String content)
     * @param imageDto (List<MultipartFile> files)
     * @param username
     */
    void create(PostCreateDto postDto, ImageUploadDto imageDto, String username);

    /**
     * 모든 게시글을 paging으로 불러오기
     * @param page 페이지 번호
     * @param size 몇개를 불러올 것인가
     * @param loginUser
     * @return
     */
    Page<PostViewDto> findAllByPage(int page, int size, User loginUser);

    /**
     * postId로 특정 post 조회
     * @param id
     * @return
     */
    Post findById(Long id);

    /**
     * postUpdate
     * @param id post Id
     * @param dto (String title, String content, LocalDate updatedDate)
     */
    void update(Long id, PostUpdateDto dto);

    /**
     * 사용자의 이름 기반으로 사용자가 올린 게시글 조회
     * @param username
     * @return
     */
    List<Post> findByUsername(String username);

    /**
     * 게시글 상세보기에서 댓글과 이미지 조회도 같이 하기 위한 method
     * @param id
     * @return
     */
    PostCommentImageDto findCommentPost(Long id);
}
