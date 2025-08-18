package spring.securitystudy.friendship.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.securitystudy.friendship.dto.FriendShipReturnDto;
import spring.securitystudy.friendship.entity.FriendShip;
import spring.securitystudy.friendship.entity.Status;
import spring.securitystudy.friendship.exception.FriendShipNotFoundException;
import spring.securitystudy.friendship.exception.HandleUserNotExistException;
import spring.securitystudy.friendship.exception.ReceiveUserNotFoundException;
import spring.securitystudy.friendship.repository.FriendShipRepository;
import spring.securitystudy.user.entity.User;
import spring.securitystudy.user.exception.UserNotFoundException;
import spring.securitystudy.user.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FriendShipServiceImpl implements FriendShipService{

    private final FriendShipRepository friendShipRepository;
    private final UserRepository memberRepository;

    @Override
    @Transactional
    public void request(String loginUser, String receiveMemberName) {
        User receiveMember = memberRepository.findByUsername(receiveMemberName)
                .orElseThrow(() -> new ReceiveUserNotFoundException("해당 유저는 없습니다."));

        FriendShip newFriendShip = new FriendShip();
        User requestUser = memberRepository.findByUsername(loginUser)
                .orElseThrow(() -> new UserNotFoundException("로그인 해주세요."));

        newFriendShip.save(requestUser, receiveMember);

        friendShipRepository.save(newFriendShip);

        requestUser.requestFriendShip(newFriendShip);
        receiveMember.receiveFriendShip(newFriendShip);
    }

    @Override
    public List<FriendShipReturnDto> findReceiveByUserName(String username) {
        List<FriendShip> receiveList = friendShipRepository.findReceive(username, Status.REQUEST);
        return receiveList.stream()
                .map(friendShip -> new FriendShipReturnDto(
                        friendShip.getSendUser().getUsername(),
                        friendShip.getStatus()
                        )
                ).toList();
    }

    @Override
    public Status isFriendStatus(User loginMember, User member) {
        return friendShipRepository.findStatus(loginMember, member);
    }

    @Override
    @Transactional
    public void acceptFriend(String receiveMemberName, String requestMemberName) {
        FriendShip friendShip = findMembersRelation(receiveMemberName, requestMemberName);

        friendShip.accept();

        removeFriendShipCollection(friendShip);
    }

    @Override
    @Transactional
    public void rejectFriend(String receiveMemberName, String requestMemberName) {
        FriendShip friendShip = findMembersRelation(receiveMemberName, requestMemberName);

        friendShipRepository.delete(friendShip);

        removeFriendShipCollection(friendShip);
    }

    private FriendShip findMembersRelation(String receiveMemberName, String requestMemberName) {
        User receiveMember = memberRepository.findByUsername(receiveMemberName)
                .orElseThrow(() -> new HandleUserNotExistException("해당 유저는 없습니다."));

        User requestMember = memberRepository.findByUsername(requestMemberName)
                .orElseThrow(() -> new HandleUserNotExistException("해당 유저는 없습니다."));

        return friendShipRepository.findByLoginMemberAndRequestMember(receiveMember, requestMember)
                .orElseThrow(() -> new FriendShipNotFoundException("해당 관계를 찾을 수 없습니다."));
    }

    private static void removeFriendShipCollection(FriendShip friendShip) {
        friendShip.getReceiveUser().removeReceive(friendShip.getSendUser());
        friendShip.getSendUser().removeRequest(friendShip.getReceiveUser());
    }

    @Override
    public Boolean isFriend(String loginUserName, User member) {
        User loginUser = memberRepository.findByUsername(loginUserName)
                .orElseThrow(() -> new UserNotFoundException("해당 유저는 없습니다."));

        return friendShipRepository.findFriendShip(loginUser, member, Status.ACCEPT).isPresent();
    }

    @Override
    public List<String> findFriendShipList(User loginUser) {
        return friendShipRepository.findFriendShipList(loginUser, Status.ACCEPT);
    }

    @Override
    public List<FriendShipReturnDto> findAllFriendsByUsername(String username) {
        List<FriendShip> allFriendShipByUsername = friendShipRepository.findAllFriendsByUsername(username);

        return allFriendShipByUsername.stream()
                .map(friendShip -> {
                    String friendUsername = friendShip.getSendUser().getUsername().equals(username)
                            ? friendShip.getReceiveUser().getUsername()
                            : friendShip.getSendUser().getUsername();
                    return new FriendShipReturnDto(friendUsername, Status.ACCEPT);
                }).toList();
    }
}
