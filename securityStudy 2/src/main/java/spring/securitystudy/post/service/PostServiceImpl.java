package spring.securitystudy.post.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.securitystudy.comment.dto.CommentDto;
import spring.securitystudy.comment.entity.Comment;
import spring.securitystudy.image.dto.ImageUploadDto;
import spring.securitystudy.image.dto.ImageUrlsDto;
import spring.securitystudy.image.entity.Image;
import spring.securitystudy.image.repository.ImageRepository;
import spring.securitystudy.image.service.ImageService;
import spring.securitystudy.member.entity.Member;
import spring.securitystudy.member.repository.MemberRepository;
import spring.securitystudy.post.dto.PostCommentDto;
import spring.securitystudy.post.dto.PostCreateDto;
import spring.securitystudy.post.dto.PostUpdateDto;
import spring.securitystudy.post.dto.PostViewDto;
import spring.securitystudy.post.entity.Post;
import spring.securitystudy.post.repository.PostRepository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final ImageRepository imageRepository;

    private final ImageService imageService;

    @Override
    @Transactional
    public void create(PostCreateDto postDto, ImageUploadDto imageDto, String username) {
        Member loginUser = memberRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다."));

        Post newPost = Post.create(postDto, loginUser);
        imageService.uploadImage(imageDto, newPost);
        loginUser.addPost(newPost);

        postRepository.save(newPost);
    }

    @Override
    public Page<PostViewDto> findAllByPage(int page, int size){
        PageRequest pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<PostViewDto> pagePosts = postRepository.findAllWithMember(pageable);
        List<Long> postIds = pagePosts.stream().map(PostViewDto::getId).toList();
        List<Image> images = imageRepository.findAllByPostId(postIds);
        Map<Long, List<String>> postImage = images.stream()
                .collect(Collectors.groupingBy(
                        image -> image.getPost().getId(),
                        Collectors.mapping(Image::getUrl, Collectors.toList())
                ));

        pagePosts.forEach(post -> {
            List<String> urls = postImage.getOrDefault(post.getId(), List.of());
            post.setImageUrls(urls);
        });

        return pagePosts;
    }

    @Override
    public Post findById(Long id) {
        Post findPost = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시물은 없습니다."));

        return findPost;
    }

    @Override
    @Transactional
    public void update(Long id, PostUpdateDto dto) {
        Post findPost = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시물은 없습니다."));


        findPost.updateContent(dto);
    }

    @Override
    public PostCommentDto findCommentPost(Long id) {
        Post findPost = findById(id);
        PostCommentDto postCommentDto = new PostCommentDto(findPost);

        log.info("comment find");
        List<Comment> commentList = findPost.getCommentList();
        for (Comment comment : commentList) {
            postCommentDto.getCommentList().add(new CommentDto(comment));
        }

        log.info("image find");
        List<Image> imageList = findPost.getImageList();
        for (Image image : imageList) {
            postCommentDto.getImageUrlsList().add(new ImageUrlsDto(image.getUrl()));
        }

        return postCommentDto;
    }

    @Override
    public List<Post> findByUsername(String username) {
        return postRepository.findByUsername(username);
    }
}
