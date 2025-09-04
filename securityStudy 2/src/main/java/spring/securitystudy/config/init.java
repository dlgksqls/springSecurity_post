package spring.securitystudy.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import spring.securitystudy.user.dto.UserRegisterDto;
import spring.securitystudy.user.entity.User;
import spring.securitystudy.user.repository.UserRepository;
import spring.securitystudy.post.dto.PostCreateDto;
import spring.securitystudy.post.entity.Post;
import spring.securitystudy.post.repository.PostRepository;

@RequiredArgsConstructor
@Component
public class init {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        User firstMember = new User();
        firstMember.createFirstUser(passwordEncoder);
        userRepository.save(firstMember);

        User secondMember = new User();
        secondMember.createSecondUser(passwordEncoder);
        userRepository.save(secondMember);

        Post firstPost = new Post();
        firstPost.createFirstPost(firstMember);
        postRepository.save(firstPost);

        Post secondPost = new Post();
        secondPost.createSecondPost(firstMember);
        postRepository.save(secondPost);

        for(int i=0; i<8; i++){
            User user = new User();
            UserRegisterDto UserDto = new UserRegisterDto();
            UserDto.setUsername(Integer.toString(i));
            UserDto.setPassword(Integer.toString(1234));
            UserDto.setEmail(i * 5 + "@email.com");
            user.createUser(UserDto, passwordEncoder);
            userRepository.save(user);

            PostCreateDto postDto = new PostCreateDto();

            postDto.setContent(Integer.toString(i));
            postDto.setTitle(Integer.toString(i));

            Post createPost = Post.create(postDto, user);

            postRepository.save(createPost);
        }
    }
}
