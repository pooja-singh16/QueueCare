package com.queuecare.backend;

import com.queuecare.backend.entity.Doctor;
import com.queuecare.backend.repository.DoctorRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class QueuecareBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(QueuecareBackendApplication.class, args);
    }

    @Bean
    CommandLineRunner initializeDoctors(DoctorRepository doctorRepository) {
        return args -> {

            if (doctorRepository.count() == 0) {

                doctorRepository.save(
                        new Doctor(
                                "Dr. Ananya Sharma",
                                "Cardiologist",
                                true
                        )
                );

                doctorRepository.save(
                        new Doctor(
                                "Dr. Rahul Verma",
                                "Dermatologist",
                                true
                        )
                );

                doctorRepository.save(
                        new Doctor(
                                "Dr. Priya Singh",
                                "Pediatrician",
                                false
                        )
                );

                System.out.println("QueueCare doctors initialized!");
            }
        };
    }
}