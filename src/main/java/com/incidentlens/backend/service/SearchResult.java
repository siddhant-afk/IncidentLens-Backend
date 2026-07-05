package com.incidentlens.backend.service;

public record SearchResult(
        Long chunkId,
        Integer chunkIndex,
        String documentFilename,
        String content,
        Double distance
) {}