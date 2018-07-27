package com.logparser.parser;

import lombok.Data;
import org.springframework.boot.ApplicationArguments;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * This is the model where input parameters {@link ParserApplication} are assigned.
 * 27.07.2018
 *
 * @author Ahmet Cetin
 */
@Data
public class InputParameters {
    private LocalDateTime startDate;
    private String duration;
    private int threshold;
    private String accesslog;

    public InputParameters(ApplicationArguments args) {
        this.startDate = LocalDateTime.parse(args.getOptionValues("startDate").get(0), DateTimeFormatter.ofPattern("yyyy-MM-dd.HH:mm:ss"));
        this.duration = args.getOptionValues("duration").get(0);
        this.threshold = Integer.parseInt(args.getOptionValues("threshold").get(0));

        // accesslog parameter is optional
        if (!StringUtils.isEmpty(args.getOptionValues("accesslog"))) {
            this.accesslog = args.getOptionValues("accesslog").get(0);
        }
    }
}
