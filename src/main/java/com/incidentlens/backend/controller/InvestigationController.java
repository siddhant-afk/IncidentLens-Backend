package com.incidentlens.backend.controller;


import com.incidentlens.backend.dto.CreateInvestigationRequest;
import com.incidentlens.backend.dto.UpdateInvestigationRequest;
import com.incidentlens.backend.entity.Investigation;
import com.incidentlens.backend.service.InvestigationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/investigations")
public class InvestigationController {


    private final InvestigationService investigationService;

    public InvestigationController(InvestigationService investigationService) {
        this.investigationService = investigationService;
    }

    @PostMapping
    public ResponseEntity<Investigation> create(@Valid @RequestBody CreateInvestigationRequest request){

        Investigation investigation = investigationService.createInvestigation(
                request.getTitle(),
                request.getDescription());

        return ResponseEntity.ok(investigation);
    }

    @GetMapping
    public ResponseEntity<List<Investigation>> getMyInvestigation(){

        return ResponseEntity.ok(investigationService.getMyInvestigations());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Investigation> getInvestigationById(@PathVariable Long id){
        return ResponseEntity.ok(investigationService.getInvestigationById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Investigation> updateInvestigation(@PathVariable Long id, @Valid @RequestBody UpdateInvestigationRequest request){
        return ResponseEntity.ok(investigationService.updateInvestigation(id, request.getTitle(),
                request.getDescription(),
                request.getStatus()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInvestigation(@PathVariable Long id){
        investigationService.deleteInvestigation(id);

        return ResponseEntity.noContent().build();
    }
}
