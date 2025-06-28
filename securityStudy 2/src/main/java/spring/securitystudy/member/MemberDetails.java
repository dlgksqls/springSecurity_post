package spring.securitystudy.member;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import spring.securitystudy.member.entity.Member;
import spring.securitystudy.member.entity.Role;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Data
public class MemberDetails implements UserDetails {

    private final Member member;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + member.getRole()));
    }

    @Override
    public String getPassword() {
        return member.getPassword();
    }

    @Override
    public String getUsername() {
        return member.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MemberDetails)) return false;
        MemberDetails that = (MemberDetails) o;
        return Objects.equals(this.getMember().getId(), that.getMember().getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMember().getId());
    }
}
