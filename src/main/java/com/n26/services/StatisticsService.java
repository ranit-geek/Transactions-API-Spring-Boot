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
import java.util.concurrent.locks.ReentrantLock;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class StatisticsService {

    @Autowired
    private TransactionCache statsCacheService;
    @Autowired
    private DateTimeConverter timeHelperUtility;
    @Autowired
    private BigDecimalSummaryStatistics statsSummary;
    @Autowired
    private BigDecimalConverter bigDecimalConverter;

    public Statistics getSummary() {
        if (statsCacheService.isCacheEmpty())

            return new Statistics(new BigDecimal(0.00).setScale(2,BigDecimal.ROUND_HALF_UP),
                    new BigDecimal(0.00).setScale(2,BigDecimal.ROUND_HALF_UP),
                    new BigDecimal(0.00).setScale(2,BigDecimal.ROUND_HALF_UP),
                    new BigDecimal(0.00).setScale(2,BigDecimal.ROUND_HALF_UP),0l);




        return new Statistics(statsSummary);
    }

    synchronized Statistics saveStats(Transactions transaction) throws InvalidParseException {

        // this operation is wrapped in synchronized block as statsSummary object is even accessed by the scheduled job
        // which is responsible for cleaning old transactions
        Statistics updatedStatsSummary = null;

        synchronized (StatisticsService.class) {


            statsSummary.accept(bigDecimalConverter.covertStringToBigDecimal(transaction.getAmount()));
            System.out.println(new Statistics(statsSummary));

            updatedStatsSummary = new Statistics(statsSummary);

            // update the per second stats buffer with the new transaction
            statsCacheService.upsertStatsInCache(timeHelperUtility.converToTimeStamp(transaction.getTimestamp()),
                    bigDecimalConverter.covertStringToBigDecimal(transaction.getAmount()));
        }

        return updatedStatsSummary;
    }

    /**
     * @return this is a scheduled job which runs every second to clean/prune transactions which are older than 60 seconds
     * from the current time. This is a async task which runs in a separate thread.
     */
    @Async
    @Scheduled(fixedDelay = DateTimeConverter.MILLIS_FOR_ONE_SECOND/1000, initialDelay = 0)
    public synchronized BigDecimalSummaryStatistics cleanOldStatsPerSecond() {
        long nowInSeconds = timeHelperUtility.currentMillis();
        ReentrantLock lock = new ReentrantLock();
        lock.lock();

//        long oldestTimeInSeconds = nowInSeconds - 60000;

        // remove the entry from cache which is older than last sixty seconds if present which will return Option.Some on which
        // we recalculate the stats again, else if the timeKey is not present it returns None on which we do nothing

        try
        {
            if(statsCacheService.removeFromCache(nowInSeconds)){
                synchronized (StatisticsService.class) {

                    statsSummary = statsCacheService.aggregate();
                }

            }

            return statsSummary;

        }
        finally {
            lock.unlock();
        }


    }

    public void clearStatsSummery()
    {
        this.statsSummary.clear();
    }


}
