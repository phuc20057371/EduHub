package com.example.eduhubvn.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.example.eduhubvn.dtos.UserProfileDTO;
import com.example.eduhubvn.entities.User;

@Mapper(componentModel = "spring", uses = {
    LecturerMapper.class,
    EducationInstitutionMapper.class,
    PartnerOrganizationMapper.class
})
public interface UserMapper {
    
    @Mapping(target = "role", expression = "java(entity.getRole() != null ? entity.getRole().name() : null)")
    @Mapping(target = "subEmails", expression = "java(entity.getSubEmails() != null ? entity.getSubEmails().stream().toList() : null)")
    @Mapping(target = "permissions", ignore = true) // This will be handled separately for SUB_ADMIN
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    UserProfileDTO toDto(User entity);

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "enabled", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "subAdminPermissions", ignore = true)
    @Mapping(target = "trainingPrograms", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    User toEntity(UserProfileDTO dto);
}