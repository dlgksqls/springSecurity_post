package spring.securitystudy.like.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import spring.securitystudy.like.entity.Like;
import spring.securitystudy.like.repository.LikeRepository;
import spring.securitystudy.post.entity.Post;
import spring.securitystudy.post.repository.PostRepository;
import spring.securitystudy.post.service.PostService;
import spring.securitystudy.user.CustomUserDetails;
import spring.securitystudy.user.entity.User;
import spring.securitystudy.user.repository.UserRepository;
import spring.securitystudy.user.service.UserService;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class LikeServiceImpl implements LikeService{

    private final PostService postService;
    private final UserService userService;

    private final PostRepository postRepository;
    private final LikeRepository likeRepository;

    @Override
    public void handleLike(Long postId, User loginUser) {
        Post findPost = postService.findById(postId);
        User findUser = userService.findByUsername(loginUser.getUsername());// 영속성 컨텍스트에 넣어주기 위함

        Optional<Like> findPostLike = likeRepository.findByPostAndUser(findPost, findUser);

        if (findPostLike.isPresent()) {
            Like deleteLike = findPostLike.get();
            findPost.removeLike(deleteLike);
            findUser.removeLike(deleteLike);

            likeRepository.delete(deleteLike);
        }
        else addNewLIke(findUser, findPost);
    }

    @Override
    public boolean likedByLoginUser(Post post, User user) {

        return false;
    }

    private void addNewLIke(User loginUser, Post findPost) {
        Like newLike = Like.addNewLike(findPost, loginUser);
        likeRepository.save(newLike);
    }
}
