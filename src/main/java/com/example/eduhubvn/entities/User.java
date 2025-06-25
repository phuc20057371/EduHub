package com.example.eduhubvn.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "_user")
public class User implements UserDetails {
    @Id
    @GeneratedValue
    private Integer id;
    @Column(name = "email", unique = true, nullable = false)
    private String email;
    private String phone;
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;
    private LocalDateTime lastLogin;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private PendingLecturer pendingLecturer;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Lecturer lecturer;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private PendingEducationInstitution pendingEducationInstitution;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private EducationInstitution educationInstitution;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private PendingPartnerOrganization pendingPartnerOrganization;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private PartnerOrganization partnerOrganization;



    @Override
    public String getUsername() {
        return email;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.getGrantedAuthorities();
    }
    @Override
    public String getPassword() {
        return password;
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
