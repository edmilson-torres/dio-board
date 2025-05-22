package br.com.dio.persistence.entity;

import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class BlockReportEntity {

    private Long id;
    private OffsetDateTime blockedAt;
    private String blockReason;
    private OffsetDateTime unblockedAt;
    private String unblockReason;
    private Long cardId;
    private String cardTitle;
    private String duration;

    @Override
    public String toString() {
        return String.format("| %-23s | %23s | %-23s | %-23s |", cardTitle, duration, blockReason, unblockReason);
    }

}
