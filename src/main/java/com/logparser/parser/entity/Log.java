package com.logparser.parser.entity;

import com.logparser.parser.converter.LocalDateTimeAttributeConverter;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * This is the entity where logs are kept which are read from the log file.
 * 27.07.2018
 *
 * @author Ahmet Cetin
 */
@Entity
@Data
@Table(name = "logs")
public class Log {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_id", nullable = false)
    private int logId;

    @Column(name = "request_time", nullable = false)
    @Convert(converter = LocalDateTimeAttributeConverter.class)
    private LocalDateTime requestTime;

    @Column(name = "ip", nullable = false)
    private String ip;

    @Column(name = "request_method", nullable = false)
    private String requestMethod;

    @Column(name = "response_code", nullable = false)
    private String responseCode;

    @Column(name = "agent", nullable = false)
    private String agent;
}
