package spring.securitystudy.post.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import spring.securitystudy.member.entity.Member;
import spring.securitystudy.member.service.MemberService;
import spring.securitystudy.post.dto.PostCreateDto;
import spring.securitystudy.post.dto.PostViewDto;
import spring.securitystudy.post.entity.Post;
import spring.securitystudy.post.repository.PostRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final MemberService memberService;
    public void create(PostCreateDto dto, String userName) {
        Member loginUser = memberService.findByUsername(userName);
        Post newPost = Post.create(dto, loginUser);
        loginUser.addPost(newPost);

        postRepository.save(newPost);
    }

    public List<PostViewDto> findAll(){
        List<PostViewDto> returnDto = new ArrayList<>();
        List<Post> allPost = postRepository.findAll();

        for (Post post : allPost) {
            returnDto.add(new PostViewDto(post));
        }

        return returnDto;
    }
}
