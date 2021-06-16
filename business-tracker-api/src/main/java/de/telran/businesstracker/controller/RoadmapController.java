package de.telran.businesstracker.controller;

import de.telran.businesstracker.model.Roadmap;
import de.telran.businesstracker.controller.dto.RoadmapDto;
import de.telran.businesstracker.mapper.RoadmapMapper;
import de.telran.businesstracker.service.RoadmapService;
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
@RequestMapping("/api/roadmap")
public class RoadmapController {

    private final RoadmapService roadmapService;
    private final RoadmapMapper roadmapMapper;

    public RoadmapController(RoadmapService roadmapService, RoadmapMapper roadmapMapper) {
        this.roadmapService = roadmapService;
        this.roadmapMapper = roadmapMapper;
    }

    @PostMapping("")
    public ResponseEntity<RoadmapDto> createRoadmap(@RequestBody @Valid RoadmapDto roadmapDto) throws URISyntaxException {
        Roadmap roadmap = roadmapService.add(roadmapDto.name, roadmapDto.startDate, roadmapDto.projectId);
        roadmapDto.id = roadmap.getId();
        return ResponseEntity
                .created(new URI("/api/roadmaps/" + roadmap.getId()))
                .body(roadmapDto);
    }

    @PutMapping("")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateRoadmap(@RequestBody @Valid RoadmapDto roadmapDto) throws HttpClientErrorException.BadRequest {
        roadmapService.edit(roadmapDto.id, roadmapDto.name, roadmapDto.startDate);
    }

    @GetMapping("")
    public List<RoadmapDto> getAllRoadmaps() {
        return roadmapService.getAll()
                .stream()
                .map(roadmapMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public RoadmapDto getRoadmap(@PathVariable Long id) {
        Roadmap roadmap = roadmapService.getById(id);
        return roadmapMapper.toDto(roadmap);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRoadmap(@PathVariable Long id) {
        roadmapService.removeById(id);
    }
}
