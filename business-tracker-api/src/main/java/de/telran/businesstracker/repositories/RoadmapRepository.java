package de.telran.businesstracker.repositories;

import de.telran.businesstracker.model.Roadmap;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoadmapRepository extends CrudRepository<Roadmap, Long> {
    List<Roadmap> findAll();
}
