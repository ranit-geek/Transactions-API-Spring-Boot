package com.n26.dao;


import com.n26.helpers.BigDecimalSummaryStatistics;

import org.springframework.stereotype.Repository;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.concurrent.ConcurrentHashMap;



@Repository
@Slf4j
public class TransactionCache {

    private ConcurrentHashMap<Long, BigDecimal> perSecondStatBuckets = new ConcurrentHashMap<>();

    /**
     * @param timeKey timestamp of transaction to be added in millis (UTC)
     * @param value amount of transaction
     * @return saves the incoming transactions
     */
    public BigDecimal insertStatsInCache(Long timeKey, BigDecimal value){

        log.info("Adding Txn to cache::  timestamp:"+timeKey+"  amount:"+value);
        return perSecondStatBuckets.put(timeKey, value);
    }



    /**
     * @param timeKey current timestamp(UTC)
     * @return it compares current timestamp with timestamps in cache and it deletes all entries
     * older than 60 seconds with respect to the incoming current timestamp and returns true if
     * any transaction deleted of false if no transaction deleted
     */
    public boolean removeFromCache(Long timeKey){

        int size= perSecondStatBuckets.size();
        for (Long key : perSecondStatBuckets.keySet()){
            if(timeKey-key>60000){
                log.info("Found older transaction:: removing timestamp:"+key);
                perSecondStatBuckets.remove(key);
            }
        }
        return size==perSecondStatBuckets.size()?false:true;
    }

    /**
     * @return aggregate all the statistics data (used while recalculation)
     */
    public BigDecimalSummaryStatistics aggregate(){
        log.info("calculating aggregated statistics from cache");
        return this.perSecondStatBuckets.values().stream().collect(BigDecimalSummaryStatistics.statistics());

    }

    /**
     * @return checks if the cache is empty
     */
    public boolean isCacheEmpty(){return perSecondStatBuckets.isEmpty();}

    /**
     * @return clears the cache and sends true if its empty or false if its not
     */
    public boolean clearCache()
    {
        log.info("Clearing transactions cache");
        perSecondStatBuckets.clear();
        return this.isCacheEmpty();
    }
}
