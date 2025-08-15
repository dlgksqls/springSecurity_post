package spring.securitystudy.friendship.service;

import spring.securitystudy.friendship.dto.FriendShipReturnDto;
import spring.securitystudy.friendship.entity.Status;
import spring.securitystudy.user.entity.User;

import java.util.List;

public interface FriendShipService {
    /**
     * 친구 요청
     * @param loginUser 로그인 유저
     * @param receiveMemberName 받는 친구 이름
     */
    void request(String loginUser, String receiveMemberName);

    /**
     * 유저 이름으로 유저가 받은 친구 요청 목록 조회
     * @param username
     * @return
     */
    List<FriendShipReturnDto> findReceiveByUserName(String username);

    /**
     * 사용자와 친구인 멤버를 list로 반환
     * @param username
     * @return
     */
    List<FriendShipReturnDto> findAllFriendsByUsername(String username);

    /**
     * 로그인 유저와 찾고자 하는 유저의 친구 관계 조회
     * @param loginMember
     * @param member
     * @return
     */
    Status isFriendStatus(User loginMember, User member);

    /**
     * 친구 요청 수락
     * @param receiveMemberName
     * @param requestMemberName
     */
    void acceptFriend(String receiveMemberName, String requestMemberName);

    /**
     * 친구 요청 거부
     * @param receiveMemberName
     * @param requestMemberName
     */
    void rejectFriend(String receiveMemberName, String requestMemberName);

    /**
     * 로그인 유저와 찾고자 하는 유저가 친구관계인지 확인
     * @param loginUserName
     * @param member
     * @return
     */
    Boolean isFriend(String loginUserName, User member);

    /**
     * 사용자와 친구인 멤버의 이름을 list로 반환
     * @param loginUser
     * @return
     */
    List<String> findFriendShipList(User loginUser);
}
