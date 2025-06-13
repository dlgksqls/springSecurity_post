package spring.securitystudy.friendship.entity;

import jakarta.persistence.*;
import lombok.Getter;
import spring.securitystudy.member.entity.Member;

import java.time.LocalDate;

@Entity
@Getter
public class FriendShip {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member sendMember;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member receiveMember;

    @Enumerated(value = EnumType.STRING)
    private Status status;
    private LocalDate createdDate;
    private LocalDate updateDate;

    public void save(Member loginUser, Member receiveUser) {
        this.sendMember = loginUser;
        this.receiveMember = receiveUser;
        this.status = Status.REQUEST;
        this.createdDate = LocalDate.now();
        this.updateDate = null;
    }

    public void accept() {
        this.status = Status.ACCEPT;
        this.updateDate = LocalDate.now();
    }
}
