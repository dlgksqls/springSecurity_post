package spring.securitystudy.friendship.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.securitystudy.friendship.entity.FriendShip;

public interface FriendShipRepository extends JpaRepository<FriendShip, Long> {
}
