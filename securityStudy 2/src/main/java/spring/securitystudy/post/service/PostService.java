package spring.securitystudy.post.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.securitystudy.image.dto.ImageUploadDto;
import spring.securitystudy.image.entity.Image;
import spring.securitystudy.image.service.ImageService;
import spring.securitystudy.member.entity.Member;
import spring.securitystudy.member.service.MemberService;
import spring.securitystudy.post.dto.PostCreateDto;
import spring.securitystudy.post.dto.PostUpdateDto;
import spring.securitystudy.post.dto.PostViewDto;
import spring.securitystudy.post.entity.Post;
import spring.securitystudy.post.repository.PostRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final MemberService memberService;
    private final ImageService imageService;

    @Transactional
    public void create(PostCreateDto postDto, ImageUploadDto imageDto, String username) {
        Member loginUser = memberService.findByUsername(username);

        Post newPost = Post.create(postDto, loginUser);
        imageService.uploadImage(imageDto, newPost);
        loginUser.addPost(newPost);

        postRepository.save(newPost);
    }

//    public List<PostViewDto> findAll(){
//        List<PostViewDto> returnDto = new ArrayList<>();
//        List<Post> allPost = postRepository.findAllWithMember();
//
//        for (Post post : allPost) {
//            boolean isFriend = post.getMember().isFriendOnly();
//            returnDto.add(new PostViewDto(post, isFriend));
//        }
//
//        return returnDto;
//    }

    public Page<PostViewDto> findAllByPage(int page, int size){
        PageRequest pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<Post> pagePosts = postRepository.findAllWithMember(pageable);

        return pagePosts.map(post -> {
            boolean isFriendOnly = post.getMember().isFriendOnly();
            List<String> imageUrls = post.getImageList().stream().map(Image::getUrl).collect(Collectors.toList());
            return new PostViewDto(post, imageUrls, isFriendOnly);
        });
    }

    public Post findById(Long id) {
        Post findPost = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시물은 없습니다."));

        return findPost;
    }

    @Transactional
    public void update(Long id, PostUpdateDto dto) {
        Post findPost = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시물은 없습니다."));


        findPost.updateContent(dto);
    }

    public List<Post> findByUsername(String username) {
        return postRepository.findByUsername(username);
    }
}
