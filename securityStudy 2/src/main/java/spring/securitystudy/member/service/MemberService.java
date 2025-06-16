package spring.securitystudy.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.securitystudy.member.dto.MemberRegisterDto;
import spring.securitystudy.member.dto.MemberUpdateDto;
import spring.securitystudy.member.entity.Member;
import spring.securitystudy.member.repository.MemberRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

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

    public Member findByUsername(String username){
        return memberRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("해당 사용자를 찾을 수 없습니다."));
    }
    
    public List<Member> findAll(){
        return memberRepository.findAll();
    }

    public List<Member> findByUsernamePrefix(String username) {
        return memberRepository.findByUsernamePrefix(username)
                .orElseThrow(() -> new IllegalArgumentException("해당 이름을 가진 사용자는 없습니다."));
    }

    @Transactional
    public void update(String username, MemberUpdateDto dto) {
        if (!username.equals(dto.getUsername()) && isUsernameDuplicate(dto.getUsername())) {
            throw new IllegalArgumentException("중복된 이름입니다.");
        }

        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("해당 이름을 가진 사용자는 없습니다."));

        member.update(dto);
    }

    private boolean isUsernameDuplicate(String username) {
        return memberRepository.findByUsername(username).isPresent();
    }
}
