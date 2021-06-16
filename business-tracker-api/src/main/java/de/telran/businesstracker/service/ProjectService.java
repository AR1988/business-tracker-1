package de.telran.businesstracker.service;

import de.telran.businesstracker.model.Project;
import de.telran.businesstracker.model.User;
import de.telran.businesstracker.repositories.ProjectRepository;
import de.telran.businesstracker.repositories.UserRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProjectService {

    static final String PROJECT_DOES_NOT_EXIST = "Error! This project doesn't exist in our DB";
    static final String USER_DOES_NOT_EXIST = "Error! This user doesn't exist in our DB";

    ProjectRepository projectRepository;
    UserRepository userRepository;

    public ProjectService(ProjectRepository projectRepository, UserRepository userRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    public Project add(String name, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException(USER_DOES_NOT_EXIST));
        Project project = Project.builder().name(name).user(user).build();
        projectRepository.save(project);
        return project;
    }

    public void edit(Long id, String name) {
        Project project = getById(id);
        project.setName(name);
        projectRepository.save(project);
    }

    public List<Project> getAll() {
        return new ArrayList<>(projectRepository.findAll());
    }

    public Project getById(Long id) {
        return projectRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(PROJECT_DOES_NOT_EXIST));
    }

    public void removeById(Long id) {
        projectRepository.deleteById(id);
    }
}



