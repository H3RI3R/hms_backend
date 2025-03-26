package com.example.main.Report.Repo;

import com.example.main.Report.Entity.NotificationHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NotificationHistoryRepository extends JpaRepository<NotificationHistory, Long> {
    Page<NotificationHistory> findByUserEmail(String email, Pageable pageable);

    Page<NotificationHistory> findByDateTimeBetween(LocalDateTime fromDate, LocalDateTime toDate, Pageable pageable);

    List<NotificationHistory> findByUserEmail(String email);

    List<NotificationHistory> findByDateTimeBetween(LocalDateTime fromDate, LocalDateTime toDate);
}