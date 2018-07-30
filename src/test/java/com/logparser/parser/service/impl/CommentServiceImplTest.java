package com.logparser.parser.service.impl;

import com.logparser.parser.entity.Comment;
import com.logparser.parser.repository.CommentRepository;
import com.logparser.parser.service.CommentService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * This is the test class for {@link CommentService}
 * 28.07.2018
 *
 * @author Ahmet Cetin
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class CommentServiceImplTest {
    @MockBean
    private CommentRepository commentRepository;

    @Autowired
    private CommentService commentService;

    @Before
    public void setUp() {
        Comment comment1 = new Comment();
        comment1.setIp("128.0.0.1");
        comment1.setDescription("Test comment desc 1");

        Comment comment2 = new Comment();
        comment2.setIp("128.0.0.2");
        comment2.setDescription("Test comment desc 2");

        Mockito.when(commentRepository.findAllByIpIn(Arrays.asList(comment1.getIp(), comment2.getIp()))
        ).thenReturn(Arrays.asList(comment1, comment2));
    }

    @Test
    public void whenValidParameters_thenCommentsShouldBeSaved() {
        List<String> ipList = Arrays.asList("128.0.0.1", "128.0.0.2");

        Comment comment1 = new Comment();
        comment1.setIp(ipList.get(0));
        comment1.setDescription("Test comment desc 1");

        Comment comment2 = new Comment();
        comment2.setIp(ipList.get(1));
        comment2.setDescription("Test comment desc 2");

        commentService.saveComments(ipList, LocalDateTime.now(), LocalDateTime.now(), 10);
        assert commentRepository.findAllByIpIn(ipList).get(0).equals(comment1) &&
                commentRepository.findAllByIpIn(ipList).get(1).equals(comment2);
    }
}