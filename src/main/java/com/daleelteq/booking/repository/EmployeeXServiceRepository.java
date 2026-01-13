package com.daleelteq.booking.repository;

import com.daleelteq.booking.domain.EmployeeXService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EmployeeXServiceRepository extends JpaRepository<EmployeeXService, Long> {
    List<EmployeeXService> findByIdEAndDate(Long employeeId, LocalDate date);
    List<EmployeeXService> findByStatus(String status);
    List<EmployeeXService> findByDate(LocalDate date);
    List<EmployeeXService> findByIdE(Long employeeId);
    List<EmployeeXService> findByIdS(Long serviceId);
}
