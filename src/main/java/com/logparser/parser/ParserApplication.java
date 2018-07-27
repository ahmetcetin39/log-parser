package com.logparser.parser;

import com.logparser.parser.service.CommentService;
import com.logparser.parser.service.LogService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
@RequiredArgsConstructor
public class ParserApplication implements ApplicationRunner {
    private static final List<String> COMPULSORY_INPUT_PARAMETERS = Arrays.asList("startDate", "duration", "threshold");
    private final LogService logService;
    private final CommentService commentService;

    public static void main(String[] args) {
        SpringApplication.run(ParserApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) {
        if (!areInputsValid(args)) {
            System.out.println("You have to give startDate, duration, threshold and accesslog parameters!");
            return;
        }

        InputParameters inputParameters = new InputParameters(args);
        if (!StringUtils.isEmpty(inputParameters.getAccesslog())) {
            // There are logs to save
            logService.saveLogFromAccessLogFile(inputParameters.getAccesslog());
        }

        // Find the end time to query for logs
        LocalDateTime endTime = getEndTimeToSearch(inputParameters.getStartDate(), inputParameters.getDuration());
        // Get ip list from the logs
        List<String> ipList = logService.getSearchResultFromLog(inputParameters, endTime);

        printIncomingArgs(args);
        if (CollectionUtils.isEmpty(ipList)) {
            System.out.println("No IP found!");
            return;
        }

        // Save ips with comments
        commentService.saveComments(ipList, inputParameters.getStartDate(), endTime, inputParameters.getThreshold());
        ipList.forEach(System.out::println);
    }

    private boolean areInputsValid(ApplicationArguments args) {
        for (String inputParameter : COMPULSORY_INPUT_PARAMETERS) {
            if (!args.containsOption(inputParameter)) {
                return false;
            }
        }
        return true;
    }

    private LocalDateTime getEndTimeToSearch(LocalDateTime startTime, String duration) {
        if (duration.equals("hourly")) {
            return startTime.plusHours(1);
        } else if (duration.equals("daily")) {
            return startTime.plusDays(1);
        }

        System.out.println("Duration can only be either hourly or daily!");
        return null;
    }

    private void printIncomingArgs(ApplicationArguments args) {
        // Print found IPs to command line
        System.out.println("--------------------\n| GIVEN PARAMETERS |\n--------------------");
        System.out.println("startDate: " + args.getOptionValues("startDate").get(0));
        System.out.println("duration: " + args.getOptionValues("duration").get(0));
        System.out.println("threshold: " + args.getOptionValues("threshold").get(0));
        if (!CollectionUtils.isEmpty(args.getOptionValues("accesslog"))) {
            System.out.println("accesslog: " + args.getOptionValues("accesslog").get(0));
        }
        System.out.println("-----------\n| IP LIST |\n-----------");
    }
}
