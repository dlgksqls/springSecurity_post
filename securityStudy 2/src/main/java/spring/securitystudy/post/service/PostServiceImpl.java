package spring.securitystudy.post.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.securitystudy.comment.dto.CommentDto;
import spring.securitystudy.exception.PostNotFoundException;
import spring.securitystudy.exception.UserNotFoundException;
import spring.securitystudy.image.dto.ImageUploadDto;
import spring.securitystudy.image.dto.ImageUrlsDto;
import spring.securitystudy.image.entity.Image;
import spring.securitystudy.image.repository.ImageRepository;
import spring.securitystudy.image.service.ImageService;
import spring.securitystudy.like.entity.Like;
import spring.securitystudy.like.repository.LikeRepository;
import spring.securitystudy.like.service.LikeService;
import spring.securitystudy.user.entity.User;
import spring.securitystudy.user.repository.UserRepository;
import spring.securitystudy.post.dto.PostCommentImageDto;
import spring.securitystudy.post.dto.PostCreateDto;
import spring.securitystudy.post.dto.PostUpdateDto;
import spring.securitystudy.post.dto.PostViewDto;
import spring.securitystudy.post.entity.Post;
import spring.securitystudy.post.repository.PostRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostServiceImpl implements PostService {

    private final ImageService imageService;

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ImageRepository imageRepository;
    private final LikeRepository likeRepository;

    @Override
    @Transactional
    public void create(PostCreateDto postDto, ImageUploadDto imageDto, String username) {
        User loginUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("해당 사용자를 찾을 수 없습니다."));

        Post newPost = Post.create(postDto, loginUser);
        imageService.uploadImage(imageDto, newPost);
        loginUser.addPost(newPost);

        postRepository.save(newPost);
    }

    @Override
    public Page<PostViewDto> findAllByPage(int page, int size, User loginUser){
        PageRequest pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<PostViewDto> pagePosts = postRepository.findAllWithMember(pageable);

        // 페이지 별로 post 불러오기
        List<Long> postIds = pagePosts.stream().map(PostViewDto::getId).toList();

        // post 별로 이미지 불러오기
        List<Image> images = imageRepository.findAllByPostId(postIds);
        Map<Long, List<String>> postImage = images.stream()
                .collect(Collectors.groupingBy(
                        image -> image.getPost().getId(),
                        Collectors.mapping(Image::getUrl, Collectors.toList())
                ));

        // 좋아요 처리
        Map<Long, Long> likeCounts = postRepository.countLikesByPostsIds(postIds).stream()
                .collect(Collectors.toMap(
                        arr -> (Long) arr[0],
                        arr -> (Long) arr[1]
                ));

        // 로그인 유저 좋아요 여부
        Set<Long> likePostIds = (loginUser != null) ?
                new HashSet<>(likeRepository.findLikeByLoginUserAndPostIds(loginUser.getId(), postIds))
                : new HashSet<>();

        pagePosts.forEach(postViewDto -> {
            postViewDto.setImageUrls(postImage.getOrDefault(postViewDto.getId(), List.of()));
            postViewDto.setLikeCnt(likeCounts.getOrDefault(postViewDto.getId(), 0L));
            postViewDto.setLikeByLoginUser(likePostIds.contains(postViewDto.getId()));
        });

        return pagePosts;
    }

    @Override
    public Post findById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException("해당 게시물은 없습니다."));
    }

    @Override
    @Transactional
    public void update(Long id, PostUpdateDto dto) {
        Post findPost = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException("해당 게시물은 없습니다."));


        findPost.updateContent(dto);
    }

    @Override
    public PostCommentImageDto findCommentPost(Long id) {
        Post findPost = findById(id);

        List<CommentDto> postComment = postRepository.findCommentByPostId(id);
        List<ImageUrlsDto> postImage = postRepository.findImageByPostId(id);

        return new PostCommentImageDto(findPost, postImage, postComment);
    }

    @Override
    public List<Post> findByUsername(String username) {
        return postRepository.findByUsername(username);
    }
}
