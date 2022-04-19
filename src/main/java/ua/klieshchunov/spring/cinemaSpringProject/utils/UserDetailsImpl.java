package ua.klieshchunov.spring.cinemaSpringProject.utils;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ua.klieshchunov.spring.cinemaSpringProject.model.entity.User;

import java.util.Collection;
import java.util.Set;

public class UserDetailsImpl implements UserDetails {
    private final User user;
    private final Set<? extends GrantedAuthority> grantedAuthorities;

    public UserDetailsImpl(User user, Set<? extends GrantedAuthority> grantedAuthorities) {
        this.user = user;
        this.grantedAuthorities = grantedAuthorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return grantedAuthorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
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
}
