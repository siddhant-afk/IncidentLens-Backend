package com.incidentlens.backend.service;


import com.incidentlens.backend.entity.Document;
import com.incidentlens.backend.entity.Investigation;
import com.incidentlens.backend.entity.User;
import com.incidentlens.backend.repository.DocumentRepository;
import com.incidentlens.backend.repository.InvestigationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final InvestigationRepository investigationRepository;
    private final AuthenticationService authenticationService;
    private final S3Service s3Service;

    public DocumentService(DocumentRepository documentRepository, InvestigationRepository investigationRepository, AuthenticationService authenticationService, S3Service s3Service) {
        this.documentRepository = documentRepository;
        this.investigationRepository = investigationRepository;
        this.authenticationService = authenticationService;
        this.s3Service = s3Service;
    }

    public List<Document> getDocumentsForInvestigation(Long investigationId){

        Investigation investigation = getOwnedInvestigation(investigationId);
        return documentRepository.findByInvestigationOrderByCreatedAtDesc(investigation);
    }

    public Document uploadDocument(Long investigationId, MultipartFile file) throws IOException{

        Investigation investigation = getOwnedInvestigation(investigationId);

        String storageKey = s3Service.uploadFile(file);

        Document document = new Document();

        document.setFilename(file.getOriginalFilename());
        document.setContentType(file.getContentType());
        document.setSize(file.getSize());
        document.setStoragePath(storageKey);
        document.setInvestigation(investigation);

        return documentRepository.save(document);
    }

    @Transactional(readOnly = true)
    public byte[] downloadDocument(Long documentId) {

        User currentUser = authenticationService.getCurrentUser();

        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new RuntimeException("Document not found"));

        if (!document.getInvestigation()
                .getUser()
                .getId()
                .equals(currentUser.getId())) {

            throw new RuntimeException("Access denied");
        }

        return s3Service.downloadFile(document.getStoragePath());
    }

    @Transactional
    public void deleteDocument(Long documentId) {
        Document document = getDocumentMetadata(documentId);

        s3Service.deleteFile(document.getStoragePath());
        documentRepository.delete(document);
    }

    @Transactional(readOnly = true)
    public Document getDocumentMetadata(Long documentId) {
        User currentUser = authenticationService.getCurrentUser();

        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new RuntimeException("Document not found"));

        if (!document.getInvestigation().getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("Access denied");
        }

        return document;
    }

    private Investigation getOwnedInvestigation(Long investigationId){

        User currentUser = authenticationService.getCurrentUser();

        Investigation investigation = investigationRepository.findById(investigationId)
                .orElseThrow(() -> new RuntimeException("Investigation not found."));

        if(!investigation.getUser().getId().equals(currentUser.getId()))
            throw new RuntimeException("You do not have access to this investigation");

        return investigation;
    }
}
