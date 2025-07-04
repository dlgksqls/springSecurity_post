package spring.securitystudy.friendship.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.securitystudy.friendship.dto.FriendShipReturnDto;
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
    public void add(String loginUser, Member receiveUser) {
        FriendShip newFriendShip = new FriendShip();
        Member requestUser = memberRepository.findByUsername(loginUser)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저는 없습니다."));

        newFriendShip.save(requestUser, receiveUser);

        friendShipRepository.save(newFriendShip);

        requestUser.requestFriendShip(newFriendShip);
        receiveUser.receiveFriendShip(newFriendShip);
    }


    public List<FriendShipReturnDto> findByUsername(String loginUserName) {
        List<FriendShip> receiveList = friendShipRepository.findReceive(loginUserName, Status.REQUEST);
        return receiveList.stream()
                .map(friendShip -> new FriendShipReturnDto(
                        friendShip.getSendMember().getUsername(),
                        friendShip.getStatus()
                        )
                ).toList();
    }

    public Status isFriendStatus(Member loginMember, Member member) {
        return friendShipRepository.findStatus(loginMember, member);
    }

    @Transactional
    public void acceptFriend(String receiveMemberName, String requestMemberName) {
        FriendShip friendShip = findMembersRelation(receiveMemberName, requestMemberName);

        if (friendShip.getStatus() == Status.ACCEPT){
            friendShip.accept();

            removeFriendShipCollection(friendShip);
        }
    }

    @Transactional
    public void rejectFriend(String receiveMemberName, String requestMemberName) {

        FriendShip friendShip = findMembersRelation(receiveMemberName, requestMemberName);

        if (friendShip.getStatus() == Status.BLOCKED){
            friendShipRepository.delete(friendShip);

            removeFriendShipCollection(friendShip);
        }
    }

    private FriendShip findMembersRelation(String receiveMemberName, String requestMemberName) {
        Member receiveMember = memberRepository.findByUsername(receiveMemberName)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저는 없습니다."));

        Member requestMember = memberRepository.findByUsername(requestMemberName)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저는 없습니다."));

        return friendShipRepository.findByLoginMemberAndRequestMember(receiveMember, requestMember);
    }

    private static void removeFriendShipCollection(FriendShip friendShip) {
        friendShip.getReceiveMember().removeReceive(friendShip.getSendMember());
        friendShip.getSendMember().removeRequest(friendShip.getReceiveMember());
    }

    public Boolean isFriend(String loginUserName, Member member) {
        Member loginUser = memberRepository.findByUsername(loginUserName)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저는 없습니다."));

        Optional<FriendShip> friendShip = friendShipRepository.findFriendShip(loginUser, member, Status.ACCEPT);
        return friendShip.isPresent();
    }

    public List<String> findFriendShipList(Member loginUser) {
        return friendShipRepository.findFriendShipList(loginUser, Status.ACCEPT);
    }

    public List<FriendShip> findAllByUsername(String username) {
        return friendShipRepository.findAllFriendsByUsername(username);
    }
}
