package com.logparser.parser.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * This is the entity where log search results are kept.
 * 27.07.2018
 *
 * @author Ahmet Cetin
 */
@Entity
@Data
@Table(name = "comment")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id", nullable = false)
    private int commentId;

    @Column(name = "ip", nullable = false)
    private String ip;

    @Column(name = "description", nullable = false)
    private String description;
}
