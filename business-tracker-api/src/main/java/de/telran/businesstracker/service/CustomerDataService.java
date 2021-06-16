package de.telran.businesstracker.service;

import de.telran.businesstracker.model.CustomerData;
import de.telran.businesstracker.repositories.CustomerDataRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.function.Supplier;

@Service
public class CustomerDataService {
    private final String CUSTOMER_DATA_NOT_FOUND = "Form not found or already submitted";

    private final CustomerDataRepository customerDataRepository;
    private final Supplier<String> uuidGenerator;

    public CustomerDataService(CustomerDataRepository customerDataRepository, Supplier<String> uuidGenerator) {
        this.customerDataRepository = customerDataRepository;
        this.uuidGenerator = uuidGenerator;
    }

    /**
     * Receives a new form, creates a new id and seves into DB
     *
     * @param rawData all data of the form in json format
     * @param email   the email, retrieved from the form data
     * @return a created entity with id
     */
    public CustomerData add(String rawData, String email) {
        String formId = uuidGenerator.get();

        CustomerData customerData = new CustomerData(formId, email, false, rawData);
        customerDataRepository.save(customerData);
        return customerData;
    }

    /**
     * Updates existing form, unless it is submitted
     *
     * @param formId  the id of the form
     * @param rawData all data in json format
     * @param email   the email, retrieved from the form data
     * @throws EntityNotFoundException if no form found
     */
    public void update(String formId, String rawData, String email) {
        CustomerData customerData = getByIdAndNotSubmitted(formId);

        customerData.setData(rawData);
        customerData.setEmail(email);
        customerDataRepository.save(customerData);
    }

    /**
     * Submits the form. Makes the form submitted. After calling this method, the form cannot be changed.
     * Also updates the data related to the form
     *
     * @param formId  the id of the form
     * @param rawData all data in json format
     * @param email   the email, retrieved from the form data
     * @throws EntityNotFoundException if no form found
     */
    public void submit(String formId, String rawData, String email) {
        CustomerData customerData = getByIdAndNotSubmitted(formId);
        customerData.setSubmitted();
        customerData.setData(rawData);
        customerData.setEmail(email);
        customerDataRepository.save(customerData);
    }

    /**
     * Finds form by its id and not submitted
     *
     * @param formId the id
     * @return an entity
     * @throws EntityNotFoundException if no form found
     */
    public CustomerData getByIdAndNotSubmitted(String formId) {
        return customerDataRepository.findByFormIdAndIsSubmitted(formId, false)
                .orElseThrow(() -> new EntityNotFoundException(CUSTOMER_DATA_NOT_FOUND));
    }

    /**
     * Finds form by its id
     *
     * @param formId the id
     * @return an entity
     * @throws EntityNotFoundException if no form found
     */
    public CustomerData getById(String formId) {
        return customerDataRepository.findById(formId)
                .orElseThrow(() -> new EntityNotFoundException(CUSTOMER_DATA_NOT_FOUND));
    }

}
