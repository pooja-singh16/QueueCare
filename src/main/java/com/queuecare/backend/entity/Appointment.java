
package com.queuecare.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "appointments")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String patientName;
    private LocalDate appointmentDate;
    private String status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    public Appointment() {
    }

    public Appointment(
            String patientName,
            LocalDate appointmentDate,
            String status,
            Doctor doctor) {
        this.patientName = patientName;
        this.appointmentDate = appointmentDate;
        this.status = status;
        this.doctor = doctor;
    }

    public Long getId() {
        return id;
    }

    public String getPatientName() {
        return patientName;
    }

    public LocalDate getAppointmentDate() {
        return appointmentDate;
    }

    public String getStatus() {
        return status;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
