package com.queuecare.backend.repository;

import com.queuecare.backend.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AppointmentRepository
        extends JpaRepository<Appointment, Long> {

    List<Appointment> findByPatientNameIgnoreCase(String patientName);
}