package de.telran.businesstracker.service;

import de.telran.businesstracker.model.Resource;
import de.telran.businesstracker.model.Task;
import de.telran.businesstracker.repositories.ResourceRepository;
import de.telran.businesstracker.repositories.TaskRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ResourceService {

    static final String RESOURCE_DOES_NOT_EXIST = "Error! This resource doesn't exist in our DB";
    static final String TASK_DOES_NOT_EXIST = "Error! This task doesn't exist in our DB";

    ResourceRepository resourceRepository;
    TaskRepository taskRepository;

    public ResourceService(ResourceRepository resourceRepository, TaskRepository taskRepository) {
        this.resourceRepository = resourceRepository;
        this.taskRepository = taskRepository;
    }

    public Resource add(String name, Integer hours, Double cost, Long taskId) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new EntityNotFoundException(TASK_DOES_NOT_EXIST));
        Resource resource = Resource.builder().name(name).hours(hours).cost(cost).task(task).build();
        resourceRepository.save(resource);
        return resource;
    }

    public void edit(Long id, String name, Integer hours, Double cost) {
        Resource resource = getById(id);
        resource.setName(name);
        resource.setHours(hours);
        resource.setCost(cost);
        resourceRepository.save(resource);
    }

    public List<Resource> getAll() {
        return new ArrayList<>(resourceRepository.findAll());
    }

    public Resource getById(Long id) {
        return resourceRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(RESOURCE_DOES_NOT_EXIST));
    }

    public void removeById(Long id) {
        resourceRepository.deleteById(id);
    }
}



