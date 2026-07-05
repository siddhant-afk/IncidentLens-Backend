package com.incidentlens.backend.controller;


import com.incidentlens.backend.entity.Document;
import com.incidentlens.backend.service.DocumentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/investigations/{investigationId}/documents")
public class DocumentController {

    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @PostMapping
    public ResponseEntity<Map<String,Object>> uploadDocument(
            @PathVariable Long investigationId,
            @RequestParam("file")MultipartFile file) throws IOException{

        Document document = documentService.uploadDocument(investigationId,file);

        return ResponseEntity.ok(Map.of(
                "id", document.getId(),
                "filename", document.getFilename(),
                "contentType", document.getContentType(),
                "size", document.getSize()
        ));
    }

    @GetMapping
    public ResponseEntity<List<Document>> getDocuments(
            @PathVariable Long investigationId
    ){
        return ResponseEntity.ok(
                documentService.getDocumentsForInvestigation(investigationId)
        );
    }

    @GetMapping("/{documentId}/download")
    public ResponseEntity<byte[]> downloadDocument(
            @PathVariable Long investigationId,
            @PathVariable Long documentId) {

        Document document = documentService.getDocumentMetadata(documentId); // or whatever you name it
        byte[] file = documentService.downloadDocument(documentId);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + document.getFilename() + "\"")
                .contentType(MediaType.parseMediaType(document.getContentType()))
                .body(file);
    }

    @DeleteMapping("/{documentId}")
    public ResponseEntity<Void> deleteDocument(
            @PathVariable Long investigationId,
            @PathVariable Long documentId) {

        documentService.deleteDocument(documentId);
        return ResponseEntity.noContent().build();
    }


}
