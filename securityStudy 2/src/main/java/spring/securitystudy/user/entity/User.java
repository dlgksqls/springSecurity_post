package spring.securitystudy.user.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.security.crypto.password.PasswordEncoder;
import spring.securitystudy.comment.entity.Comment;
import spring.securitystudy.friendship.entity.FriendShip;
import spring.securitystudy.like.entity.Like;
import spring.securitystudy.user.dto.UserRegisterDto;
import spring.securitystudy.user.dto.UserUpdateDto;
import spring.securitystudy.post.entity.Post;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "users")
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(unique = true, nullable = false)
    private String username;

    private String password;
    private boolean isFriendOnly = false;

    @Enumerated(EnumType.STRING)
    private Role role;

    private boolean enable = false; // 이메일 인증 여부

    @OneToMany(mappedBy = "user",  cascade = CascadeType.REMOVE)
    private List<Post> postList = new ArrayList<>();

    @OneToMany(mappedBy = "user",  cascade = CascadeType.REMOVE)
    private List<Comment> commentList = new ArrayList<>();

    @OneToMany(mappedBy = "sendUser",  cascade = CascadeType.REMOVE)
    private List<FriendShip> sendList = new ArrayList<>();

    @OneToMany(mappedBy = "receiveUser",  cascade = CascadeType.REMOVE)
    private List<FriendShip> reveiveList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Like> likeList = new ArrayList<>();

    public void createUser(UserRegisterDto dto, PasswordEncoder passwordEncoder) {
        this.username = dto.getUsername();
        this.password = passwordEncoder.encode(dto.getPassword());
        this.role = Role.USER;
    }

    public void addPost(Post post) {
        postList.add(post);
    }
    public void addComment(Comment comment) {
        this.commentList.add(comment);
    }

    public void createFirstUser(PasswordEncoder passwordEncoder) {
        this.username = "admin";
        this.password = passwordEncoder.encode("1234");
        this.role = Role.USER;
    }

    public void createSecondUser(PasswordEncoder passwordEncoder) {
        this.username = "user";
        this.password = passwordEncoder.encode("1234");
        this.role = Role.USER;
    }

    public void requestFriendShip(FriendShip newFriendShip) {
        this.sendList.add(newFriendShip);
    }

    public void receiveFriendShip(FriendShip newFriendShip) {
        this.reveiveList.add(newFriendShip);
    }

    public void removeReceive(User requestMember) {
        this.reveiveList.removeIf(member -> member.equals(requestMember));
    }

    public void removeRequest(User loginMember) {
        this.sendList.removeIf(member -> member.equals(loginMember));
    }

    public void update(UserUpdateDto dto) {
        this.username = dto.getUsername();
        this.isFriendOnly = dto.isFriendOnly();
    }

    public void changeFriendOnly(boolean friendOnly) {
        if (this.isFriendOnly == friendOnly) return;
        this.isFriendOnly = friendOnly;
    }

    public void addLike(Like like) {
        this.likeList.add(like);
    }

    public void removeLike(Like deleteLike) {
        this.likeList.remove(deleteLike);
    }

    public void enable() {
        this.enable = true;
    }
}
