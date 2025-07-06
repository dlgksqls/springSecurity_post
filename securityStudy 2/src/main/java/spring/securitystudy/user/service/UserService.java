package spring.securitystudy.user.service;

import spring.securitystudy.user.dto.UserProfile;
import spring.securitystudy.user.dto.UserRegisterDto;
import spring.securitystudy.user.dto.UserUpdateDto;
import spring.securitystudy.user.entity.User;

import java.util.List;
import java.util.Map;

public interface UserService {
    /**
     * 회원가입
     * @param dto (String username, String password)
     * @return
     */
    User register(UserRegisterDto dto);

    /**
     * 회원 이름으로 회원 조회
     * @param username
     * @return
     */
    User findByUsername(String username);

    /**
     * 모든 회원 조회
     * @return
     */
    List<User> findAll();

    /**
     * prefix로 시작하는 유저 조회
     * @param
     * @return
     */
    List<User> findByUsernamePrefix(String prefix);

    /**
     * username으로 유저 조회 후 dto내용 기반으로 정보 update
     * @param username
     * @param dto (String username, boolean friendOnly)
     */
    void update(String username, UserUpdateDto dto);

    /**
     * username으로 유저 조회 후 친구만 공개 유무 수정
     * @param username
     * @param isFriendOnly
     * @return
     */
    User changeFriendOnly(String username, boolean isFriendOnly);

    /**
     * username으로 유저가 만든 포스트를 조회하고, 포스트를 다 모아서 유저 프로필 보여주기
     * @param username
     * @return
     */
    UserProfile findPostByUsername(String username);

    /**
     * 로그인 유저를 기준으로 <유저, 관계> 찾기
     * @param loginMember
     * @param findMemberList
     * @return
     */
    Map<String, String> findFriendShipStatus(User loginMember, List<User> findMemberList);
}
