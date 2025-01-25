package com.example.main.Subscribe.Repository;

import com.example.main.Subscribe.Model.SubscriberMail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriberRepo extends JpaRepository<SubscriberMail, Long> {
}
