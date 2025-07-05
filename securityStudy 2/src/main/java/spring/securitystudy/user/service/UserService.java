package spring.securitystudy.user.service;

import spring.securitystudy.user.dto.UserProfile;
import spring.securitystudy.user.dto.UserRegisterDto;
import spring.securitystudy.user.dto.UserUpdateDto;
import spring.securitystudy.user.entity.User;

import java.util.List;
import java.util.Map;

public interface UserService {
    User register(UserRegisterDto dto);
    User findByUsername(String username);
    List<User> findAll();
    List<User> findByUsernamePrefix(String username);
    void update(String username, UserUpdateDto dto);
    User changeFriendOnly(String username, boolean isFriendOnly);
    UserProfile findPostByUsername(String username);
    Map<String, String> findFriendShipStatus(User loginMember, List<User> findMemberList);
}
