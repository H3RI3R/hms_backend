package com.example.main.Report.Repo;

import com.example.main.Login.Entity.LoginModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginHistoryRepo extends JpaRepository<LoginModel,Long> {

}
