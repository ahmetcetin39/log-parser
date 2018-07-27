package com.logparser.parser.service;

import com.logparser.parser.InputParameters;

import java.time.LocalDateTime;
import java.util.List;

/**
 * This is the service interface where logging operations are handled.
 * 27.07.2018
 *
 * @author Ahmet Cetin
 */
public interface LogService {
    /**
     * It is used to save the logs which are read from the given file path.
     *
     * @param filePath: the file to read the logs.
     */
    void saveLogFromAccessLogFile(String filePath);

    /**
     * It is used to get the search results from the log according to the given parameters.
     *
     * @param inputParameters: the application parameters which are used for searching the logs.
     * @param endTime:         the endTime of search.
     * @return the list of ips of log results.
     */
    List<String> getSearchResultFromLog(InputParameters inputParameters, LocalDateTime endTime);
}
