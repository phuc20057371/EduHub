package com.example.eduhubvn.entities;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.example.eduhubvn.entities.Permission.*;

@RequiredArgsConstructor
@Getter
public enum Role {
    USER(Collections.emptySet()),
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
    );

    private final Set<Permission> permissions;

    public List<SimpleGrantedAuthority> getGrantedAuthorities(){
        List<SimpleGrantedAuthority> authorities = permissions.stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toList());
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return authorities;
    }
}
