package spring.securitystudy.friendship.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import spring.securitystudy.friendship.entity.FriendShip;
import spring.securitystudy.friendship.entity.Status;
import spring.securitystudy.member.entity.Member;

import java.util.List;
import java.util.Optional;


public interface FriendShipRepository extends JpaRepository<FriendShip, Long> {
    @Query("SELECT fs FROM FriendShip fs " +
            "JOIN FETCH fs.receiveMember rm " +
            "JOIN FETCH fs.sendMember sm " +
            "WHERE rm.username = :loginUserName AND fs.status = :status")
    List<FriendShip> findReceive(@Param("loginUserName") String loginUserName, @Param("status") Status status); // String 이 아닌 Enum 전달

    @Query("SELECT fs.status " +
            "FROM FriendShip fs " +
            "JOIN FETCH fs.receiveMember rm " +
            "JOIN FETCH fs.sendMember sm " +
            "WHERE rm = :loginUser AND sm = :member")
    Status findStatus(@Param("loginUserName") Member loginUser, @Param("member") Member member);

    @Query("SELECT fs " +
            "FROM FriendShip fs " +
            "JOIN FETCH fs.receiveMember rm " +
            "JOIN FETCH fs.sendMember sm " +
            "WHERE rm = :loginUser AND sm = :member AND fs.status = :status")
    Optional<FriendShip> findFriendShip(@Param("loginUser") Member loginUser, @Param("member") Member member, @Param("status") Status status);
}
