package spring.securitystudy.like.service;

import org.springframework.stereotype.Service;
import spring.securitystudy.post.entity.Post;
import spring.securitystudy.user.entity.User;

import java.util.List;
import java.util.Optional;

public interface LikeService {

    /**
     * 게시글에 좋아요를 누르지 않은 게시글은 좋아요를, 좋아요를 누른 게시글은 좋아요 취소를
     * @param loginUser
     * @param postId
     */
    void handleLike(Long postId, User loginUser);

    List<User> likeUserPost(Long postId);

    List<Post> userLike(Long userId);
}
