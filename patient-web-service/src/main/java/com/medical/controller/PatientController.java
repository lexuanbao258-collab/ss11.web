package com.medical.controller;

import com.medical.model.Patient;
import com.medical.service.PatientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;

    @PostMapping
    public ResponseEntity<Patient> addPatient(@RequestBody Patient patient) {
        log.info("Có yêu cầu thêm bệnh nhân mới: name={}", patient.getName());

        if (patient.getAge() != null && patient.getAge() > 120) {
            log.warn("Tuổi bệnh nhân quá cao: name={}, age={}",
                    patient.getName(),
                    patient.getAge());
        }

        Patient savedPatient = patientService.addPatient(patient);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedPatient);
    }

    @GetMapping
    public ResponseEntity<List<Patient>> findAll() {
        log.info("Có yêu cầu lấy danh sách bệnh nhân");
        return ResponseEntity.ok(patientService.findAll());
    }

    @GetMapping("/search")
    public ResponseEntity<List<Patient>> searchPatient(@RequestParam String keyword) {
        log.info("Có yêu cầu tìm kiếm bệnh nhân: keyword={}", keyword);
        return ResponseEntity.ok(patientService.searchByName(keyword));
    }

    /**
     * Endpoint chỉ dùng để kiểm tra bài 4: gọi number=0 để tạo lỗi 500 tại runtime.
     * Sau khi hoàn thành bài tập, nên xóa endpoint này khỏi ứng dụng thực tế.
     */
    @GetMapping("/test-error")
    public ResponseEntity<String> testError(@RequestParam int number) {
        int result = 10 / number;
        return ResponseEntity.ok("Kết quả = " + result);
    }
}
