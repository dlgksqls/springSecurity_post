package spring.securitystudy.friendship.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.securitystudy.friendship.entity.FriendShip;
import spring.securitystudy.friendship.entity.Status;
import spring.securitystudy.friendship.repository.FriendShipRepository;
import spring.securitystudy.member.entity.Member;
import spring.securitystudy.member.repository.MemberRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FriendShipService {
    private final FriendShipRepository friendShipRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void add(Member loginUser, Member receiveUser) {
        FriendShip newFriendShip = new FriendShip();
        newFriendShip.save(loginUser, receiveUser);

        friendShipRepository.save(newFriendShip);

        loginUser.requestFriendShip(newFriendShip);
        receiveUser.reveiveFriendShip(newFriendShip);
    }


    public List<FriendShip> findByUsername(String loginUserName) {
        return friendShipRepository.findReceive(loginUserName, Status.REQUEST);
    }

    public Status isFriendStatus(String loginUserName, Member member) {
        Member loginUser = memberRepository.findByUsername(loginUserName)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저는 없습니다."));

        return friendShipRepository.findStatus(loginUser, member);
    }

    @Transactional
    public void acceptFriend(Member receiveMember, Member requestMember) {
        FriendShip friendShip = friendShipRepository.findByLoginMemberAndRequestMember(receiveMember, requestMember);

        if (friendShip.getStatus().name().equals("REQUEST")){
            friendShip.accept();

            receiveMember.removeReceive(requestMember);
            requestMember.removeRequest(receiveMember);
        }
    }

    @Transactional
    public void rejectFriend(Member receiveMember, Member requestMember) {
        FriendShip friendShip = friendShipRepository.findByLoginMemberAndRequestMember(receiveMember, requestMember);

        if (friendShip.getStatus().name().equals("REQUEST")){
            friendShipRepository.delete(friendShip);

            receiveMember.removeReceive(requestMember);
            requestMember.removeRequest(receiveMember);
        }
    }

    public Boolean isFriend(String loginUserName, Member member) {
        Member loginUser = memberRepository.findByUsername(loginUserName)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저는 없습니다."));

        Optional<FriendShip> friendShip = friendShipRepository.findFriendShip(loginUser, member, Status.ACCEPT);
        return friendShip.isPresent();
    }
}
