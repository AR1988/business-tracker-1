package de.telran.businesstracker.service;

import de.telran.businesstracker.model.Milestone;
import de.telran.businesstracker.model.Roadmap;
import de.telran.businesstracker.repositories.MilestoneRepository;
import de.telran.businesstracker.repositories.RoadmapRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class MilestoneService {

    static final String ROADMAP_DOES_NOT_EXIST = "Error! This roadmap doesn't exist in our DB";
    static final String MEMBER_DOES_NOT_EXIST = "Error! This milestone doesn't exist in our DB";

    MilestoneRepository milestoneRepository;
    RoadmapRepository roadmapRepository;

    public MilestoneService(MilestoneRepository milestoneRepository, RoadmapRepository roadmapRepository) {
        this.milestoneRepository = milestoneRepository;
        this.roadmapRepository = roadmapRepository;
    }

    public Milestone add(String name, LocalDate startDate, LocalDate finishDate, Long roadmapId) {
        Roadmap roadmap = roadmapRepository.findById(roadmapId).orElseThrow(() -> new EntityNotFoundException(ROADMAP_DOES_NOT_EXIST));
        Milestone milestone = Milestone.builder().name(name)
                .startDate(startDate).finishDate(finishDate)
                .roadmap(roadmap).build();
        milestoneRepository.save(milestone);
        return milestone;
    }

    public void edit(Long id, String name, LocalDate startDate, LocalDate finishDate) {
        Milestone milestone = getById(id);
        milestone.setName(name);
        milestone.setStartDate(startDate);
        milestone.setFinishDate(finishDate);
        milestoneRepository.save(milestone);
    }

    public List<Milestone> getAll() {
        return new ArrayList<>(milestoneRepository.findAll());
    }

    public Milestone getById(Long id) {
        return milestoneRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(MEMBER_DOES_NOT_EXIST));
    }

    public void removeById(Long id) {
        milestoneRepository.deleteById(id);
    }
}



