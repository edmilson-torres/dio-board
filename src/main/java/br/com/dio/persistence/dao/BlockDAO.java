package br.com.dio.persistence.dao;

import br.com.dio.persistence.entity.BlockEntity;
import br.com.dio.persistence.entity.BlockReportEntity;
import br.com.dio.persistence.entity.BoardColumnEntity;
import lombok.AllArgsConstructor;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static br.com.dio.persistence.converter.OffsetDateTimeConverter.toTimestamp;
import static br.com.dio.persistence.entity.BoardColumnKindEnum.findByName;

@AllArgsConstructor
public class BlockDAO {

    private final Connection connection;

    public void block(final String reason, final Long cardId) throws SQLException {
        var sql = "INSERT INTO BLOCKS (blocked_at, block_reason, card_id) VALUES (?, ?, ?);";
        try(var statement = connection.prepareStatement(sql)){
            var i = 1;
            statement.setTimestamp(i ++, toTimestamp(OffsetDateTime.now()));
            statement.setString(i ++, reason);
            statement.setLong(i, cardId);
            statement.executeUpdate();
        }
    }

    public void unblock(final String reason, final Long cardId) throws SQLException{
        var sql = "UPDATE BLOCKS SET unblocked_at = ?, unblock_reason = ? WHERE card_id = ? AND unblock_reason IS NULL;";
        try(var statement = connection.prepareStatement(sql)){
            var i = 1;
            statement.setTimestamp(i ++, toTimestamp(OffsetDateTime.now()));
            statement.setString(i ++, reason);
            statement.setLong(i, cardId);
            statement.executeUpdate();
        }
    }

    public List<BlockEntity> findAll() throws SQLException{
        List<BlockEntity> entities = new ArrayList<>();
        var sql = "SELECT id, blocked_at, block_reason, unblocked_at, unblock_reason, card_id FROM BLOCKS";
        try(var statement = connection.prepareStatement(sql)){
            statement.executeQuery();
            var resultSet = statement.getResultSet();
            while (resultSet.next()){
                var entity = new BlockEntity();
                entity.setId(resultSet.getLong("id"));
                entity.setBlockedAt(OffsetDateTime.parse(resultSet.getString("blocked_at")));
                entity.setUnblockedAt(OffsetDateTime.parse(resultSet.getString("unblocked_at")));
                entity.setBlockReason(resultSet.getString("block_reason"));
                entity.setUnblockReason(resultSet.getString("unblocked_at"));
                entity.setCardId(resultSet.getLong("card_id"));
                entities.add(entity);
            }
            return entities;
        }
    }

    public List<BlockReportEntity> findAllByBoardId(Long boardId) throws SQLException {
        List<BlockReportEntity> entities = new ArrayList<>();
        var sql = "SELECT b.id, b.blocked_at, b.unblocked_at, b.block_reason, b.unblock_reason, b.card_id, c.title FROM BLOCKS b INNER JOIN CARDS c ON b.card_id = c.id WHERE c.board_id = ?";
        try(var statement = connection.prepareStatement(sql)){
            statement.setLong(1, boardId);
            statement.executeQuery();
            var resultSet = statement.getResultSet();
            while (resultSet.next()){
                var entity = new BlockReportEntity();
                entity.setId(resultSet.getLong("b.id"));
                entity.setUnblockReason(resultSet.getString("b.unblock_reason"));
                entity.setCardId(resultSet.getLong("b.card_id"));
                entity.setCardTitle(resultSet.getString("c.title"));
                entity.setBlockReason(resultSet.getString("b.block_reason"));

                String timestampBlockedAt = resultSet.getString("b.blocked_at");
                String timestampUnblockedAt = resultSet.getString("b.unblocked_at");

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

                OffsetDateTime blockedAt = OffsetDateTime.of(
                        java.time.LocalDateTime.parse(timestampBlockedAt, formatter),
                        ZoneOffset.UTC
                );

                OffsetDateTime unblockedAt = OffsetDateTime.of(
                        java.time.LocalDateTime.parse(timestampUnblockedAt, formatter),
                        ZoneOffset.UTC
                );

                entity.setBlockedAt(blockedAt);
                entity.setUnblockedAt(unblockedAt);


                entities.add(entity);
            }
            return entities;
        }
    }}
