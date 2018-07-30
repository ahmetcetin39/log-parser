package com.logparser.parser.repository;

import com.logparser.parser.entity.Log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * This is the repository interface which is used for DB operations of {@link Log} entity.
 * 27.07.2018
 *
 * @author Ahmet Cetin
 */
@Repository
public interface LogRepository extends JpaRepository<Log, Serializable> {
    @Query("select l.ip from Log l where l.requestTime > ?1 and l.requestTime < ?2 group by l.ip having count(l.ip) > ?3")
    List<String> getIpsWhichPassedThresholdBetweenTimes(LocalDateTime startTime, LocalDateTime endTime, long threshold);

    @Query("select l.agent from Log l where l.ip = ?1")
    String getAgentByIp(String ip);
}
