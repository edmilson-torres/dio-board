package br.com.dio.persistence.entity;

import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class CardEntity {

    private Long id;
    private String title;
    private String description;
    private BoardColumnEntity boardColumn = new BoardColumnEntity();
    private OffsetDateTime enteredColumnAt;
    private OffsetDateTime createdAt;
}
