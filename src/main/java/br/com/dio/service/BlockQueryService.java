package br.com.dio.service;

import br.com.dio.persistence.dao.BlockDAO;
import br.com.dio.persistence.dao.BoardDAO;
import br.com.dio.persistence.entity.BlockEntity;
import br.com.dio.persistence.entity.BlockReportEntity;
import lombok.AllArgsConstructor;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class BlockQueryService {

    private final Connection connection;

    public List<BlockReportEntity> findAllByBoardId(final Long boardId) throws SQLException {
        var dao = new BlockDAO(connection);

        return dao.findAllByBoardId(boardId);
    }
}
