package de.telran.businesstracker.controller;

import de.telran.businesstracker.model.Project;
import de.telran.businesstracker.controller.dto.ProjectDto;
import de.telran.businesstracker.mapper.ProjectMapper;
import de.telran.businesstracker.service.ProjectService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/project")
public class ProjectController {

    private final ProjectService projectService;
    private final ProjectMapper projectMapper;

    public ProjectController(ProjectService projectService, ProjectMapper projectMapper) {
        this.projectService = projectService;
        this.projectMapper = projectMapper;
    }

    @PostMapping("")
    public ResponseEntity<ProjectDto> createProject(@RequestBody @Valid ProjectDto projectDto) throws URISyntaxException {
        Project project = projectService.add(projectDto.name, projectDto.userId);
        projectDto.id = project.getId();
        return ResponseEntity
                .created(new URI("/api/projects/" + project.getId()))
                .body(projectDto);
    }

    @PutMapping("")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateProject(@RequestBody @Valid ProjectDto projectDto) throws HttpClientErrorException.BadRequest {
        projectService.edit(projectDto.id, projectDto.name);
    }

    @GetMapping("")
    public List<ProjectDto> getAllProjects() {
        return projectService.getAll()
                .stream()
                .map(projectMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ProjectDto getProject(@PathVariable Long id) {
        Project project = projectService.getById(id);
        return projectMapper.toDto(project);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProject(@PathVariable Long id) {
        projectService.removeById(id);
    }
}
