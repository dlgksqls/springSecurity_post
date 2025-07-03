package spring.securitystudy.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.securitystudy.friendship.entity.Status;
import spring.securitystudy.friendship.repository.FriendShipRepository;
import spring.securitystudy.image.entity.Image;
import spring.securitystudy.member.dto.MemberProfile;
import spring.securitystudy.member.dto.MemberRegisterDto;
import spring.securitystudy.member.dto.MemberUpdateDto;
import spring.securitystudy.member.entity.Member;
import spring.securitystudy.member.repository.MemberRepository;
import spring.securitystudy.post.dto.PostViewDto;
import spring.securitystudy.post.entity.Post;
import spring.securitystudy.post.repository.PostRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final FriendShipRepository friendShipRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public Member register(MemberRegisterDto dto) {
        if (memberRepository.findByUsername(dto.getUsername()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 사용자 입니다.");
        }

        Member member = new Member();
        member.createMember(dto, passwordEncoder);
        memberRepository.save(member);

        return member;
    }

    @Override
    public Member findByUsername(String username){
        return memberRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("해당 사용자를 찾을 수 없습니다."));
    }

    @Override
    public List<Member> findAll(){
        return memberRepository.findAll();
    }

    @Override
    public List<Member> findByUsernamePrefix(String username) {
        return memberRepository.findByUsernamePrefix(username)
                .orElseThrow(() -> new IllegalArgumentException("해당 이름을 가진 사용자는 없습니다."));
    }

    @Override
    @Transactional
    public void update(String username, MemberUpdateDto dto) {
        Member findMember = findByUsername(username);
        findMember.update(dto);
    }

    @Override
    @Transactional
    public Member changeFriendOnly(String username, boolean isFriendOnly){
        Member loginMember = findByUsername(username);
        loginMember.changeFriendOnly(isFriendOnly);

        return loginMember;
    }

    @Override
    public MemberProfile findPostByUsername(String username) {
        Member findMember = findByUsername(username);
        List<Post> postByUsername = postRepository.findByUsername(username);
        List<PostViewDto> postDto = new ArrayList<>();
        for (Post post : postByUsername) {
            List<String> imageUrls = post.getImageList().stream().map(Image::getUrl).collect(Collectors.toList());
            postDto.add(new PostViewDto(post, imageUrls, true));
        }
        return MemberProfile.builder()
                .username(username)
                .role(findMember.getRole())
                .posts(postDto)
                .build();
    }

    @Override
    public Map<String, String> findFriendShipStatus(Member loginMember, List<Member> findMemberList) {
        Map<String, String> findMemberFriendShipList = new HashMap<>();
        for (Member member : findMemberList) {
            Status status = friendShipRepository.findStatus(loginMember, member);

            if (status != null){
                findMemberFriendShipList.put(member.getUsername(), status.name());
            }
            else findMemberFriendShipList.put(member.getUsername(), null);
        }

        return findMemberFriendShipList;
    }

    private boolean isUsernameDuplicate(String username) {
        return memberRepository.findByUsername(username).isPresent();
    }
}
