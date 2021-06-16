package de.telran.businesstracker.repositories;

import de.telran.businesstracker.model.Milestone;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MilestoneRepository extends CrudRepository<Milestone, Long> {
    List<Milestone> findAll();

    List<Milestone> findAllByRoadmapProjectId(Long projectId);
}
