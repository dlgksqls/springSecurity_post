package spring.securitystudy.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.securitystudy.user.UserDetailsImpl;
import spring.securitystudy.user.entity.User;
import spring.securitystudy.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User member = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("해당 유저는 없습니다."));

        return new UserDetailsImpl(member);
    }
}
