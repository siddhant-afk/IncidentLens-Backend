package com.incidentlens.backend.service;

import com.google.genai.Client;
import com.google.genai.types.ContentEmbedding;
import com.google.genai.types.GenerateContentResponse;
import com.google.genai.types.EmbedContentResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GeminiAiProviderService implements AiProviderService {

    private final Client client;

    @Value("${gemini.generate-model}")
    private String generateModel;

    @Value("${gemini.embedding-model}")
    private String embeddingModel;

    public GeminiAiProviderService(Client client) {
        this.client = client;
    }

    @Override
    public String generateText(String prompt) {
        GenerateContentResponse response =
                client.models.generateContent(generateModel, prompt, null);

        return response.text();
    }

    @Override
    public float[] embed(String text) {
        EmbedContentResponse response =
                client.models.embedContent(embeddingModel, text, null);

        List<ContentEmbedding> embeddings = response.embeddings()
                .orElseThrow(() -> new IllegalStateException("No embeddings returned"));

        if (embeddings.isEmpty()) {
            throw new IllegalStateException("No embeddings returned");
        }

        List<Float> values = embeddings.get(0).values()
                .orElseThrow(() -> new IllegalStateException("No embedding values returned"));

        float[] vector = new float[values.size()];
        for (int i = 0; i < values.size(); i++) {
            vector[i] = values.get(i);
        }

        return vector;
    }
}