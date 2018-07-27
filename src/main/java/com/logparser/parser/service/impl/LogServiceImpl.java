package com.logparser.parser.service.impl;

import com.logparser.parser.InputParameters;
import com.logparser.parser.entity.Log;
import com.logparser.parser.repository.LogRepository;
import com.logparser.parser.service.LogService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.FileReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * This is the service implementation for log services.
 * 27.07.2018
 *
 * @author Ahmet Cetin
 */
@Service
@RequiredArgsConstructor
public class LogServiceImpl implements LogService {
    private static final DateTimeFormatter DTF_LOG_FILE = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
    private static final char LINE_SEPARATOR = '|';
    private static final int PREPARED_STATEMENT_BULK_INSERTION_SIZE = 10_000;
    private static final String DATASOURCE_URL_PARAMETERS = "&useServerPrepStmts=false&rewriteBatchedStatements=true";
    private static final String INSERT_SQL_LOGS = "INSERT INTO logs " + "(request_time, ip, request_method, response_code, agent) VALUES (?,?,?,?,?)";
    private final Environment environment;
    private final LogRepository logRepository;
    private String jdbcUrl;
    private String jdbcUsername;
    private String jdbcPassword;

    @PostConstruct
    private void init() {
        this.jdbcUrl = environment.getRequiredProperty("spring.datasource.url") + DATASOURCE_URL_PARAMETERS;
        this.jdbcUsername = environment.getProperty("spring.datasource.username");
        this.jdbcPassword = environment.getProperty("spring.datasource.password");
    }

    @Override
    public void saveLogFromAccessLogFile(String filePath) {
        List<Log> logsToSave = new ArrayList<>();
        try {
            Reader in = new FileReader(filePath);
            Iterable<CSVRecord> recordsInCsv = CSVFormat.RFC4180.withDelimiter(LINE_SEPARATOR).parse(in);
            recordsInCsv.forEach(r -> logsToSave.add(convertCsvRecordToLogEntity(r)));
            saveLogs(logsToSave);
            System.out.println("Logs are saved to DB successfully!");
        } catch (Exception e) {
            System.out.println("Could not save logs to DB!" + e.getMessage());
        }
    }

    @Override
    public List<String> getSearchResultFromLog(InputParameters inputParameters, LocalDateTime endTime) {
        return logRepository.getIpsWhichPassedThresholdBetweenTimes(inputParameters.getStartDate(), endTime, (long) inputParameters.getThreshold());
    }

    /**
     * This method is used to convert a csvRecord to a {@link Log} entity.
     */
    private Log convertCsvRecordToLogEntity(CSVRecord csvRecord) {
        Log log = new Log();
        log.setRequestTime(LocalDateTime.parse(csvRecord.get(0), DTF_LOG_FILE));
        log.setIp(csvRecord.get(1));
        log.setRequestMethod(csvRecord.get(2));
        log.setResponseCode(csvRecord.get(3));
        log.setAgent(csvRecord.get(4));
        return log;
    }

    /**
     * This method is used to save the logs in bulk using PreparedStatement.
     * The reason to insert the logs this way is to make it much faster than standard repository save method.
     */
    private void saveLogs(List<Log> logs) {
        try (Connection connToLogParser = DriverManager.getConnection(jdbcUrl, jdbcUsername, jdbcPassword);
             PreparedStatement stmtLogParser = connToLogParser.prepareStatement(INSERT_SQL_LOGS)) {
            connToLogParser.setAutoCommit(false);

            int counter = 0;
            for (Log log : logs) {
                stmtLogParser.setTimestamp(1, Timestamp.valueOf(log.getRequestTime()));
                stmtLogParser.setString(2, log.getIp());
                stmtLogParser.setString(3, log.getRequestMethod());
                stmtLogParser.setString(4, log.getResponseCode());
                stmtLogParser.setString(5, log.getAgent());
                stmtLogParser.addBatch();
                counter++;

                if (counter % PREPARED_STATEMENT_BULK_INSERTION_SIZE == 0) {
                    stmtLogParser.executeBatch();
                    connToLogParser.commit();
                    stmtLogParser.clearBatch();
                    counter = 0;
                }
            }

            if (counter != 0) {
                stmtLogParser.executeBatch();
                connToLogParser.commit();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
