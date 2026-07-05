package com.incidentlens.backend.controller;

import com.incidentlens.backend.service.SearchResult;
import com.incidentlens.backend.service.SemanticSearchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/investigations")
public class SemanticSearchController {

    private final SemanticSearchService semanticSearchService;

    public SemanticSearchController(SemanticSearchService semanticSearchService) {
        this.semanticSearchService = semanticSearchService;
    }

    @GetMapping("/{investigationId}/search")
    public ResponseEntity<List<SearchResult>> search(
            @PathVariable Long investigationId,
            @RequestParam("q") String query,
            @RequestParam(defaultValue = "5") int limit) {

        return ResponseEntity.ok(
                semanticSearchService.search(investigationId, query, limit)
        );
    }
}