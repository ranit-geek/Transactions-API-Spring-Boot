package com.n26.dao;


import com.n26.helpers.BigDecimalSummaryStatistics;

import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.concurrent.ConcurrentHashMap;

import javaslang.collection.HashMap;
import javaslang.control.Option;

@Repository
public class TransactionCache {

    private ConcurrentHashMap<Long, BigDecimal> perSecondStatBuckets = new ConcurrentHashMap<>();

    public BigDecimal upsertStatsInCache(Long timeKey, BigDecimal value){

        // try to get the existing timestamp from cache.If the result is Option.Some, it indicates that the key is present in cache
        // where we update the existing stats, else create a new bucket for the new timestamp in cache
//        BigDecimalSummaryStatistics stats = getFromCache(timeKey)
//                .map(existingPerSecStat -> {
//                    existingPerSecStat.accept(value);
//                    return existingPerSecStat;
//                })
//                .getOrElse(() -> {
//                    BigDecimalSummaryStatistics newPerSecondStatistic = new BigDecimalSummaryStatistics();
//                    newPerSecondStatistic.accept(value);
//                    return newPerSecondStatistic;
//                });

           return perSecondStatBuckets.putIfAbsent(timeKey, value);

//        return stats;
    }

    /**
     * @param timeKey
     * @return get the summary for the specified key from cache if exists else return None monad type
     */
//    public Option<BigDecimalSummaryStatistics> getFromCache(Long timeKey){
//        return Option.of(perSecondStatBuckets.get(timeKey));
//    }


    /**
     * @param timeKey
     * @return removeFromCache the timeKey if present in perSecStatBuckets and return Option.Some
     * if timeKey is not present it returns Option.None
     */
    public boolean removeFromCache(Long timeKey){

        // the operation is wrapped in Option<T> monad, so that even if the function remove throws a NPE due to null key, the
        // resultant output of the function will be None which is more typesafe

        int size= perSecondStatBuckets.size();
        for (Long key : perSecondStatBuckets.keySet()){
            if(timeKey-key>60000){

                perSecondStatBuckets.remove(key);
            }
        }

        return size==perSecondStatBuckets.size()?false:true;
    }

    /**
     * @return aggregate all the statistics data (used while recalculation)
     */
    public BigDecimalSummaryStatistics aggregate(){
//        return HashMap.ofAll(perSecondStatBuckets)
//                .foldLeft(new BigDecimalSummaryStatistics(), (acc, perSecondStat) -> {
//                    acc.combine(perSecondStat._2());
//                    return acc;
//                });

        return this.perSecondStatBuckets.values().stream().collect(BigDecimalSummaryStatistics.statistics());

    }


    public boolean isCacheEmpty(){return perSecondStatBuckets.isEmpty();}

    public boolean clearCache()
    {
        perSecondStatBuckets.clear();
        return this.isCacheEmpty();
    }
}
