package de.telran.businesstracker.controller;

import de.telran.businesstracker.data.Milestone;
import de.telran.businesstracker.dto.KpiDto;
import de.telran.businesstracker.dto.KpiDtoToAdd;
import de.telran.businesstracker.mapper.KpiMapper;
import de.telran.businesstracker.service.MilestoneService;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/kpi")
public class KpiController {

    MilestoneService milestoneService;
    KpiMapper mapper;

    public KpiController(MilestoneService milestoneService, KpiMapper mapper) {
        this.milestoneService = milestoneService;
        this.mapper = mapper;
    }

    @PostMapping("")
    public void addKpi(@RequestBody KpiDtoToAdd kpi) {
        milestoneService.addKpi(kpi.mileStoneId, kpi.name);
    }


    @GetMapping("/by-ms")
    public List<KpiDto> getAllByMileStoneId(@RequestParam long msId) {
        return milestoneService.getAllKpisByMsId(msId).stream().map(kpiName -> mapper.toDto(kpiName, msId)).collect(Collectors.toList());
    }

    @GetMapping("/by-project")
    public List<KpiDto> getAllByProjectId(@RequestParam long projectId) {
        return milestoneService.getAllKpisByProjectId(projectId).stream().map(kpiName -> mapper.toDto(kpiName, 0)).collect(Collectors.toList());
    }

    @DeleteMapping("")
    public void deleteKpi(@RequestParam long mileStoneId, @RequestParam String name) {
        milestoneService.removeKpi(mileStoneId, name);
    }
}
