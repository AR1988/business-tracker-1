package de.telran.businesstracker.service;

import de.telran.businesstracker.model.Project;
import de.telran.businesstracker.model.Roadmap;
import de.telran.businesstracker.model.User;
import de.telran.businesstracker.repositories.ProjectRepository;
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
class RoadmapServiceTest {

    @Mock
    ProjectRepository projectRepository;

    @Mock
    RoadmapRepository roadmapRepository;

    @InjectMocks
    RoadmapService roadmapService;

    @Test
    public void testAdd_success() {
        User user = new User(2L);
        Project project = new Project(4L, "Great project", user);

        when(projectRepository.findById(project.getId())).thenReturn(Optional.of(project));

        Roadmap roadmap = Roadmap.builder()
                .id(1L)
                .name("Roadmap")
                .startDate(LocalDate.now())
                .project(project)
                .build();

        roadmapService.add(roadmap.getName(), roadmap.getStartDate(), roadmap.getProject().getId());

        verify(roadmapRepository, times(1)).save(any());
        verify(roadmapRepository, times(1))
                .save(argThat(savedRoadmap -> savedRoadmap.getName().equals(roadmap.getName()) &&
                        savedRoadmap.getStartDate().equals(roadmap.getStartDate()) &&
                        savedRoadmap.getProject().getId().equals(project.getId()))
                );
    }

    @Test
    public void testAdd_projectDoesNotExist_EntityNotFoundException() {
        User user = new User(2L);
        Project project = new Project(4L, "Great project", user);

        Roadmap roadmap = Roadmap.builder()
                .id(1L)
                .name("Roadmap")
                .startDate(LocalDate.now())
                .project(project)
                .build();

        Exception exception = assertThrows(EntityNotFoundException.class, () ->
                roadmapService.add(roadmap.getName(), roadmap.getStartDate(), roadmap.getProject().getId() + 1));

        verify(projectRepository, times(1)).findById(any());
        assertEquals("Error! This project doesn't exist in our DB", exception.getMessage());
    }

    @Test
    public void roadmapEdit_roadmapExist_fieldsChanged() {

        User user = new User(2L);
        Project project = new Project(4L, "Great project", user);

        Roadmap roadmap = Roadmap.builder()
                .id(1L)
                .name("Roadmap")
                .startDate(LocalDate.now())
                .project(project)
                .build();

        String newName = "New roadmap";
        LocalDate newStartDay = LocalDate.now().plusDays(1);

        when(roadmapRepository.findById(roadmap.getId())).thenReturn(Optional.of(roadmap));
        roadmapService.edit(roadmap.getId(), newName, newStartDay);

        verify(roadmapRepository, times(1)).save(any());
        verify(roadmapRepository, times(1))
                .save(argThat(savedRoadmap -> savedRoadmap.getName().equals(newName) &&
                        savedRoadmap.getStartDate().equals(newStartDay) &&
                        savedRoadmap.getProject().getId().equals(project.getId()))
                );
    }

    @Test
    void getAll_twoObjects() {
        User user = new User(2L);
        Project project = new Project(4L, "Great project", user);

        Roadmap roadmap1 = Roadmap.builder()
                .id(1L)
                .name("Roadmap")
                .startDate(LocalDate.now())
                .project(project)
                .build();

        Roadmap roadmap2 = Roadmap.builder()
                .id(7L)
                .name("Roadmap")
                .startDate(LocalDate.now())
                .project(project)
                .build();

        List<Roadmap> roadmaps = new ArrayList<>();

        roadmaps.add(roadmap1);
        roadmaps.add(roadmap2);

        when(roadmapRepository.findAll()).thenReturn(roadmaps);

        assertEquals(roadmap1.getName(), roadmapService.getAll().get(0).getName());
        assertEquals(roadmap1.getStartDate(), roadmapService.getAll().get(0).getStartDate());
        assertEquals(roadmap1.getProject(), roadmapService.getAll().get(0).getProject());

        assertEquals(roadmap2.getName(), roadmapService.getAll().get(1).getName());
        assertEquals(roadmap2.getStartDate(), roadmapService.getAll().get(1).getStartDate());
        assertEquals(roadmap2.getProject(), roadmapService.getAll().get(1).getProject());
    }

    @Test
    void testGetById_objectExist() {
        User user = new User(2L);
        Project project = new Project(4L, "Great project", user);

        Roadmap roadmap = Roadmap.builder()
                .id(1L)
                .name("Roadmap")
                .startDate(LocalDate.now())
                .project(project)
                .build();

        when(roadmapRepository.findById(roadmap.getId())).thenReturn(Optional.of(roadmap));
        Roadmap expectedRoadmap = roadmapService.getById(roadmap.getId());

        assertEquals(expectedRoadmap.getName(), roadmap.getName());
        assertEquals(expectedRoadmap.getStartDate(), roadmap.getStartDate());
        assertEquals(expectedRoadmap.getProject(), roadmap.getProject());

        verify(roadmapRepository, times(1)).findById(argThat(
                id -> id.equals(roadmap.getId())));
    }

    @Test
    void testGetById_objectNotExist() {
        User user = new User(2L);
        Project project = new Project(4L, "Great project", user);

        Roadmap roadmap = Roadmap.builder()
                .id(1L)
                .name("Roadmap")
                .startDate(LocalDate.now())
                .project(project)
                .build();

        Exception exception = assertThrows(EntityNotFoundException.class,
                () -> roadmapService.getById(roadmap.getId() + 1));

        verify(roadmapRepository, times(1)).findById(any());
        assertEquals("Error! This roadmap doesn't exist in our DB", exception.getMessage());

    }

    @Captor
    ArgumentCaptor<Roadmap> taskArgumentCaptor;

    @Test
    void removeById_oneObjectDeleted() {
        User user = new User(2L);
        Project project = new Project(4L, "Great project", user);

        Roadmap roadmap = Roadmap.builder()
                .id(1L)
                .name("Roadmap")
                .startDate(LocalDate.now())
                .project(project)
                .build();

        when(projectRepository.findById(project.getId())).thenReturn(Optional.of(project));

        roadmapService.add(roadmap.getName(), roadmap.getStartDate(), roadmap.getProject().getId());
        roadmapService.removeById(roadmap.getId());

        List<Roadmap> capturedRoadmaps = taskArgumentCaptor.getAllValues();
        verify(roadmapRepository, times(1)).deleteById(roadmap.getId());
        assertEquals(0, capturedRoadmaps.size());
    }
}
