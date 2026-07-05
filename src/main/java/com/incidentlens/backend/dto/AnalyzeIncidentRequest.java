package com.incidentlens.backend.dto;

import jakarta.validation.constraints.NotBlank;

public class AnalyzeIncidentRequest {

    @NotBlank(message = "Incident description is required")
    private String incidentDescription;

    public AnalyzeIncidentRequest() {
    }

    public String getIncidentDescription() {
        return incidentDescription;
    }

    public void setIncidentDescription(String incidentDescription) {
        this.incidentDescription = incidentDescription;
    }
}