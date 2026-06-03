package com.medical.service;

import com.medical.model.Patient;
import com.medical.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;

    public Patient addPatient(Patient patient) {
        log.trace("Bắt đầu lưu bệnh nhân: name={}", patient.getName());

        Patient savedPatient = patientRepository.save(patient);

        log.info("Đã lưu bệnh nhân thành công: id={}, name={}, age={}",
                savedPatient.getId(),
                savedPatient.getName(),
                savedPatient.getAge());

        return savedPatient;
    }

    public List<Patient> searchByName(String keyword) {
        log.trace("Bắt đầu tìm kiếm bệnh nhân với từ khóa: {}", keyword);

        List<Patient> patients = patientRepository.findByNameContainingIgnoreCase(keyword);

        log.trace("Số bệnh nhân tìm thấy: {}", patients.size());
        return patients;
    }

    public List<Patient> findAll() {
        log.trace("Bắt đầu lấy danh sách bệnh nhân");

        List<Patient> patients = patientRepository.findAll();

        log.trace("Tổng số bệnh nhân: {}", patients.size());
        return patients;
    }
}
