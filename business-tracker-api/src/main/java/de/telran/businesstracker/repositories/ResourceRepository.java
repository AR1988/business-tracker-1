package de.telran.businesstracker.repositories;

import de.telran.businesstracker.model.Resource;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResourceRepository extends CrudRepository<Resource, Long> {
    List<Resource> findAll();
}
