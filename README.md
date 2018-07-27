Summary
-------
- This application is built using Maven, Java8, Spring Boot and MySQL.

- This application is basically a log file parser. Additionally it is used for searching IPs which made n requests in the given period.

- If there is a log file in the program arguments, the logs are saved to MySQL.
Log file is the optional argument for the application. If it is not given, IPs are searched in existing logs.

- PreparedStatement is used to be able to save the logs in bulk. It makes bulk insertions much quicker than other methods such as standard Spring Data Jpa save method.

- To run this application first create a mysql table on your localhost named 'log_parser' and create a user 'log_parser_application'.
Run installDB.sql to create tables and run the following commands:

With accesslog file:

    java -jar parser.jar --accesslog=/path/to/file --startDate=2017-01-01.13:00:00 --duration=hourly --threshold=100

Without accesslog file:

    java -jar parser.jar --startDate=2017-01-01.13:00:00 --duration=hourly --threshold=100


Java
----
- This is a Java tool which parses and loads given log file to MySQL. Values in the logs are separated with pipe (|).

- The tool takes "accesslog", "startDate", "duration" and "threshold" as command line arguments.
    - "startDate" is of "yyyy-MM-dd.HH:mm:ss" format
    - "duration" can take only "hourly", "daily"
    - "threshold" is an integer value.

SQL
---

- This is the query to find the IPs which made more requests than given number in the given period:

      select l.ip from Log l 
      where l.requestTime > '2017-01-01.13:00:00' and l.requestTime < '2017-01-01.14:00:00'
      group by l.ip having count(l.ip) > 100;

- This is the query to find requests of a given IP:

      select l.* from Log l where ip = '192.168.102.136';