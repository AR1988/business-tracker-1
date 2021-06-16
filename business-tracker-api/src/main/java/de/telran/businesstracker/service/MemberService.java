package de.telran.businesstracker.service;

import de.telran.businesstracker.model.Member;
import de.telran.businesstracker.model.Project;
import de.telran.businesstracker.model.User;
import de.telran.businesstracker.repositories.MemberRepository;
import de.telran.businesstracker.repositories.ProjectRepository;
import de.telran.businesstracker.repositories.UserRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
public class MemberService {

    static final String PROJECT_DOES_NOT_EXIST = "Error! This project doesn't exist in our DB";
    static final String USER_DOES_NOT_EXIST = "Error! This user doesn't exist in our DB";
    static final String MEMBER_DOES_NOT_EXIST = "Error! This member doesn't exist in our DB";

    MemberRepository memberRepository;
    ProjectRepository projectRepository;
    UserRepository userRepository;

    public MemberService(MemberRepository memberRepository, ProjectRepository projectRepository, UserRepository userRepository) {
        this.memberRepository = memberRepository;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    public Member add(String position, Long projectId, Long userId) {
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new EntityNotFoundException(PROJECT_DOES_NOT_EXIST));
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException(USER_DOES_NOT_EXIST));
        Member member = Member.builder().position(position).project(project).user(user).build();
        memberRepository.save(member);
        return member;
    }

    public void edit(Long id, String position) {
        Member member = getById(id);
        member.setPosition(position);
        memberRepository.save(member);
    }

    public List<Member> getAll() {
        return new ArrayList<>(memberRepository.findAll());
    }

    public Member getById(Long id) {
        return memberRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(MEMBER_DOES_NOT_EXIST));
    }

    public void removeById(Long id) {
        memberRepository.deleteById(id);
    }
}



