package de.telran.businesstracker.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CustomerData {

    public CustomerData(String formId) {
        this.formId = formId;
    }

    public CustomerData(String formId, String email, boolean isSubmitted, String data) {
        this.formId = formId;
        this.email = email;
        this.isSubmitted = isSubmitted;
        this.data = data;
    }

    @Id
    private String formId;

    @Setter
    private String email;

    private boolean isSubmitted;

    @Setter
    @Column(columnDefinition="TEXT")
    String data;

    public void setSubmitted() {
        isSubmitted = true;
    }
}
