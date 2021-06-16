package de.telran.businesstracker.controller;

import de.telran.businesstracker.model.Milestone;
import de.telran.businesstracker.controller.dto.MilestoneDto;
import de.telran.businesstracker.mapper.MilestoneMapper;
import de.telran.businesstracker.service.MilestoneService;
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
@RequestMapping("/api/milestones")
public class MilestoneController {

    private final MilestoneService milestoneService;
    private final MilestoneMapper milestoneMapper;

    public MilestoneController(MilestoneService milestoneService, MilestoneMapper milestoneMapper) {
        this.milestoneService = milestoneService;
        this.milestoneMapper = milestoneMapper;
    }

    @PostMapping("")
    public ResponseEntity<MilestoneDto> createMilestone(@RequestBody @Valid MilestoneDto milestoneDto) throws URISyntaxException {
        Milestone milestone = milestoneService.add(milestoneDto.name, milestoneDto.startDate, milestoneDto.finishDate, milestoneDto.roadmapId);
        milestoneDto.id = milestone.getId();
        return ResponseEntity
                .created(new URI("/api/milestones/" + milestone.getId()))
                .body(milestoneDto);
    }

    @PutMapping("")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateMilestone(@RequestBody @Valid MilestoneDto milestoneDto) throws HttpClientErrorException.BadRequest {

        milestoneService.edit(milestoneDto.id, milestoneDto.name, milestoneDto.startDate, milestoneDto.finishDate);
    }

    @GetMapping("")
    public List<MilestoneDto> getAllMilestones() {
        return milestoneService.getAll()
                .stream()
                .map(milestoneMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public MilestoneDto getMilestone(@PathVariable Long id) {
        Milestone milestone = milestoneService.getById(id);
        return milestoneMapper.toDto(milestone);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMilestone(@PathVariable Long id) {
        milestoneService.removeById(id);
    }
}
