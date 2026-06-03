# Patient Web Service - Tổng hợp bài Logging 1 đến 4

Project Spring Boot dùng **Gradle**, **PostgreSQL**, **Spring Data JPA**, **SLF4J**, **Logback** và **Lombok**.

## Nội dung đã triển khai

| Bài | Nội dung |
|---|---|
| Bài 1 | `@Slf4j`, log `INFO` khi thêm bệnh nhân, log `WARN` nếu tuổi lớn hơn `120` |
| Bài 2 | `root=INFO`, Hibernate SQL ở mức `DEBUG`, package `com.medical.service` ở mức `TRACE` |
| Bài 3 | Ghi log vào `logs/patient_web_service.log`, giới hạn `2MB`, nén `.gz`, giữ lịch sử `7` ngày |
| Bài 4 | `@RestControllerAdvice`, bắt `Exception.class`, ghi stack trace bằng `log.error("Lỗi hệ thống xảy ra: ", e)` |

## Yêu cầu

- JDK 17 trở lên
- IntelliJ IDEA
- PostgreSQL hoặc Docker Desktop

## Cách 1: chạy PostgreSQL bằng Docker Compose

Mở Terminal tại thư mục project và chạy:

```bash
docker compose up -d
```

Database mặc định:

```text
Database: medical_db
Username: postgres
Password: 123456
Port:     5432
```

## Cách 2: dùng PostgreSQL đã cài trên máy

Mở pgAdmin hoặc PostgreSQL Query Tool và chạy file:

```text
database/create_database.sql
```

Nếu mật khẩu PostgreSQL trên máy không phải `123456`, sửa dòng sau trong:

```text
src/main/resources/application.properties
```

```properties
spring.datasource.password=${DB_PASSWORD:123456}
```

Ví dụ mật khẩu thật là `admin`, đổi thành:

```properties
spring.datasource.password=${DB_PASSWORD:admin}
```

## Chạy project trong IntelliJ

1. Giải nén file ZIP.
2. Mở IntelliJ IDEA.
3. Chọn **Open** và chọn thư mục `patient-web-service`.
4. Đợi IntelliJ tải Gradle dependency.
5. Mở class:

```text
src/main/java/com/medical/PatientWebServiceApplication.java
```

6. Nhấn **Run**.

Hibernate sẽ tự tạo bảng `patients` trong database `medical_db`.

## Test API

Có thể chạy trực tiếp các request trong file:

```text
requests/patient-api.http
```

Hoặc dùng Postman.

### 1. Thêm bệnh nhân bình thường: xem INFO

```http
POST http://localhost:8080/api/patients
Content-Type: application/json
```

```json
{
  "name": "Nguyen Van An",
  "age": 21
}
```

### 2. Thêm bệnh nhân tuổi quá cao: xem WARN

```http
POST http://localhost:8080/api/patients
Content-Type: application/json
```

```json
{
  "name": "Tran Van B",
  "age": 135
}
```

### 3. Tìm kiếm bệnh nhân: xem SQL DEBUG và service TRACE

```http
GET http://localhost:8080/api/patients/search?keyword=An
```

### 4. Kiểm tra Exception Handling: tạo lỗi chia cho 0

```http
GET http://localhost:8080/api/patients/test-error?number=0
```

Client nhận JSON an toàn:

```json
{
  "timestamp": "...",
  "status": 500,
  "error": "Internal Server Error",
  "message": "Hệ thống đang gặp sự cố. Vui lòng thử lại sau.",
  "path": "/api/patients/test-error"
}
```

Trong Console và file log sẽ có stack trace, bao gồm vị trí lỗi trong `PatientController.testError(...)`.

## File log

Sau khi chạy ứng dụng, kiểm tra:

```text
logs/patient_web_service.log
```

Khi file đạt `2MB`, file cũ sẽ được nén dạng:

```text
patient_web_service.log.2026-06-03.0.gz
```

Để test rotation nhanh, tạm đổi:

```properties
logging.logback.rollingpolicy.max-file-size=10KB
```

Sau khi kiểm tra xong, đổi lại:

```properties
logging.logback.rollingpolicy.max-file-size=2MB
```

## Chạy profile production minh họa

Profile `prod` chỉ ghi lỗi nghiêm trọng:

```bash
gradle bootRun --args='--spring.profiles.active=prod'
```

Trong IntelliJ, có thể thêm Program arguments:

```text
--spring.profiles.active=prod
```

## Lưu ý

- Endpoint `/api/patients/test-error` chỉ dùng để demo bài 4. Nên xóa endpoint này khi xây dựng ứng dụng thực tế.
- Trong hệ thống y tế thực tế, không nên ghi bệnh án, số căn cước, địa chỉ hoặc thông tin nhạy cảm vào log.
- File ZIP không kèm Gradle Wrapper. IntelliJ vẫn có thể import `build.gradle` và tải Gradle dependency. Nếu chạy bằng Terminal, máy cần cài Gradle hoặc tạo Gradle Wrapper từ IntelliJ.
