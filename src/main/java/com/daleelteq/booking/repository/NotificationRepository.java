package com.daleelteq.booking.repository;

import com.daleelteq.booking.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByIdR(Long rendezVousId);
    List<Notification> findByType(String type);
}
