package spring.securitystudy.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.securitystudy.exception.user.UserNotFoundException;
import spring.securitystudy.friendship.entity.Status;
import spring.securitystudy.friendship.repository.FriendShipRepository;
import spring.securitystudy.image.entity.Image;
import spring.securitystudy.user.dto.UserProfile;
import spring.securitystudy.user.dto.UserRegisterDto;
import spring.securitystudy.user.dto.UserUpdateDto;
import spring.securitystudy.user.entity.User;
import spring.securitystudy.user.exception.UserAlreadyExistsException;
import spring.securitystudy.user.repository.UserRepository;
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
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final FriendShipRepository friendShipRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public User register(UserRegisterDto dto) {
        if (userRepository.findByUsername(dto.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException("해당 이름을 가진 유저는 이미 존재합니다.");
        }

        User user = new User();
        user.createUser(dto, passwordEncoder);
        userRepository.save(user);

        return user;
    }

    @Override
    public User findByUsername(String username){
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("해당 사용자는 없습니다."));
    }

    @Override
    public List<User> findAll(){
        return userRepository.findAll();
    }

    @Override
    public List<User> findByUsernamePrefix(String prefix) {
        return userRepository.findByUsernamePrefix(prefix)
                .orElseThrow(() -> new UserNotFoundException("해당 사용자는 없습니다."));
    }

    @Override
    @Transactional
    public void update(String username, UserUpdateDto dto) {
        User findUser = findByUsername(username);
        findUser.update(dto);
    }

    @Override
    @Transactional
    public User changeFriendOnly(String username, boolean isFriendOnly){
        User loginUser = findByUsername(username);
        loginUser.changeFriendOnly(isFriendOnly);

        return loginUser;
    }

    @Override
    public UserProfile findPostByUsername(String username) {
        User findUser = findByUsername(username);
        List<Post> postByUsername = postRepository.findByUsername(username);
        List<PostViewDto> postDto = new ArrayList<>();
        for (Post post : postByUsername) {
            List<String> imageUrls = post.getImageList().stream().map(Image::getUrl).collect(Collectors.toList());
            postDto.add(new PostViewDto(post, imageUrls, true));
        }
        return UserProfile.builder()
                .username(username)
                .role(findUser.getRole())
                .posts(postDto)
                .build();
    }

    @Override
    public Map<String, String> findFriendShipStatus(User loginMember, List<User> findMemberList) {
        Map<String, String> findMemberFriendShipList = new HashMap<>();
        for (User user : findMemberList) {
            Status status = friendShipRepository.findStatus(loginMember, user);

            if (status != null){
                findMemberFriendShipList.put(user.getUsername(), status.name());
            }
            else findMemberFriendShipList.put(user.getUsername(), null);
        }

        return findMemberFriendShipList;
    }

    private boolean isUsernameDuplicate(String username) {
        return userRepository.findByUsername(username).isPresent();
    }
}
