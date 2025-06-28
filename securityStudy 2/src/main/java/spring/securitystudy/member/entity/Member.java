package spring.securitystudy.member.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.security.crypto.password.PasswordEncoder;
import spring.securitystudy.comment.entity.Comment;
import spring.securitystudy.friendship.entity.FriendShip;
import spring.securitystudy.member.dto.MemberRegisterDto;
import spring.securitystudy.member.dto.MemberUpdateDto;
import spring.securitystudy.post.entity.Post;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Member {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;
    private boolean isFriendOnly = false;
    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "member")
    private List<Post> postList = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Comment> commentList = new ArrayList<>();

    @OneToMany(mappedBy = "sendMember")
    private List<FriendShip> sendList = new ArrayList<>();

    @OneToMany(mappedBy = "receiveMember")
    private List<FriendShip> reveiveList = new ArrayList<>();

    public void createMember(MemberRegisterDto dto, PasswordEncoder passwordEncoder) {
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

    public void createFirstMember(PasswordEncoder passwordEncoder) {
        this.username = "admin";
        this.password = passwordEncoder.encode("1234");
        this.role = Role.USER;
    }

    public void createSecondMember(PasswordEncoder passwordEncoder) {
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

    public void removeReceive(Member requestMember) {
        this.reveiveList.removeIf(member -> member.equals(requestMember));
    }

    public void removeRequest(Member loginMember) {
        this.sendList.removeIf(member -> member.equals(loginMember));
    }

    public void update(MemberUpdateDto dto) {
        this.username = dto.getUsername();
        this.isFriendOnly = dto.isFriendOnly();
    }
}
