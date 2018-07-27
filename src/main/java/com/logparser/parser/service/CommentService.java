package com.logparser.parser.service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * This is the service interface where ips are saved with comments are saved according to search criteria.
 * 27.07.2018
 *
 * @author Ahmet Cetin
 */
public interface CommentService {
    /**
     * This method is used to save ips with comments.
     *
     * @param ips:       the ips to save.
     * @param startTime: the start time of searching
     * @param endTime:   the end time of searching
     * @param threshold: the number of records looked for between startTime and endTime
     */
    void saveComments(List<String> ips, LocalDateTime startTime, LocalDateTime endTime, int threshold);
}
