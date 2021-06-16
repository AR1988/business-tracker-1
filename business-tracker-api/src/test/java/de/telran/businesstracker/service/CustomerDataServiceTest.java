package de.telran.businesstracker.service;

import de.telran.businesstracker.model.CustomerData;
import de.telran.businesstracker.repositories.CustomerDataRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerDataServiceTest {

    @Mock
    CustomerDataRepository repository;

    @Mock
    Supplier<String> uuidGenerator;

    @InjectMocks
    CustomerDataService customerDataService;

    @Test
    public void testAdd_successful() {
        String data = "data";
        String email = "email";

        when(uuidGenerator.get()).thenReturn("239");

        CustomerData customerData = customerDataService.add(data, email);

        assertEquals(data, customerData.getData());
        assertEquals(email, customerData.getEmail());
        assertEquals("239", customerData.getFormId());

        verify(repository).save(argThat(argument -> argument == customerData));
    }

    @Test
    public void testUpdate_successful() {
        CustomerData customerDataBefore = new CustomerData("239", "email_before", false, "data_before");
        when(repository.findByFormIdAndIsSubmitted("239", false)).thenReturn(Optional.of(customerDataBefore));

        customerDataService.update("239", "data_new", "email_new");
        verify(repository, times(1)).save(argThat(
                argument -> !argument.isSubmitted()
                        && argument.getData().equals("data_new")
                        && argument.getEmail().equals("email_new")
                        && argument.getFormId().equals("239")));
    }

    @Test
    public void testSubmit_successful() {
        CustomerData customerDataBefore = new CustomerData("239", "email_before", false, "data_before");
        when(repository.findByFormIdAndIsSubmitted("239", false)).thenReturn(Optional.of(customerDataBefore));

        customerDataService.submit("239", "data_new", "email_new");
        verify(repository, times(1)).save(argThat(
                argument -> argument.isSubmitted()
                        && argument.getData().equals("data_new")
                        && argument.getEmail().equals("email_new")
                        && argument.getFormId().equals("239")));
    }

    @Test
    public void testGetByFormIdAndIsSubmitted_successful() {
        CustomerData customerDataBefore = new CustomerData("239", "email_before", false, "data_before");
        when(repository.findByFormIdAndIsSubmitted("239", false)).thenReturn(Optional.of(customerDataBefore));

        assertEquals(customerDataBefore, customerDataService.getByIdAndNotSubmitted("239"));
    }

    @Test
    public void testGetByFormId_successful() {
        CustomerData customerDataBefore = new CustomerData("239", "email_before", false, "data_before");
        when(repository.findById("239")).thenReturn(Optional.of(customerDataBefore));

        assertEquals(customerDataBefore, customerDataService.getById("239"));
    }


    @Test
    public void testUpdate_noFormFound_throwsEntityNotFoundException() {
        when(repository.findByFormIdAndIsSubmitted(anyString(), anyBoolean())).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> customerDataService.update("239", "data", "email"));
    }

    @Test
    public void testSubmit_noFormFound_throwsEntityNotFoundException() {
        when(repository.findByFormIdAndIsSubmitted(anyString(), anyBoolean())).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> customerDataService.submit("239", "data", "email"));
    }

    @Test
    public void testGetByFormIdAndIsSubmitted_noFormFound_throwsEntityNotFoundException() {
        when(repository.findByFormIdAndIsSubmitted(anyString(), anyBoolean())).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> customerDataService.getByIdAndNotSubmitted("239"));
    }

    @Test
    public void testGetById_noFormFound_throwsEntityNotFoundException() {
        when(repository.findById(anyString())).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> customerDataService.getById("239"));
    }
}
