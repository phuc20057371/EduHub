package com.example.eduhubvn.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.example.eduhubvn.dtos.UserProfileDTO;
import com.example.eduhubvn.entities.User;

@Mapper(componentModel = "spring", uses = {
        LecturerMapper.class,
        InstitutionMapper.class,
        PartneMapper.class
}, unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface UserMapper {

    @Mapping(target = "role", expression = "java(entity.getRole() != null ? entity.getRole().name() : null)")
    @Mapping(target = "subEmails", expression = "java(entity.getSubEmails() != null ? entity.getSubEmails().stream().toList() : null)")
    @Mapping(target = "permissions", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    UserProfileDTO toDto(User entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    User toEntity(UserProfileDTO dto);
}