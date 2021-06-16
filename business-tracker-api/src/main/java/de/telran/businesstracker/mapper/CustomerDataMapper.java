package de.telran.businesstracker.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.telran.businesstracker.controller.dto.CustomerDataDto;
import de.telran.businesstracker.model.CustomerData;
import org.springframework.stereotype.Service;

@Service
public class CustomerDataMapper {

    private final ObjectMapper objectMapper;

    public CustomerDataMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public CustomerDataDto toDto(CustomerData customerData) throws JsonProcessingException {
        CustomerDataDto res = objectMapper.readValue(customerData.getData(), CustomerDataDto.class);
        res.formId = customerData.getFormId();
        res.isSubmitted = customerData.isSubmitted();
        return res;
    }
}
