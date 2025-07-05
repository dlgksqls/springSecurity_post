package spring.securitystudy.friendship.entity;

import jakarta.persistence.*;
import lombok.Getter;
import spring.securitystudy.user.entity.User;

import java.time.LocalDate;

@Entity
@Getter
public class FriendShip {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User sendUser;

    @ManyToOne(fetch = FetchType.LAZY)
    private User receiveUser;

    @Enumerated(value = EnumType.STRING)
    private Status status;
    private LocalDate createdDate;
    private LocalDate updateDate;

    public void save(User loginUser, User receiveUser) {
        this.sendUser = loginUser;
        this.receiveUser = receiveUser;
        this.status = Status.REQUEST;
        this.createdDate = LocalDate.now();
        this.updateDate = null;
    }

    public void accept() {
        this.status = Status.ACCEPT;
        this.updateDate = LocalDate.now();
    }
}
