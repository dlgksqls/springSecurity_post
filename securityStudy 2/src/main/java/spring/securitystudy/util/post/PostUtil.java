package spring.securitystudy.util.post;

import spring.securitystudy.image.entity.Image;
import spring.securitystudy.post.dto.PostViewDto;
import spring.securitystudy.post.entity.Post;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PostUtil {

    public static List<Long> getPostIds(List<Post> posts){
        return posts
                .stream()
                .map(Post::getId)
                .toList();
    }

    public static List<PostViewDto> makePostViewDto(List<Post> posts, Map<Long, Long> likeCounts){
        List<PostViewDto> postDto = new ArrayList<>();

        for (Post post : posts) {
            List<String> imageUrls = post
                    .getImageList()
                    .stream()
                    .map(Image::getUrl)
                    .collect(Collectors.toList());

            long likeCount = likeCounts.getOrDefault(post.getId(), 0L);
            postDto.add(new PostViewDto(post, imageUrls, likeCount, true));
        }

        return postDto;
    }
}
