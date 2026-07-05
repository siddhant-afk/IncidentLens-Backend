package com.incidentlens.backend.service;

import com.incidentlens.backend.entity.Document;
import com.incidentlens.backend.entity.DocumentChunk;
import com.incidentlens.backend.repository.DocumentChunkRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class DocumentProcessingService {

    private final DocumentService documentService;
    private final S3Service s3Service;
    private final TextExtractionService textExtractionService;
    private final ChunkingService chunkingService;
    private final AiProviderService aiProviderService;
    private final DocumentChunkRepository documentChunkRepository;

    public DocumentProcessingService(DocumentService documentService,
                                     S3Service s3Service,
                                     TextExtractionService textExtractionService,
                                     ChunkingService chunkingService,
                                     AiProviderService aiProviderService,
                                     DocumentChunkRepository documentChunkRepository) {
        this.documentService = documentService;
        this.s3Service = s3Service;
        this.textExtractionService = textExtractionService;
        this.chunkingService = chunkingService;
        this.aiProviderService = aiProviderService;
        this.documentChunkRepository = documentChunkRepository;
    }

    public List<DocumentChunk> processDocument(Long documentId) throws IOException {
        Document document = documentService.getDocumentMetadata(documentId);

        byte[] fileBytes = s3Service.downloadFile(document.getStoragePath());
        String text = textExtractionService.extractText(
                fileBytes,
                document.getContentType(),
                document.getFilename()
        );

        List<String> chunks = chunkingService.chunkText(text);
        List<DocumentChunk> savedChunks = new ArrayList<>();

        for (int i = 0; i < chunks.size(); i++) {
            String chunkText = chunks.get(i);
            float[] embedding = aiProviderService.embed(chunkText);

            DocumentChunk chunk = new DocumentChunk();
            chunk.setDocument(document);
            chunk.setChunkIndex(i);
            chunk.setContent(chunkText);
            chunk.setEmbedding(embedding);

            savedChunks.add(documentChunkRepository.save(chunk));
        }

        return savedChunks;
    }


}