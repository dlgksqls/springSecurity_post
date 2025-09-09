package spring.securitystudy.like.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.securitystudy.like.entity.Like;
import spring.securitystudy.like.repository.LikeRepository;
import spring.securitystudy.post.entity.Post;
import spring.securitystudy.post.repository.PostRepository;
import spring.securitystudy.post.service.PostService;
import spring.securitystudy.user.CustomUserDetails;
import spring.securitystudy.user.entity.User;
import spring.securitystudy.user.repository.UserRepository;
import spring.securitystudy.user.service.UserService;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class LikeServiceImpl implements LikeService{

    private final PostService postService;
    private final UserService userService;

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final LikeRepository likeRepository;

    @Override
    @Transactional
    public void handleLike(Long postId, User loginUser) {
        Post findPost = postRepository.getReferenceById(postId);
        User findUser = userRepository.getReferenceById(loginUser.getId());

        Optional<Like> exist = likeRepository.findByPostAndUser(findPost.getId(), findUser.getId());

        if (exist.isPresent()) {
            findPost.removeLike(exist.get());
            findUser.removeLike(exist.get());

            likeRepository.delete(exist.get());
        }
        else addNewLIke(findUser, findPost);
    }

    @Override
    public List<User> likeUserPost(Long postId) {
        return likeRepository.findLikeUserPost(postId);
    }

    @Override
    public List<Post> userLike(Long userId){
        return likeRepository.findUserLike(userId);
    }

    private void addNewLIke(User loginUser, Post findPost) {
        Like newLike = Like.addNewLike(findPost, loginUser);
        likeRepository.save(newLike);
    }
}
