package spring.securitystudy.member.service;

import spring.securitystudy.member.dto.MemberProfile;
import spring.securitystudy.member.dto.MemberRegisterDto;
import spring.securitystudy.member.dto.MemberUpdateDto;
import spring.securitystudy.member.entity.Member;

import java.util.List;
import java.util.Map;

public interface MemberService {
    Member register(MemberRegisterDto dto);
    Member findByUsername(String username);
    List<Member> findAll();
    List<Member> findByUsernamePrefix(String username);
    void update(String username, MemberUpdateDto dto);
    Member changeFriendOnly(String username, boolean isFriendOnly);
    MemberProfile findPostByUsername(String username);
    Map<String, String> findFriendShipStatus(Member loginMember, List<Member> findMemberList);
}
