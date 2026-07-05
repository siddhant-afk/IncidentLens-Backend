package com.incidentlens.backend.repository;


import com.incidentlens.backend.entity.Document;
import com.incidentlens.backend.entity.Investigation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentRepository extends JpaRepository<Document,Long> {

    List<Document> findByInvestigationOrderByCreatedAtDesc(Investigation investigation);
}
