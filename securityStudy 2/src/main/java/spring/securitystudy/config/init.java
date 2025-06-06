package spring.securitystudy.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import spring.securitystudy.member.entity.Member;
import spring.securitystudy.member.repository.MemberRepository;
import spring.securitystudy.post.entity.Post;
import spring.securitystudy.post.repository.PostRepository;

@RequiredArgsConstructor
@Component
public class init {

    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        Member firstMember = new Member();
        firstMember.createFirstMember(passwordEncoder);
        memberRepository.save(firstMember);

        Member secondMember = new Member();
        secondMember.createSecondMember(passwordEncoder);
        memberRepository.save(secondMember);

        Post firstPost = new Post();
        firstPost.createFirstPost(firstMember);
        postRepository.save(firstPost);

        Post secondPost = new Post();
        secondPost.createSecondPost(firstMember);
        postRepository.save(secondPost);
    }
}
