package com.example.eduhubvn.services;



import com.example.eduhubvn.dtos.*;

import com.example.eduhubvn.dtos.edu.EducationInstitutionDTO;
import com.example.eduhubvn.dtos.lecturer.*;
import com.example.eduhubvn.entities.*;
import com.example.eduhubvn.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public Optional<User> getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(principal instanceof User user)) {
            throw new IllegalStateException("Không tìm thấy user đang đăng nhập");
        }
        Optional<User> u = userRepository.findById(user.getId());
        if (u.isEmpty()) {
            throw new IllegalStateException("Không tìm thấy user đang đăng nhập");
        }
        return u;
    }

}
