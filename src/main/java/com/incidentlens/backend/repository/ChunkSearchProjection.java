package com.incidentlens.backend.repository;

public interface ChunkSearchProjection {

    Long getChunkId();
    Integer getChunkIndex();
    String getContent();
    String getDocumentFilename();
    Double getDistance();
}