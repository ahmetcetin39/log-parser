package com.logparser.parser.service.impl;

import com.logparser.parser.entity.Comment;
import com.logparser.parser.repository.CommentRepository;
import com.logparser.parser.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * This is the service implementation for comment services.
 * 27.07.2018
 *
 * @author Ahmet Cetin
 */
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private static final DateTimeFormatter DTF_COMMENT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final CommentRepository commentRepository;

    @Override
    public void saveComments(List<String> ipList, LocalDateTime startTime, LocalDateTime endTime, int threshold) {
        List<Comment> comments = new ArrayList<>();
        for (String ip : ipList) {
            Comment comment = new Comment();
            comment.setIp(ip);
            comment.setDescription("This ip has reached more than " + threshold + " requests between " + startTime.format(DTF_COMMENT) + " and " + endTime.format(DTF_COMMENT));
            comments.add(comment);
        }
        commentRepository.saveAll(comments);
        commentRepository.flush();
    }
}
