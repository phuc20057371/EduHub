package com.example.eduhubvn.mapper;

import com.example.eduhubvn.dtos.lecturer.ResearchProjectDTO;
import com.example.eduhubvn.dtos.lecturer.request.ResearchProjectReq;
import com.example.eduhubvn.dtos.lecturer.request.ResearchProjectUpdateReq;
import com.example.eduhubvn.entities.ResearchProject;
import com.example.eduhubvn.entities.ResearchProjectUpdate;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ResearchProjectMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "adminNote", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "lecturer", ignore = true)
    ResearchProject toEntity(ResearchProjectReq req);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "adminNote", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "researchProject", ignore = true)
    ResearchProjectUpdate toEntity(ResearchProjectUpdateReq req);

    ResearchProjectDTO toDTO(ResearchProject entity);

    ResearchProjectDTO toDTO(ResearchProjectUpdate entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "adminNote", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "lecturer", ignore = true)
    void updateEntityFromRequest(ResearchProjectUpdateReq req, @MappingTarget ResearchProject project);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "adminNote", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "researchProject", ignore = true)
    void updateUpdateFromRequest(ResearchProjectUpdateReq req, @MappingTarget ResearchProjectUpdate project);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "adminNote", ignore = true)
    @Mapping(target = "lecturer", ignore = true)
    void updateEntityFromUpdate(ResearchProjectUpdate update, @MappingTarget ResearchProject original);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "adminNote", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    List<ResearchProjectDTO> toDTOs(List<ResearchProject> researchProjects);
}
