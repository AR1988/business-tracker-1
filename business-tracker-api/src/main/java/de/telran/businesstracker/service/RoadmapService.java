package de.telran.businesstracker.service;

import de.telran.businesstracker.model.Project;
import de.telran.businesstracker.model.Roadmap;
import de.telran.businesstracker.repositories.ProjectRepository;
import de.telran.businesstracker.repositories.RoadmapRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class RoadmapService {

    static final String ROADMAP_DOES_NOT_EXIST = "Error! This roadmap doesn't exist in our DB";
    static final String PROJECT_DOES_NOT_EXIST = "Error! This project doesn't exist in our DB";

    RoadmapRepository roadmapRepository;
    ProjectRepository projectRepository;

    public RoadmapService(RoadmapRepository roadmapRepository, ProjectRepository projectRepository) {
        this.roadmapRepository = roadmapRepository;
        this.projectRepository = projectRepository;
    }

    public Roadmap add(String name, LocalDate startDate, Long projectId) {
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new EntityNotFoundException(PROJECT_DOES_NOT_EXIST));
        Roadmap roadmap = Roadmap.builder().name(name).startDate(startDate).project(project).build();
        roadmapRepository.save(roadmap);
        return roadmap;
    }

    public void edit(Long id, String name, LocalDate startDate) {
        Roadmap roadmap = getById(id);
        roadmap.setName(name);
        roadmap.setStartDate(startDate);
        roadmapRepository.save(roadmap);
    }

    public List<Roadmap> getAll() {
        return new ArrayList<>(roadmapRepository.findAll());
    }

    public Roadmap getById(Long id) {
        return roadmapRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(ROADMAP_DOES_NOT_EXIST));
    }

    public void removeById(Long id) {
        roadmapRepository.deleteById(id);
    }
}



