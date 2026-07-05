package com.incidentlens.backend.service;

public interface AiProviderService {

    String generateText(String prompt);

    float[] embed(String text);
}