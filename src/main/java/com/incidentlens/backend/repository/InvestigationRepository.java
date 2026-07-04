package com.incidentlens.backend.repository;

import com.incidentlens.backend.entity.Investigation;
import com.incidentlens.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InvestigationRepository extends JpaRepository<Investigation,Long> {


    List<Investigation> findByUserOrderByCreatedAtDesc(User user);
}
