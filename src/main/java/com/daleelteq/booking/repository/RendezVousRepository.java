package com.daleelteq.booking.repository;

import com.daleelteq.booking.domain.RendezVous;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RendezVousRepository extends JpaRepository<RendezVous, Long> {
    List<RendezVous> findByStatus(String status);
    List<RendezVous> findByIdC(Long clientId);
    List<RendezVous> findByIdES(Long esId);
}
