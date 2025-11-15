package com.example.eduhubvn.services;

import com.example.eduhubvn.entities.SubAdminPermission;
import com.example.eduhubvn.entities.User;
import com.example.eduhubvn.enums.Role;
import com.example.eduhubvn.repositories.SubAdminPermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DynamicAuthoritiesService {
    
    private final SubAdminPermissionRepository subAdminPermissionRepository;
    
    public Collection<? extends GrantedAuthority> getAuthoritiesForUser(User user) {
        List<GrantedAuthority> authorities = new ArrayList<>(user.getRole().getGrantedAuthorities());
        
        // If user is SUB_ADMIN, add their dynamic permissions
        if (user.getRole() == Role.SUB_ADMIN) {
            List<SubAdminPermission> permissions = subAdminPermissionRepository.findByUserId(user.getId());
            for (SubAdminPermission permission : permissions) {
                authorities.add(new SimpleGrantedAuthority(permission.getPermission().getPermission()));
            }
        }
        
        return authorities;
    }
}
