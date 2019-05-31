package com.n26.services;


import com.n26.CustomExeptions.InvalidParseException;
import com.n26.dao.TransactionCache;
import com.n26.entities.Statistics;
import com.n26.entities.Transactions;
import com.n26.helpers.BigDecimalConverter;
import com.n26.helpers.BigDecimalSummaryStatistics;
import com.n26.helpers.DateTimeConverter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class StatisticsService {


    @Autowired
    private TransactionCache txnCacheService;
    @Autowired
    private DateTimeConverter timeHelperUtility;
    @Autowired
    private BigDecimalSummaryStatistics statsSummary;
    @Autowired
    private BigDecimalConverter bigDecimalConverter;

    public Statistics getSummary() {
        if (txnCacheService.isCacheEmpty())

            return new Statistics(new BigDecimal(0.00).setScale(2,BigDecimal.ROUND_HALF_UP),
                    new BigDecimal(0.00).setScale(2,BigDecimal.ROUND_HALF_UP),
                    new BigDecimal(0.00).setScale(2,BigDecimal.ROUND_HALF_UP),
                    new BigDecimal(0.00).setScale(2,BigDecimal.ROUND_HALF_UP),0l);

        return new Statistics(statsSummary);
    }

    /**
     * @return Saves the current transaction in statistical summery as well as transaction cache
     * and returns updated statistical summery
     */
    public synchronized Statistics saveStats(Transactions transaction) throws InvalidParseException {

        Statistics updatedStatsSummary;


        statsSummary.accept(bigDecimalConverter.covertStringToBigDecimal(transaction.getAmount()));
        updatedStatsSummary = new Statistics(statsSummary);
        txnCacheService.insertStatsInCache(timeHelperUtility.convertToTimeStamp(transaction.getTimestamp()),
                bigDecimalConverter.covertStringToBigDecimal(transaction.getAmount()));


        return updatedStatsSummary;
    }

    /**
     * @return this is a scheduled job which runs in every millisecond to clean transactions which are older than 60 seconds
     * from the current time. This is a async task which runs in a separate thread.
     */
    @Async
    @Scheduled(fixedDelay = DateTimeConverter.MILLIS_FOR_ONE_SECOND/1000, initialDelay = 0)
    public synchronized BigDecimalSummaryStatistics cleanOldStatsPerSecond() {
        long currentTimeInMilli = timeHelperUtility.currentMillis();
        log.debug("Job running to clean older transactions. current time: {}", currentTimeInMilli);
        if(txnCacheService.removeFromCache(currentTimeInMilli)){
                log.debug("ReCalculating stats summary");
                statsSummary = txnCacheService.aggregate();
        }
        else {
            log.debug("stats older than 60 seconds not found for cleaning");
        }
        return statsSummary;

    }

    public void clearStatsSummery()
    {
        log.info("Clearing statistical summery");
        this.statsSummary.clear();
    }


}
