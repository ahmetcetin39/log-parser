package com.logparser.parser.service.impl;

import com.logparser.parser.repository.LogRepository;
import com.logparser.parser.service.LogService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

/**
 * This is the test class for {@link LogService}
 * 30.07.2018
 *
 * @author Ahmet Cetin
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class LogServiceImplTest {
    @MockBean
    LogRepository logRepository;

    @Autowired
    LogService logService;

    @Before
    public void setUp() {
        String line1 = "2017-01-01 00:00:11.763|111.111.111.111|'GET / HTTP/1.1'|200|'swcd (unknown version) CFNetwork/808.2.16 Darwin/15.6.0'";
        String line2 = "2017-01-01 00:00:23.003|111.111.111.112|'GET / HTTP/1.1'|200|'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.79 Safari/537.36 Edge/14.14393'";
        List<String> lines = Arrays.asList(line1, line2);

        Path file = Paths.get("access.log");
        try {
            Files.write(file, lines, Charset.forName("UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Mockito.when(logRepository.getAgentByIp("111.111.111.111")).thenReturn("'swcd (unknown version) CFNetwork/808.2.16 Darwin/15.6.0'");
        Mockito.when(logRepository.getAgentByIp("111.111.111.112")).thenReturn("'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.79 Safari/537.36 Edge/14.14393'");
    }

    @Test
    public void whenFilePathIsGiven_logsAreSaved() {
        logService.saveLogFromAccessLogFile("access.log");
        assert logRepository.getAgentByIp("111.111.111.111").equals("'swcd (unknown version) CFNetwork/808.2.16 Darwin/15.6.0'")
                && logRepository.getAgentByIp("111.111.111.112").equals("'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.79 Safari/537.36 Edge/14.14393'");
    }
}