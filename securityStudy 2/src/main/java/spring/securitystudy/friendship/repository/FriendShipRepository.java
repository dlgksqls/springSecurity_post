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

//    @Query("SELECT fs.status " +
//            "FROM FriendShip fs " +
//            "JOIN FETCH fs.receiveMember rm " +
//            "JOIN FETCH fs.sendMember sm " +
//            "WHERE rm = :loginUser AND sm = :member")
//    Status findStatus(@Param("loginUser") Member loginUser, @Param("member") Member member);

    @Query("SELECT fs.status " + // 엔티티를 조회하는 것이 아니면 fetch join을 쓰면 안됨
            "FROM FriendShip fs " +
            "WHERE fs.receiveMember = :member AND fs.sendMember = :loginUser")
    Status findStatus(@Param("loginUser") Member loginUser, @Param("member") Member member);

    @Query("SELECT fs " +
            "FROM FriendShip fs " +
            "JOIN FETCH fs.receiveMember JOIN FETCH fs.sendMember " +
            "WHERE fs.receiveMember = :receiveMember AND fs.sendMember = :requestMember")
    FriendShip findByLoginMemberAndRequestMember(@Param("receiveMember") Member receiveMember, @Param("requestMember") Member requestMember);

    @Query("SELECT fs " +
            "FROM FriendShip fs " +
            "JOIN FETCH fs.receiveMember rm " +
            "JOIN FETCH fs.sendMember sm " +
            "WHERE rm = :loginUser AND sm = :member AND fs.status = :status")
    Optional<FriendShip> findFriendShip(@Param("loginUser") Member loginUser, @Param("member") Member member, @Param("status") Status status);
}
