package de.telran.businesstracker.controller;

import de.telran.businesstracker.data.Member;
import de.telran.businesstracker.dto.MemberDto;
import de.telran.businesstracker.mapper.MemberMapper;
import de.telran.businesstracker.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/member")
public class MemberController {

    public final MemberService memberService;
    public final MemberMapper memberMapper;

    public MemberController(MemberService memberService, MemberMapper memberMapper) {
        this.memberService = memberService;
        this.memberMapper = memberMapper;
    }

    @PostMapping("")
    public ResponseEntity<MemberDto> createMember(@RequestBody @Valid MemberDto memberDto) throws URISyntaxException {
        Member member = memberService.add(memberDto.position, memberDto.projectId, memberDto.userId);
        memberDto.id = member.getId();
        return ResponseEntity
                .created(new URI("/api/members/" + member.getId()))
                .body(memberDto);
    }

    @PutMapping("")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateMember(@RequestBody @Valid MemberDto memberDto) throws HttpClientErrorException.BadRequest {

        memberService.edit(memberDto.id, memberDto.position);
    }

    @GetMapping("")
    public List<MemberDto> getAllMembers() {
        return memberService.getAll()
                .stream()
                .map(memberMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public MemberDto getMember(@PathVariable Long id) {
        Member member = memberService.getById(id);
        return memberMapper.toDto(member);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMember(@PathVariable Long id) {
        memberService.removeById(id);
    }
}
