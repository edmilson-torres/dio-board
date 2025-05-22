package br.com.dio.service;

import br.com.dio.exception.EntityNotFoundException;
import br.com.dio.persistence.entity.BlockReportEntity;
import lombok.AllArgsConstructor;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.Duration;
import java.util.List;

@AllArgsConstructor
public class BlockReportService {

    private final Connection connection;

    public void generateReport(final Long boardId) throws SQLException {
        var blockQueryService = new BlockQueryService(connection);

        List<BlockReportEntity> blocks = blockQueryService.findAllByBoardId(boardId);

        if (blocks.isEmpty()) {
            throw new EntityNotFoundException("No Blocks found");
        }

        printTable(blocks);
    }

    public static void printTable(List<BlockReportEntity> blocks) {
        if (blocks == null || blocks.isEmpty()) {
            System.out.println("No Blocks to display.");
            return;
        }

        System.out.println("+-------------------------+-------------------------+-------------------------+-------------------------+");
        System.out.println("| Title                   | Duration                | Block Reason            | Unblock Reason          |");
        System.out.println("+-------------------------+-------------------------+-------------------------+-------------------------+");

        for (BlockReportEntity block : blocks) {

            Duration duration = Duration.between(block.getBlockedAt(), block.getUnblockedAt());
            long hours = duration.toHours();
            long remainingMinutes = duration.toMinutesPart();
            long remainingSeconds = duration.toSecondsPart();
            block.setDuration(hours + ":" + remainingMinutes + ":" + remainingSeconds);

            System.out.println(block);
        }

        System.out.println("+-------------------------+-------------------------+-------------------------+-------------------------+");
    }
}
