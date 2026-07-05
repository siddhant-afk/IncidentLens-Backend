package com.incidentlens.backend.service;

public record DocumentChunkEmbedding(
        int chunkIndex,
        String content,
        float[] embedding
) {}