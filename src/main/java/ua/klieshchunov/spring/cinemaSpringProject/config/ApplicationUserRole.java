package ua.klieshchunov.spring.cinemaSpringProject.config;

import com.google.common.collect.Sets;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

import static ua.klieshchunov.spring.cinemaSpringProject.config.ApplicationUserPermission.*;

public enum ApplicationUserRole {
    USER, ADMIN, MANAGER

    //Uncomment to get authorities-feature (does not work correctly)
//    USER(Sets.newHashSet(
//            SEANCE_READ, MOVIE_READ
//    )),
//    ADMIN(Sets.newHashSet(
//            MOVIE_READ, MOVIE_WRITE,
//            SEANCE_READ, SEANCE_WRITE,
//            USER_READ, USER_WRITE,
//            TICKET_READ, TICKET_WRITE
//    )),
//    MANAGER(Sets.newHashSet(
//            MOVIE_READ, MOVIE_WRITE,
//            SEANCE_READ, SEANCE_WRITE,
//            USER_READ,
//            TICKET_READ, TICKET_WRITE
//    ));
//
//    private final Set<ApplicationUserPermission> permissions;
//
//    ApplicationUserRole(Set<ApplicationUserPermission> permissions) {
//        this.permissions = permissions;
//    }
//
//    public Set<ApplicationUserPermission> getPermissions() {
//        return permissions;
//    }
//
//    public Set<SimpleGrantedAuthority> getGrantedAuthority() {
//        Set<SimpleGrantedAuthority> permissions = getPermissions().stream()
//                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
//                .collect(Collectors.toSet());
//        permissions.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
//        return permissions;
//    }
}
