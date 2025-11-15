package com.example.eduhubvn.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import static com.example.eduhubvn.enums.Permission.*;

import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Getter
public enum Role {
    USER(Collections.emptySet()),
    SCHOOL (
            Set.of(
                    SCHOOL_READ,
                    SCHOOL_CREATE,
                    SCHOOL_UPDATE,
                    SCHOOL_DELETE

            )
    ),
    ORGANIZATION(
            Set.of(
                    ORGANIZATION_READ,
                    ORGANIZATION_CREATE,
                    ORGANIZATION_UPDATE,
                    ORGANIZATION_DELETE

            )
    ),
    LECTURER(
            Set.of(
                    LECTURER_READ,
                    LECTURER_CREATE,
                    LECTURER_UPDATE,
                    LECTURER_DELETE
            )
    ),
    SUB_ADMIN(Collections.emptySet()), // Dynamic permissions will be assigned
    ADMIN (EnumSet.allOf(Permission.class));
    private final Set<Permission> permissions;

    public List<SimpleGrantedAuthority> getGrantedAuthorities(){
        List<SimpleGrantedAuthority> authorities = permissions.stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toList());
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return authorities;
    }
}
