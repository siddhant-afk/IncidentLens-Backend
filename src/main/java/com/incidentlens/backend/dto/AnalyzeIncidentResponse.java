package com.incidentlens.backend.dto;

import com.incidentlens.backend.service.SearchResult;

import java.util.List;

public class AnalyzeIncidentResponse {

    private String analysis;
    private List<SearchResult> evidence;

    public AnalyzeIncidentResponse() {
    }

    public AnalyzeIncidentResponse(String analysis, List<SearchResult> evidence) {
        this.analysis = analysis;
        this.evidence = evidence;
    }

    public String getAnalysis() {
        return analysis;
    }

    public void setAnalysis(String analysis) {
        this.analysis = analysis;
    }

    public List<SearchResult> getEvidence() {
        return evidence;
    }

    public void setEvidence(List<SearchResult> evidence) {
        this.evidence = evidence;
    }
}