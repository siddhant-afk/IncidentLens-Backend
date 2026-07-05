package com.incidentlens.backend.service;

import com.incidentlens.backend.dto.AnalyzeIncidentResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class IncidentAnalysisService {

    private final InvestigationService investigationService;
    private final SemanticSearchService semanticSearchService;
    private final AiProviderService aiProviderService;

    public IncidentAnalysisService(InvestigationService investigationService,
                                   SemanticSearchService semanticSearchService,
                                   AiProviderService aiProviderService) {
        this.investigationService = investigationService;
        this.semanticSearchService = semanticSearchService;
        this.aiProviderService = aiProviderService;
    }

    public AnalyzeIncidentResponse analyze(Long investigationId, String incidentDescription) {
        // ownership check
        investigationService.getInvestigationById(investigationId);

        List<SearchResult> evidence = semanticSearchService.search(investigationId, incidentDescription, 5);

        String prompt = buildPrompt(incidentDescription, evidence);
        String analysis = aiProviderService.generateText(prompt);

        return new AnalyzeIncidentResponse(analysis, evidence);
    }

    private String buildPrompt(String incidentDescription, List<SearchResult> evidence) {
        String evidenceText = evidence.stream()
                .map(r -> """
                        [Document: %s | Chunk #%d | Distance: %.4f]
                        %s
                        """.formatted(
                        r.documentFilename(),
                        r.chunkIndex(),
                        r.distance(),
                        r.content()
                ))
                .collect(Collectors.joining("\n"));

        return """
                You are an incident investigation assistant.

                Analyze the incident below using only the evidence provided.
                Return a concise but useful analysis with:
                1. likely root cause
                2. supporting evidence
                3. immediate recommendations
                4. next debugging steps

                Incident:
                %s

                Evidence:
                %s
                """.formatted(incidentDescription, evidenceText);
    }
}