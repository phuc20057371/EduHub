package com.example.eduhubvn.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.example.eduhubvn.dtos.program.TrainingProgramDTO;
import com.example.eduhubvn.dtos.program.TrainingProgramReq;
import com.example.eduhubvn.dtos.program.TrainingUnitDTO;
import com.example.eduhubvn.entities.TrainingProgram;
import com.example.eduhubvn.entities.TrainingUnit;

@Mapper(componentModel = "spring", uses = {
        UserMapper.class,
        PartnerOrganizationMapper.class,
        TrainingProgramRequestMapper.class,
        LecturerMapper.class
})
public interface TrainingProgramMapper {

    @Mapping(target = "user", source = "user")
    @Mapping(target = "partnerOrganization", source = "partnerOrganization")
    @Mapping(target = "trainingProgramRequest", source = "trainingProgramRequest")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    TrainingProgramDTO toDto(TrainingProgram entity);

    @Mapping(target = "units", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    TrainingProgram toEntity(TrainingProgramDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    TrainingUnit toUnitEntity(TrainingUnitDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "units", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "partnerOrganization", ignore = true)
    @Mapping(target = "trainingProgramRequest", ignore = true)
    @Mapping(target = "id", ignore = true)
    TrainingProgram fromReq(TrainingProgramReq req);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "units", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "partnerOrganization", ignore = true)
    @Mapping(target = "trainingProgramRequest", ignore = true)
    @Mapping(target = "id", ignore = true)
    void updateFromReq(TrainingProgramReq req, @MappingTarget TrainingProgram entity);

}
