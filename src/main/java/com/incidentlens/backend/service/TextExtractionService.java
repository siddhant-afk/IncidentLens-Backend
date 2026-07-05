package com.incidentlens.backend.service;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service
public class TextExtractionService {

    public String extractText(byte[] fileBytes, String contentType, String filename) throws IOException {
        if (isPlainText(contentType, filename)) {
            return new String(fileBytes, StandardCharsets.UTF_8);
        }

        if (isPdf(contentType, filename)) {
            return extractPdfText(fileBytes);
        }

        throw new IllegalArgumentException("Unsupported file type: " + filename);
    }

    private boolean isPlainText(String contentType, String filename) {
        return "text/plain".equalsIgnoreCase(contentType)
                || filename.toLowerCase().endsWith(".txt")
                || filename.toLowerCase().endsWith(".log");
    }

    private boolean isPdf(String contentType, String filename) {
        return "application/pdf".equalsIgnoreCase(contentType)
                || filename.toLowerCase().endsWith(".pdf");
    }

    private String extractPdfText(byte[] fileBytes) throws IOException {
        try (PDDocument document = Loader.loadPDF(new ByteArrayInputStream(fileBytes).readAllBytes())) {
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(document);
        }
    }
}