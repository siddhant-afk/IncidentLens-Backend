package com.incidentlens.backend.controller;

import com.incidentlens.backend.dto.AnalyzeIncidentRequest;
import com.incidentlens.backend.dto.AnalyzeIncidentResponse;
import com.incidentlens.backend.service.IncidentAnalysisService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/investigations")
public class IncidentAnalysisController {

    private final IncidentAnalysisService incidentAnalysisService;

    public IncidentAnalysisController(IncidentAnalysisService incidentAnalysisService) {
        this.incidentAnalysisService = incidentAnalysisService;
    }

    @PostMapping("/{investigationId}/analyze")
    public ResponseEntity<AnalyzeIncidentResponse> analyze(
            @PathVariable Long investigationId,
            @Valid @RequestBody AnalyzeIncidentRequest request) {

        return ResponseEntity.ok(
                incidentAnalysisService.analyze(investigationId, request.getIncidentDescription())
        );
    }
}