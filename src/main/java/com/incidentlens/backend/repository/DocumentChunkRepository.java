package com.incidentlens.backend.repository;

import com.incidentlens.backend.entity.Document;
import com.incidentlens.backend.entity.DocumentChunk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DocumentChunkRepository extends JpaRepository<DocumentChunk, Long> {

    List<DocumentChunk> findByDocumentOrderByChunkIndexAsc(Document document);

    @Query(value = """
            SELECT
                dc.id AS chunkId,
                dc.chunk_index AS chunkIndex,
                dc.content AS content,
                d.filename AS documentFilename,
                (dc.embedding <=> CAST(:queryEmbedding AS vector)) AS distance
            FROM document_chunks dc
            JOIN documents d ON d.id = dc.document_id
            WHERE d.investigation_id = :investigationId
            ORDER BY dc.embedding <=> CAST(:queryEmbedding AS vector)
            LIMIT :limit
            """, nativeQuery = true)
    List<ChunkSearchProjection> searchTopChunksByInvestigationId(
            @Param("investigationId") Long investigationId,
            @Param("queryEmbedding") String queryEmbedding,
            @Param("limit") int limit
    );


}