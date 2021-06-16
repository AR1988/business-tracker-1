package de.telran.businesstracker.mapper;

import de.telran.businesstracker.model.Project;
import de.telran.businesstracker.controller.dto.ProjectDto;
import org.springframework.stereotype.Component;

@Component
public class ProjectMapper {

    public ProjectDto toDto(Project project) {
        return ProjectDto.builder()
                .id(project.getId())
                .name(project.getName())
                .userId(project.getUser().getId())
                .build();
    }
}
