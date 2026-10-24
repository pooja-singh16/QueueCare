package com.queuecare.backend;

import com.queuecare.backend.entity.Appointment;
import com.queuecare.backend.entity.Doctor;
import com.queuecare.backend.repository.AppointmentRepository;
import com.queuecare.backend.repository.DoctorRepository;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;

    public AppointmentController(
            AppointmentRepository appointmentRepository,
            DoctorRepository doctorRepository) {

        this.appointmentRepository = appointmentRepository;
        this.doctorRepository = doctorRepository;
    }

    @PostMapping
    public Map<String, Object> bookAppointment(
            @RequestBody Map<String, String> request) {

        String patientName = request.get("patientName");
        String doctorIdText = request.get("doctorId");
        String appointmentDateText = request.get("appointmentDate");

        if (patientName == null || patientName.isBlank()
                || doctorIdText == null || doctorIdText.isBlank()
                || appointmentDateText == null
                || appointmentDateText.isBlank()) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "patientName, doctorId and appointmentDate are required"
            );
        }

        Long doctorId;
        LocalDate appointmentDate;

        try {
            doctorId = Long.parseLong(doctorIdText);
            appointmentDate = LocalDate.parse(appointmentDateText);
        } catch (NumberFormatException | DateTimeParseException ex) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "doctorId must be a number and appointmentDate must use YYYY-MM-DD"
            );
        }

        if (appointmentDate.isBefore(LocalDate.now())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Appointment date cannot be in the past"
            );
        }

        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Doctor not found"
                ));

        if (!doctor.isAvailable()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Doctor is not available"
            );
        }

        Appointment appointment = new Appointment(
                patientName.trim(),
                appointmentDate,
                "BOOKED",
                doctor
        );

        Appointment saved = appointmentRepository.save(appointment);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("appointmentId", saved.getId());
        response.put("patientName", saved.getPatientName());
        response.put("doctorId", doctor.getId());
        response.put("doctorName", doctor.getName());
        response.put("appointmentDate", saved.getAppointmentDate());
        response.put("status", saved.getStatus());
        response.put("message", "Appointment booked successfully!");

        return response;
    }

    @GetMapping
    public List<Map<String, Object>> getAppointments() {

        return appointmentRepository.findAll()
                .stream()
                .map(appointment -> {
                    Map<String, Object> response = new LinkedHashMap<>();

                    response.put("appointmentId", appointment.getId());
                    response.put("patientName", appointment.getPatientName());
                    response.put("doctorId", appointment.getDoctor().getId());
                    response.put("doctorName", appointment.getDoctor().getName());
                    response.put(
                            "appointmentDate",
                            appointment.getAppointmentDate()
                    );
                    response.put("status", appointment.getStatus());

                    return response;
                })
                .toList();
    }
}