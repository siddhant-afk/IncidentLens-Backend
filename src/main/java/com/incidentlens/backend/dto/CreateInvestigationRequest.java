package com.incidentlens.backend.dto;

import jakarta.validation.constraints.NotBlank;

public class CreateInvestigationRequest {

    @NotBlank(message = "Title cannot be blank")
    private String title;

    @NotBlank(message = "Description required")
    private String description;

    public CreateInvestigationRequest() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
