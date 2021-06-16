package de.telran.businesstracker.service;

import de.telran.businesstracker.model.Milestone;
import de.telran.businesstracker.model.Project;
import de.telran.businesstracker.model.Roadmap;
import de.telran.businesstracker.model.User;
import de.telran.businesstracker.repositories.MilestoneRepository;
import de.telran.businesstracker.repositories.RoadmapRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
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
class MilestoneServiceTest {

    @Mock
    MilestoneRepository milestoneRepository;

    @Mock
    RoadmapRepository roadmapRepository;

    @InjectMocks
    MilestoneService milestoneService;

    @Test
    public void testAdd_success() {
        User user = new User(2L);
        Project project = new Project(4L, "Great project", user);
        Roadmap roadmap = new Roadmap(3L, "Roadmap", LocalDate.now(), project);

        when(roadmapRepository.findById(roadmap.getId())).thenReturn(Optional.of(roadmap));

        Milestone milestone = Milestone.builder()
                .id(1L)
                .name("Milestone")
                .startDate(LocalDate.now())
                .finishDate(LocalDate.now().plusDays(10))
                .roadmap(roadmap)
                .build();


        milestoneService.add(milestone.getName(), milestone.getStartDate()
                , milestone.getFinishDate(), milestone.getRoadmap().getId());

        verify(milestoneRepository, times(1)).save(any());
        verify(milestoneRepository, times(1))
                .save(argThat(savedMilestone -> savedMilestone.getName().equals(milestone.getName()) &&
                        savedMilestone.getStartDate().equals(milestone.getStartDate()) &&
                        savedMilestone.getFinishDate().equals(milestone.getFinishDate()) &&
                        savedMilestone.getRoadmap().getId().equals(roadmap.getId()))
                );
    }

    @Test
    public void testAdd_roadmapDoesNotExist_EntityNotFoundException() {
        User user = new User(2L);
        Project project = new Project(4L, "Great project", user);
        Roadmap roadmap = new Roadmap(3L, "Roadmap", LocalDate.now(), project);

        Milestone milestone = Milestone.builder()
                .id(1L)
                .name("Milestone")
                .startDate(LocalDate.now())
                .finishDate(LocalDate.now().plusDays(10))
                .roadmap(roadmap)
                .build();

        Exception exception = assertThrows(EntityNotFoundException.class, () ->
                milestoneService.add(milestone.getName(), milestone.getStartDate()
                        , milestone.getFinishDate(), milestone.getRoadmap().getId()));

        verify(roadmapRepository, times(1)).findById(any());
        assertEquals("Error! This roadmap doesn't exist in our DB", exception.getMessage());
    }

    @Test
    public void milestoneEdit_milestoneExist_fieldsChanged() {

        User user = new User(2L);
        Project project = new Project(4L, "Great project", user);
        Roadmap roadmap = new Roadmap(3L, "Roadmap", LocalDate.now(), project);

        Milestone milestone = Milestone.builder()
                .id(1L)
                .name("Milestone")
                .startDate(LocalDate.now())
                .finishDate(LocalDate.now().plusDays(10))
                .roadmap(roadmap)
                .build();

        String newName = "New milestone";
        LocalDate newStartDay = LocalDate.now().plusDays(1);
        LocalDate newFinishDay = LocalDate.now().plusDays(15);

        when(milestoneRepository.findById(milestone.getId())).thenReturn(Optional.of(milestone));
        milestoneService.edit(milestone.getId(), newName, newStartDay, newFinishDay);

        verify(milestoneRepository, times(1)).save(any());
        verify(milestoneRepository, times(1))
                .save(argThat(savedMilestone -> savedMilestone.getName().equals(newName) &&
                        savedMilestone.getStartDate().equals(newStartDay) &&
                        savedMilestone.getFinishDate().equals(newFinishDay) &&
                        savedMilestone.getRoadmap().getId().equals(roadmap.getId()))
                );
    }

    @Test
    void getAll_twoObjects() {
        User user = new User(2L);
        Project project = new Project(4L, "Great project", user);
        Roadmap roadmap = new Roadmap(3L, "Roadmap", LocalDate.now(), project);

        Milestone milestone1 = Milestone.builder()
                .id(1L)
                .name("Milestone")
                .startDate(LocalDate.now())
                .finishDate(LocalDate.now().plusDays(10))
                .roadmap(roadmap)
                .build();

        Milestone milestone2 = Milestone.builder()
                .id(2L)
                .name("Milestone")
                .startDate(LocalDate.now())
                .finishDate(LocalDate.now().plusDays(10))
                .roadmap(roadmap)
                .build();

        List<Milestone> milestones = new ArrayList<>();

        milestones.add(milestone1);
        milestones.add(milestone2);

        when(milestoneRepository.findAll()).thenReturn(milestones);

        assertEquals(milestone1.getName(), milestoneService.getAll().get(0).getName());
        assertEquals(milestone1.getStartDate(), milestoneService.getAll().get(0).getStartDate());
        assertEquals(milestone1.getFinishDate(), milestoneService.getAll().get(0).getFinishDate());
        assertEquals(milestone1.getRoadmap(), milestoneService.getAll().get(0).getRoadmap());

        assertEquals(milestone2.getName(), milestoneService.getAll().get(1).getName());
        assertEquals(milestone2.getStartDate(), milestoneService.getAll().get(1).getStartDate());
        assertEquals(milestone2.getFinishDate(), milestoneService.getAll().get(1).getFinishDate());
        assertEquals(milestone2.getRoadmap(), milestoneService.getAll().get(1).getRoadmap());
    }

    @Test
    void testGetById_objectExist() {
        User user = new User(2L);
        Project project = new Project(4L, "Great project", user);
        Roadmap roadmap = new Roadmap(3L, "Roadmap", LocalDate.now(), project);

        Milestone milestone = Milestone.builder()
                .id(1L)
                .name("Milestone")
                .startDate(LocalDate.now())
                .finishDate(LocalDate.now().plusDays(10))
                .roadmap(roadmap)
                .build();

        when(milestoneRepository.findById(milestone.getId())).thenReturn(Optional.of(milestone));
        Milestone expectedMilestone = milestoneService.getById(milestone.getId());

        assertEquals(expectedMilestone.getName(), milestone.getName());
        assertEquals(expectedMilestone.getStartDate(), milestone.getStartDate());
        assertEquals(expectedMilestone.getFinishDate(), milestone.getFinishDate());
        assertEquals(expectedMilestone.getRoadmap(), milestone.getRoadmap());

        verify(milestoneRepository, times(1)).findById(argThat(
                id -> id.equals(milestone.getId())));
    }

    @Test
    void testGetById_objectNotExist() {
        User user = new User(2L);
        Project project = new Project(4L, "Great project", user);
        Roadmap roadmap = new Roadmap(3L, "Roadmap", LocalDate.now(), project);

        Milestone milestone = Milestone.builder()
                .id(1L)
                .name("Milestone")
                .startDate(LocalDate.now())
                .finishDate(LocalDate.now().plusDays(10))
                .roadmap(roadmap)
                .build();

        Exception exception = assertThrows(EntityNotFoundException.class,
                () -> milestoneService.getById(milestone.getId() + 1));

        verify(milestoneRepository, times(1)).findById(any());
        assertEquals("Error! This milestone doesn't exist in our DB", exception.getMessage());

    }

    @Captor
    ArgumentCaptor<Milestone> taskArgumentCaptor;

    @Test
    void removeById_oneObjectDeleted() {
        User user = new User(2L);
        Project project = new Project(4L, "Great project", user);
        Roadmap roadmap = new Roadmap(3L, "Roadmap", LocalDate.now(), project);

        when(roadmapRepository.findById(roadmap.getId())).thenReturn(Optional.of(roadmap));

        Milestone milestone = Milestone.builder()
                .id(1L)
                .name("Milestone")
                .startDate(LocalDate.now())
                .finishDate(LocalDate.now().plusDays(10))
                .roadmap(roadmap)
                .build();

        milestoneService.add(milestone.getName(), milestone.getStartDate()
                , milestone.getFinishDate(), milestone.getRoadmap().getId());
        milestoneService.removeById(milestone.getId());

        List<Milestone> capturedMilestones = taskArgumentCaptor.getAllValues();
        verify(milestoneRepository, times(1)).deleteById(milestone.getId());
        assertEquals(0, capturedMilestones.size());
    }
}
