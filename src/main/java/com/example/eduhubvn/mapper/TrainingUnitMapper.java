package com.example.eduhubvn.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.example.eduhubvn.dtos.program.TrainingUnitDTO;
import com.example.eduhubvn.entities.TrainingUnit;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface TrainingUnitMapper {

    @Mapping(target = "trainingProgram", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    TrainingUnit toEntity(TrainingUnitDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    TrainingUnitDTO toDTO(TrainingUnit dto);

}
