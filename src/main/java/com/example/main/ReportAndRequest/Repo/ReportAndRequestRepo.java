package com.example.main.ReportAndRequest.Repo;

import com.example.main.ReportAndRequest.Entity.ReportAndRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportAndRequestRepo extends JpaRepository<ReportAndRequest,Long> {
}
