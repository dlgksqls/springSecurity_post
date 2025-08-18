package spring.securitystudy.friendship.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import spring.securitystudy.friendship.entity.FriendShip;
import spring.securitystudy.friendship.entity.Status;
import spring.securitystudy.user.entity.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public interface FriendShipRepository extends JpaRepository<FriendShip, Long> {
    @Query("SELECT fs FROM FriendShip fs " +
            "JOIN FETCH fs.receiveUser ru " +
            "JOIN FETCH fs.sendUser su " +
            "WHERE ru.username = :loginUserName AND fs.status = :status")
    List<FriendShip> findReceive(@Param("loginUserName") String loginUserName, @Param("status") Status status); // String 이 아닌 Enum 전달

//    @Query("SELECT fs.status " +
//            "FROM FriendShip fs " +
//            "JOIN FETCH fs.receiveMember rm " +
//            "JOIN FETCH fs.sendMember sm " +
//            "WHERE rm = :loginUser AND sm = :member")
//    Status findStatus(@Param("loginUser") Member loginUser, @Param("member") Member member);

    @Query("SELECT fs.status " + // 엔티티를 조회하는 것이 아니면 fetch join을 쓰면 안됨
            "FROM FriendShip fs " +
            "WHERE fs.receiveUser = :user AND fs.sendUser = :loginUser")
    Status findStatus(@Param("user") User user, @Param("loginUser") User loginUser);

    @Query("SELECT fs " +
            "FROM FriendShip fs " +
            "JOIN FETCH fs.receiveUser JOIN FETCH fs.sendUser " +
            "WHERE fs.receiveUser = :receiveUser AND fs.sendUser = :requestUser")
    Optional<FriendShip> findByLoginMemberAndRequestMember(@Param("receiveUser") User receiveUser, @Param("requestUser") User requestUser);

    @Query("SELECT fs " +
            "FROM FriendShip fs " +
            "JOIN FETCH fs.receiveUser rm " +
            "JOIN FETCH fs.sendUser sm " +
            "WHERE rm = :loginUser AND sm = :user AND fs.status = :status")
    Optional<FriendShip> findFriendShip(@Param("loginUser") User loginUser, @Param("user") User user, @Param("status") Status status);

    @Query("SELECT CASE " +
            "WHEN fs.sendUser = :loginUser THEN fs.receiveUser.username ELSE fs.sendUser.username END " +
            "FROM FriendShip fs " +
            "WHERE (fs.sendUser = :loginUser OR fs.receiveUser = :loginUser) " +
            "AND fs.status = :status")
    ArrayList<String> findFriendShipList(@Param("loginUser") User loginUser, @Param("status") Status status);

    @Query("SELECT DISTINCT fs FROM FriendShip fs " +
            "JOIN FETCH fs.sendUser sm " +
            "JOIN FETCH fs.receiveUser rm " +
            "WHERE (sm.username = :username OR rm.username = :username) " +
            "AND fs.status = 'ACCEPT'")  // 'FRIEND'는 친구 상태명 예시입니다
    List<FriendShip> findAllFriendsByUsername(@Param("username") String username);
}
