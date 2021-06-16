package de.telran.businesstracker.controller;

import de.telran.businesstracker.model.Member;
import de.telran.businesstracker.model.Milestone;
import de.telran.businesstracker.model.Project;
import de.telran.businesstracker.model.Roadmap;
import de.telran.businesstracker.model.Task;
import de.telran.businesstracker.model.User;
import de.telran.businesstracker.repositories.MemberRepository;
import de.telran.businesstracker.repositories.MilestoneRepository;
import de.telran.businesstracker.repositories.ProjectRepository;
import de.telran.businesstracker.repositories.RoadmapRepository;
import de.telran.businesstracker.repositories.UserRepository;
import de.telran.businesstracker.service.TaskService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class TaskControllerTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    MilestoneRepository milestoneRepository;
    @Autowired
    ProjectRepository projectRepository;
    @Autowired
    RoadmapRepository roadmapRepository;
    @Autowired
    TaskService taskService;

    @Test
    public void integrationTestTask() {
        User user = new User();
        userRepository.save(user);
        Project project = Project.builder().build();
        Roadmap roadmap = Roadmap.builder().build();
        projectRepository.save(project);
        roadmapRepository.save(roadmap);
       // Member member = new Member(1L, "Boss", project, user);
        Member member = Member.builder().build();
        memberRepository.save(member);
        Milestone milestone = Milestone.builder().build();
       // Milestone milestone = new Milestone(2L, "Milestone", LocalDate.now(), LocalDate.now().plusDays(3), roadmap);
        milestoneRepository.save(milestone);
        Task task = taskService.add("Task1", false, milestone.getId(), member.getId());

        Assertions.assertEquals("Task1", task.getName());
        Assertions.assertEquals(false, task.getFinished());
        Assertions.assertEquals(milestone.getId(), task.getMilestone().getId());
        Assertions.assertEquals(member.getId(), task.getResponsibleMember().getId());

        taskService.edit(task.getId(), "Task2", true);
       Task editedTask = taskService.getById(task.getId());
        Assertions.assertEquals("Task2", editedTask.getName());
        Assertions.assertEquals(true, editedTask.getFinished());

        Task task1 = taskService.add("Task1", false, milestone.getId(), member.getId());
        Assertions.assertEquals("Task2", taskService.getById(task.getId()).getName());

        List<Task> expected = taskService.getAll();
        Assertions.assertEquals(expected.size(), 2);
        Assertions.assertEquals(expected.get(0).getName(), "Task2");

        taskService.removeById(task1.getId());
        taskService.removeById(task.getId());

        List<Task> expected1 = taskService.getAll();
        Assertions.assertEquals(expected1.size(), 0);

        milestoneRepository.delete(milestone);
        memberRepository.delete(member);
        roadmapRepository.delete(roadmap);
        projectRepository.delete(project);
        userRepository.delete(user);

    }
}
