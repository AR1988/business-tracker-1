package de.telran.businesstracker.repositories;

import de.telran.businesstracker.model.CustomerData;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerDataRepository extends CrudRepository<CustomerData, String> {

    public Optional<CustomerData> findByFormIdAndIsSubmitted(String formId, boolean isSubmitted);
}
