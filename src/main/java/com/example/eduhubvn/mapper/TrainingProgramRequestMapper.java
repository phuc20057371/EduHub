package com.example.eduhubvn.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.example.eduhubvn.dtos.program.TrainingProgramRequestDTO;
import com.example.eduhubvn.entities.TrainingProgramRequest;

@Mapper(componentModel = "spring", uses = {PartnerOrganizationMapper.class})
public interface TrainingProgramRequestMapper {
    
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    TrainingProgramRequestDTO toDto(TrainingProgramRequest entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    TrainingProgramRequest toEntity(TrainingProgramRequestDTO dto);
}