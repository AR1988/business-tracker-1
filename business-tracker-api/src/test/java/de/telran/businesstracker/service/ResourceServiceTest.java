package de.telran.businesstracker.service;

import de.telran.businesstracker.model.Member;
import de.telran.businesstracker.model.Milestone;
import de.telran.businesstracker.model.Resource;
import de.telran.businesstracker.model.Task;
import de.telran.businesstracker.repositories.ResourceRepository;
import de.telran.businesstracker.repositories.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ResourceServiceTest {

    @Mock
    TaskRepository taskRepository;

    @Mock
    ResourceRepository resourceRepository;

    @InjectMocks
    ResourceService resourceService;

    @Test
    public void testAdd_success() {
        Member member = new Member();
        Milestone milestone = new Milestone();
        Task task = new Task(2L, "Task", false, milestone, member);

        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));

        Resource resource = Resource.builder()
                .id(1L)
                .name("Resource")
                .hours(100)
                .cost(1290.00)
                .task(task)
                .build();


        resourceService.add(resource.getName(), resource.getHours(), resource.getCost(),
                resource.getTask().getId());

        verify(resourceRepository, times(1)).save(any());
        verify(resourceRepository, times(1))
                .save(argThat(savedResource -> savedResource.getName().equals(resource.getName()) &&
                        savedResource.getHours().equals(resource.getHours()) &&
                        savedResource.getCost().equals(resource.getCost()) &&
                        savedResource.getTask().getId().equals(resource.getTask().getId())
                ));
    }

    @Test
    public void testAdd_taskDoesNotExist_EntityNotFoundException() {
        Member member = new Member();
        Milestone milestone = new Milestone();
        Task task = new Task(2L, "Task", false, milestone, member);

        Resource resource = Resource.builder()
                .id(1L)
                .name("Resource")
                .hours(100)
                .cost(1290.00)
                .task(task)
                .build();

        Exception exception = assertThrows(EntityNotFoundException.class, () ->
                resourceService.add(resource.getName(), resource.getHours(), resource.getCost(),
                        resource.getTask().getId()));

        verify(taskRepository, times(1)).findById(any());
        assertEquals("Error! This task doesn't exist in our DB", exception.getMessage());
    }

    @Test
    public void resourceEdit_resourceExist_fieldsChanged() {
        Member member = new Member();
        Milestone milestone = new Milestone();
        Task task = new Task(2L, "Task", false, milestone, member);

        Resource resource = Resource.builder()
                .id(1L)
                .name("Resource")
                .hours(100)
                .cost(1290.00)
                .task(task)
                .build();

        String newName = "New milestone";
        Integer newHours = 80;
        Double newCast = 5000.00;

        when(resourceRepository.findById(resource.getId())).thenReturn(Optional.of(resource));
        resourceService.edit(resource.getId(), newName, newHours, newCast);

        verify(resourceRepository, times(1)).save(any());
        verify(resourceRepository, times(1))
                .save(argThat(savedResource -> savedResource.getName().equals(newName) &&
                        savedResource.getHours().equals(newHours) &&
                        savedResource.getCost().equals(newCast) &&
                        savedResource.getTask().getId().equals(task.getId()))
                );
    }

    @Test
    void getAll_twoObjects() {
        Member member = new Member();
        Milestone milestone = new Milestone();
        Task task = new Task(2L, "Task", false, milestone, member);

        Resource resource1 = Resource.builder()
                .id(1L)
                .name("Resource")
                .hours(100)
                .cost(1290.00)
                .task(task)
                .build();

        Resource resource2 = Resource.builder()
                .id(2L)
                .name("Resource")
                .hours(100)
                .cost(1290.00)
                .task(task)
                .build();

        List<Resource> resources = new ArrayList<>();

        resources.add(resource1);
        resources.add(resource2);

        when(resourceRepository.findAll()).thenReturn(resources);

        assertEquals(resource1.getName(), resourceService.getAll().get(0).getName());
        assertEquals(resource1.getHours(), resourceService.getAll().get(0).getHours());
        assertEquals(resource1.getCost(), resourceService.getAll().get(0).getCost());
        assertEquals(resource1.getTask(), resourceService.getAll().get(0).getTask());

        assertEquals(resource2.getName(), resourceService.getAll().get(1).getName());
        assertEquals(resource2.getHours(), resourceService.getAll().get(1).getHours());
        assertEquals(resource2.getCost(), resourceService.getAll().get(1).getCost());
        assertEquals(resource2.getTask(), resourceService.getAll().get(1).getTask());
    }

    @Test
    void testGetById_objectExist() {
        Member member = new Member();
        Milestone milestone = new Milestone();
        Task task = new Task(2L, "Task", false, milestone, member);

        Resource resource = Resource.builder()
                .id(1L)
                .name("Resource")
                .hours(100)
                .cost(1290.00)
                .task(task)
                .build();

        when(resourceRepository.findById(resource.getId())).thenReturn(Optional.of(resource));
        Resource expectedResource = resourceService.getById(resource.getId());

        assertEquals(expectedResource.getName(), resource.getName());
        assertEquals(expectedResource.getHours(), resource.getHours());
        assertEquals(expectedResource.getCost(), resource.getCost());
        assertEquals(expectedResource.getTask(), resource.getTask());

        verify(resourceRepository, times(1)).findById(argThat(
                id -> id.equals(resource.getId())));
    }

    @Test
    void testGetById_objectNotExist() {
        Member member = new Member();
        Milestone milestone = new Milestone();
        Task task = new Task(2L, "Task", false, milestone, member);

        Resource resource = Resource.builder()
                .id(1L)
                .name("Resource")
                .hours(100)
                .cost(1290.00)
                .task(task)
                .build();

        Exception exception = assertThrows(EntityNotFoundException.class,
                () -> resourceService.getById(resource.getId() + 1));

        verify(resourceRepository, times(1)).findById(any());
        assertEquals("Error! This resource doesn't exist in our DB", exception.getMessage());

    }

    @Captor
    ArgumentCaptor<Resource> taskArgumentCaptor;

    @Test
    void removeById_oneObjectDeleted() {
        Member member = new Member();
        Milestone milestone = new Milestone();
        Task task = new Task(2L, "Task", false, milestone, member);

        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));

        Resource resource = Resource.builder()
                .id(1L)
                .name("Resource")
                .hours(100)
                .cost(1290.00)
                .task(task)
                .build();

        resourceService.add(resource.getName(), resource.getHours(), resource.getCost(),
                resource.getTask().getId());
        resourceService.removeById(resource.getId());

        List<Resource> capturedResources = taskArgumentCaptor.getAllValues();
        verify(resourceRepository, times(1)).deleteById(resource.getId());
        assertEquals(0, capturedResources.size());
    }
}
