package spring.securitystudy.friendship.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import spring.securitystudy.friendship.entity.FriendShip;
import spring.securitystudy.friendship.repository.FriendShipRepository;
import spring.securitystudy.member.entity.Member;

@Service
@RequiredArgsConstructor
public class FriendShipService {
    private final FriendShipRepository friendShipRepository;

    public void add(Member loginUser, Member receiveUser) {
        FriendShip newFriendShip = new FriendShip();
        newFriendShip.save(loginUser, receiveUser);

        friendShipRepository.save(newFriendShip);

        loginUser.requestFriendShip(newFriendShip);
        receiveUser.reveiveFriendShip(newFriendShip);
    }
}
