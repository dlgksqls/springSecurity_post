package spring.securitystudy.post.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
import spring.securitystudy.util.image.ImageUtil;
import spring.securitystudy.util.like.LikeUtil;
import spring.securitystudy.util.post.PostUtil;

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
        Page<Post> pagePosts = postRepository.findAllWithMember(pageable);

        // 페이지 별로 post 불러오기
        List<Long> postIds = PostUtil.getPostIds(pagePosts.getContent());

        // post 별로 이미지 불러오기
        Map<Long, List<String>> postImage = getPostImage(postIds);

        // 좋아요 처리
        Map<Long, Long> likeCounts = countPostLike(postIds);

        // 로그인 유저 좋아요 여부
        Set<Long> likePostIds = loginUserLikePost(loginUser, postIds);

        List<PostViewDto> dtoList = pageToPostViewDto(pagePosts, postImage, likeCounts, likePostIds);

        return new PageImpl<>(dtoList, pageable, pagePosts.getTotalElements());
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

    private Map<Long, List<String>> getPostImage(List<Long> postIds) {
        List<Image> images = imageRepository.findAllByPostId(postIds);
        Map<Long, List<String>> postImage = ImageUtil.getAllImages(images);
        return postImage;
    }

    private Map<Long, Long> countPostLike(List<Long> postIds) {
        List<Object[]> likeObjects = postRepository.countLikesByPostsIds(postIds);
        Map<Long, Long> likeCounts = LikeUtil.countLikesByPostsIds(likeObjects);
        return likeCounts;
    }

    private Set<Long> loginUserLikePost(User loginUser, List<Long> postIds) {
        Set<Long> likePostIds = (loginUser != null) ?
                new HashSet<>(likeRepository.findLikeByLoginUserAndPostIds(loginUser.getId(), postIds))
                : new HashSet<>();
        return likePostIds;
    }

    private static List<PostViewDto> pageToPostViewDto(Page<Post> pagePosts, Map<Long, List<String>> postImage, Map<Long, Long> likeCounts, Set<Long> likePostIds) {
        List<PostViewDto> dtoList = pagePosts.getContent().stream()
                .map(post -> new PostViewDto(
                        post,
                        postImage.getOrDefault(post.getId(), List.of()),
                        likeCounts.getOrDefault(post.getId(), 0L),
                        likePostIds.contains(post.getId()),
                        post.getUser().isFriendOnly()
                ))
                .toList();
        return dtoList;
    }
}
