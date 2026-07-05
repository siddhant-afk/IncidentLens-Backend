package com.incidentlens.backend.service;

import com.incidentlens.backend.repository.ChunkSearchProjection;
import com.incidentlens.backend.repository.DocumentChunkRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SemanticSearchService {

    private final InvestigationService investigationService;
    private final DocumentChunkRepository documentChunkRepository;
    private final AiProviderService aiProviderService;

    public SemanticSearchService(InvestigationService investigationService,
                                 DocumentChunkRepository documentChunkRepository,
                                 AiProviderService aiProviderService) {
        this.investigationService = investigationService;
        this.documentChunkRepository = documentChunkRepository;
        this.aiProviderService = aiProviderService;
    }

    public List<SearchResult> search(Long investigationId, String query, int limit) {
        // This also enforces ownership
        investigationService.getInvestigationById(investigationId);

        float[] queryEmbedding = aiProviderService.embed(query);
        String vectorLiteral = toPgVectorLiteral(queryEmbedding);

        return documentChunkRepository.searchTopChunksByInvestigationId(
                        investigationId,
                        vectorLiteral,
                        limit
                ).stream()
                .map(this::toSearchResult)
                .collect(Collectors.toList());
    }

    private SearchResult toSearchResult(ChunkSearchProjection projection) {
        return new SearchResult(
                projection.getChunkId(),
                projection.getChunkIndex(),
                projection.getDocumentFilename(),
                projection.getContent(),
                projection.getDistance()
        );
    }

    private String toPgVectorLiteral(float[] embedding) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < embedding.length; i++) {
            if (i > 0) {
                sb.append(",");
            }
            sb.append(embedding[i]);
        }
        sb.append("]");
        return sb.toString();
    }
}