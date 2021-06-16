package de.telran.businesstracker.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.telran.businesstracker.controller.dto.CustomerDataDto;
import de.telran.businesstracker.controller.dto.CustomerDataIdDto;
import de.telran.businesstracker.mapper.CustomerDataMapper;
import de.telran.businesstracker.model.CustomerData;
import de.telran.businesstracker.service.CustomerDataService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customer-data")
public class CustomerDataController {
    private final CustomerDataService customerDataService;
    private final ObjectMapper objectMapper;
    private final CustomerDataMapper mapper;

    public CustomerDataController(CustomerDataService customerDataService, ObjectMapper objectMapper, CustomerDataMapper mapper) {
        this.customerDataService = customerDataService;
        this.objectMapper = objectMapper;
        this.mapper = mapper;
    }

    @PostMapping("")
    public CustomerDataIdDto create(@RequestBody CustomerDataDto customerDataDto) throws JsonProcessingException {
        String rawData = objectMapper.writeValueAsString(customerDataDto);
        CustomerData customerData = customerDataService.add(rawData, customerDataDto.email);
        CustomerDataIdDto idDto = new CustomerDataIdDto();
        idDto.formId = customerData.getFormId();
        return idDto;
    }

    @PutMapping("")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody CustomerDataDto customerDataDto) throws JsonProcessingException {
        String rawData = objectMapper.writeValueAsString(customerDataDto);
        customerDataService.update(customerDataDto.formId, rawData, customerDataDto.email);
    }

    @PutMapping("/submit")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void submit(@RequestBody CustomerDataDto customerDataDto) throws JsonProcessingException {
        String rawData = objectMapper.writeValueAsString(customerDataDto);
        customerDataService.submit(customerDataDto.formId, rawData, customerDataDto.email);
    }

    @GetMapping("/{id}")
    public CustomerDataDto get(@PathVariable String id) throws JsonProcessingException {
        CustomerData customerData = customerDataService.getById(id);
        return mapper.toDto(customerData);
    }

}
