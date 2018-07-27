package com.logparser.parser.repository;

import com.logparser.parser.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;

/**
 * This is the repository interface which is used for DB operations of {@link Comment} entity.
 * 27.07.2018
 *
 * @author Ahmet Cetin
 */
public interface CommentRepository extends JpaRepository<Comment, Serializable> {
}
